package com.easytrip.controllers;

import com.easytrip.entities.Avis;
import com.easytrip.services.AvisService;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherTousAvisController implements Initializable {

    @FXML
    private TreeTableView<AvisWithType> userAvisTreeTable;

    @FXML
    private TreeTableColumn<AvisWithType, Integer> colAvisId;

    @FXML
    private TreeTableColumn<AvisWithType, String> colUserName;

    @FXML
    private TreeTableColumn<AvisWithType, String> colResType;

    @FXML
    private TreeTableColumn<AvisWithType, Integer> colRating;

    @FXML
    private TreeTableColumn<AvisWithType, String> colDetails;

    @FXML
    private TreeTableColumn<AvisWithType, String> colDateAvis;

    @FXML
    private TreeTableColumn<AvisWithType, String> colActionBtn;

    @FXML
    private TextField searchField;

    private final AvisService avisService = new AvisService();
    private FilteredList<AvisWithType> filteredAvisList; // Store FilteredList as a field to reuse in reloadAvisData

    // Class to hold Avis and reservation type
    public static class AvisWithType extends RecursiveTreeObject<AvisWithType> {
        Avis avis;
        String type;

        AvisWithType(Avis avis, String type) {
            this.avis = avis;
            this.type = type;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up the columns with proper ObservableValue
        colAvisId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getValue().avis.getId_avis()).asObject());
        colResType.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().type));
        colRating.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getValue().avis.getNote()).asObject());
        colDetails.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().avis.getDescription()));
        colDateAvis.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getValue().avis.getDate_avis() != null ?
                        cellData.getValue().getValue().avis.getDate_avis().toString() : ""));
        colUserName.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getValue().avis.getNomUtilisateur() + " " +
                                cellData.getValue().getValue().avis.getPrenomUtilisateur()
                )
        );
        // Add cell factory for action column
        colActionBtn.setCellFactory(param -> new TreeTableCell<AvisWithType, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Button visualiserButton = new Button("🔍");
                    visualiserButton.setOnAction(e -> visualiserAvis(getTreeTableRow().getItem()));

                    Button sendSmsButton = new Button("📱");
                    sendSmsButton.setOnAction(e -> sendSms(getTreeTableRow().getItem()));

                    Button supprimerButton = new Button("❌");
                    supprimerButton.setOnAction(e -> supprimerAvis(getTreeTableRow().getItem()));

                    HBox hbox = new HBox(10);
                    hbox.getChildren().addAll(visualiserButton, sendSmsButton, supprimerButton);
                    hbox.setStyle("-fx-alignment: center;");
                    setGraphic(hbox);
                }
            }
        });

        // Load data into the TreeTableView with filtering
        ObservableList<AvisWithType> avisList = loadAvisWithTypes();
        filteredAvisList = new FilteredList<>(avisList, p -> true);

        // Set up search field listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAvisList.setPredicate(avisWithType -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true; // Show all if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Search in username, firstname, description, type, and date with null checks
                String fullName = (avisWithType.avis.getNomUtilisateur() != null ? avisWithType.avis.getNomUtilisateur() : "") + " " +
                        (avisWithType.avis.getPrenomUtilisateur() != null ? avisWithType.avis.getPrenomUtilisateur() : "");
                String description = avisWithType.avis.getDescription() != null ? avisWithType.avis.getDescription() : "";
                String type = avisWithType.type != null ? avisWithType.type : "";
                String date = avisWithType.avis.getDate_avis() != null ? avisWithType.avis.getDate_avis().toString() : "";
                return fullName.toLowerCase().contains(lowerCaseFilter)
                        || description.toLowerCase().contains(lowerCaseFilter)
                        || type.toLowerCase().contains(lowerCaseFilter)
                        || date.toLowerCase().contains(lowerCaseFilter);
            });
            userAvisTreeTable.refresh(); // Force refresh to ensure UI updates
        });

        // Set up the TreeTableView with filtered data
        TreeItem<AvisWithType> root = new RecursiveTreeItem<>(filteredAvisList, RecursiveTreeObject::getChildren);
        userAvisTreeTable.setRoot(root);
        userAvisTreeTable.setShowRoot(false);
    }

    private ObservableList<AvisWithType> loadAvisWithTypes() {
        ObservableList<AvisWithType> list = FXCollections.observableArrayList();
        List<Avis> avisList = avisService.getAllAvisWithUserInfo();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetdev", "root", "")) {
            String query = """
                SELECT r.id_service, r.id_hotel, r.id_vol
                FROM reservations r
                WHERE r.id_reservation = ?
            """;
            PreparedStatement ps = conn.prepareStatement(query);

            for (Avis avis : avisList) {
                ps.setInt(1, avis.getId_reservation());
                ResultSet rs = ps.executeQuery();

                StringBuilder type = new StringBuilder();
                if (rs.next()) {
                    if (rs.getInt("id_service") != 0) type.append("Service ");
                    if (rs.getInt("id_hotel") != 0) type.append("Hôtel ");
                    if (rs.getInt("id_vol") != 0) type.append("Vol ");
                }

                String finalType = type.toString().trim();
                if (finalType.isEmpty()) finalType = "Aucun";

                list.add(new AvisWithType(avis, finalType));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private void visualiserAvis(AvisWithType avisWithType) {
        Avis avis = avisWithType.avis;
        String type = avisWithType.type;
        openVisualiserAvisWindow(avis, type);
    }

    private void openVisualiserAvisWindow(Avis avis, String type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VisualiserAvispourAdmin.fxml"));
            Parent root = loader.load();

            VisualiserAvisController controller = loader.getController();
            controller.setAvis(avis);
            controller.setType(type);

            Stage stage = new Stage();
            stage.setTitle("Détails de l'Avis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void supprimerAvis(AvisWithType avisWithType) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Voulez-vous vraiment supprimer cet avis ?");
        alert.setContentText("Description : " + avisWithType.avis.getDescription());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            avisService.supprimerEntity(avisWithType.avis);
            reloadAvisData();
        }
    }

    private void sendSms(AvisWithType avisWithType) {
        // Placeholder for SMS sending logic (to be implemented later)
        System.out.println("Envoi d'un SMS pour l'avis ID: " + avisWithType.avis.getId_avis());
    }

    private void reloadAvisData() {
        ObservableList<AvisWithType> updatedAvisList = loadAvisWithTypes();
        filteredAvisList = new FilteredList<>(updatedAvisList, p -> true);
        TreeItem<AvisWithType> root = new RecursiveTreeItem<>(filteredAvisList, RecursiveTreeObject::getChildren);
        userAvisTreeTable.setRoot(root);
        userAvisTreeTable.refresh();
    }
}