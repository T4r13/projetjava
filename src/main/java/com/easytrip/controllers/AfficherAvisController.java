package com.easytrip.controllers;

import com.easytrip.entities.Avis;
import com.easytrip.services.AvisService;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.controls.RecursiveTreeItem;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class AfficherAvisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TreeTableView<AvisWithType> avisTreeTable;

    @FXML
    private TreeTableColumn<AvisWithType, Number> colId;

    @FXML
    private TreeTableColumn<AvisWithType, Number> colNote;

    @FXML
    private TreeTableColumn<AvisWithType, String> colDescription;

    @FXML
    private TreeTableColumn<AvisWithType, String> colDate;

    @FXML
    private TreeTableColumn<AvisWithType, String> colReservation;

    @FXML
    private TreeTableColumn<AvisWithType, String> colAction;

    @FXML
    private TextField searchField;

    private ObservableList<AvisWithType> avisList;
    private FilteredList<AvisWithType> filteredAvisList;

    // Classe interne auxiliaire pour stocker le type de réservation
    public static class AvisWithType extends RecursiveTreeObject<AvisWithType> {
        Avis avis;
        String type;

        AvisWithType(Avis avis, String type) {
            this.avis = avis;
            this.type = type;
        }
    }

    @FXML
    void initialize() {
        // Liens des colonnes avec les propriétés d'AvisWithType
        colId.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().avis.getId_avis()));
        colNote.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getValue().avis.getNote()));
        colDescription.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().avis.getDescription()));
        colDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().avis.getDate_avis().toString()));
        colReservation.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getValue().type));

        // Ajouter un cellFactory pour la colonne d'action
        colAction.setCellFactory(param -> new TreeTableCell<AvisWithType, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                // Si la ligne est vide, on ne montre rien
                if (empty) {
                    setGraphic(null);
                } else {
                    // Créer les boutons
                    Button visualiserButton = new Button("🔍");  // Bouton de visualisation
                    visualiserButton.setOnAction(e -> visualiserAvis(getTreeTableRow().getItem()));

                    Button modifierButton = new Button("✏️");  // Bouton de modification
                    modifierButton.setOnAction(e -> modifierAvis(getTreeTableRow().getItem()));

                    Button supprimerButton = new Button("❌");  // Bouton de suppression
                    supprimerButton.setOnAction(e -> supprimerAvis(getTreeTableRow().getItem()));

                    // Créer un HBox pour aligner les boutons horizontalement
                    HBox hbox = new HBox(10);  // Espace de 10px entre chaque bouton
                    hbox.getChildren().addAll(visualiserButton, modifierButton, supprimerButton);
                    hbox.setStyle("-fx-alignment: center;");  // Centrer les boutons dans la cellule

                    // Mettre les boutons dans la cellule
                    setGraphic(hbox);
                }
            }
        });

        // Charger les données dans le TreeTableView avec filtrage
        avisList = loadAvisWithTypes();
        filteredAvisList = new FilteredList<>(avisList, p -> true);

        // Configurer l'écouteur pour la barre de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredAvisList.setPredicate(avisWithType -> {
                if (newValue == null || newValue.trim().isEmpty()) {
                    return true; // Afficher tous les avis si la recherche est vide
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Rechercher dans la description, le type, la note et la date avec vérifications null
                String description = avisWithType.avis.getDescription() != null ? avisWithType.avis.getDescription() : "";
                String type = avisWithType.type != null ? avisWithType.type : "";
                //String note = String.valueOf(avisWithType.avis.getNote());
                String date = avisWithType.avis.getDate_avis() != null ? avisWithType.avis.getDate_avis().toString() : "";
                return description.toLowerCase().contains(lowerCaseFilter)
                        || type.toLowerCase().contains(lowerCaseFilter)
                        /*|| note.contains(lowerCaseFilter)*/
                        || date.toLowerCase().contains(lowerCaseFilter);
            });
            avisTreeTable.refresh(); // Forcer le rafraîchissement de l'interface
        });

        // Configurer le TreeTableView avec les données filtrées
        TreeItem<AvisWithType> root = new RecursiveTreeItem<>(filteredAvisList, RecursiveTreeObject::getChildren);
        avisTreeTable.setRoot(root);
        avisTreeTable.setShowRoot(false); // Pour ne pas afficher la racine dans le TreeTableView
    }

    // Méthode pour charger les avis avec leur type depuis la base de données
    private ObservableList<AvisWithType> loadAvisWithTypes() {
        ObservableList<AvisWithType> list = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/projetdev", "root", "")) {
            String query = """
                SELECT a.*, r.id_service, r.id_hotel, r.id_vol
                FROM avis a
                LEFT JOIN reservations r ON a.id_reservation = r.id_reservation
            """;
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            // Parcours des résultats de la base de données
            while (rs.next()) {
                // Création de l'objet Avis et initialisation de ses propriétés
                Avis avis = new Avis();
                avis.setId_avis(rs.getInt("id_avis"));
                avis.setId_user(rs.getInt("id_user"));
                avis.setId_reservation(rs.getInt("id_reservation"));
                avis.setDate_avis(rs.getDate("date_avis"));
                avis.setDescription(rs.getString("description"));
                avis.setNote(rs.getInt("note"));

                // Construction du type en fonction des informations de la réservation
                StringBuilder type = new StringBuilder();
                if (rs.getInt("id_service") != 0) type.append("Service ");
                if (rs.getInt("id_hotel") != 0) type.append("Hôtel ");
                if (rs.getInt("id_vol") != 0) type.append("Vol ");

                // Définir un type par défaut si aucun type spécifique n'est trouvé
                String finalType = type.toString().trim();
                if (finalType.isEmpty()) finalType = "Aucun";

                // Ajouter l'objet AvisWithType dans la liste
                list.add(new AvisWithType(avis, finalType));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer l'exception SQL
        }

        return list;
    }

    private void visualiserAvis(AvisWithType avisWithType) {
        Avis avis = avisWithType.avis;
        String type = avisWithType.type; // récupérer dynamiquement le type
        openVisualiserAvisWindow(avis, type); // appel avec les 2 paramètres
    }

    private void openVisualiserAvisWindow(Avis avis, String type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/VisualiserAvis.fxml"));
            Parent root = loader.load();

            // Passer l'objet Avis au contrôleur
            VisualiserAvisController controller = loader.getController();
            controller.setAvis(avis);
            controller.setType(type); // <--- on passe dynamiquement le type ici

            Stage stage = new Stage();
            stage.setTitle("Détails de l'Avis");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void modifierAvis(AvisWithType avisWithType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierAvis.fxml"));
            Parent root = loader.load();

            ModifierAvisController controller = loader.getController();
            controller.setAvis(avisWithType.avis);

            Stage stage = new Stage();
            stage.setTitle("Modifier un Avis");
            stage.setScene(new Scene(root));
            stage.show();

            // Fermer la fenêtre actuelle si vous le souhaitez
            stage.setOnHiding(event -> {
                // Recharger les avis après la modification
                reloadAvisData();
            });

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
            // Appel à ta méthode dans le service
            new AvisService().supprimerEntity(avisWithType.avis);

            // Supprimer l'avis de la vue
            avisTreeTable.getRoot().getChildren().removeIf(
                    item -> item.getValue().avis.getId_avis() == avisWithType.avis.getId_avis()
            );
        }
    }

    private void reloadAvisData() {
        // Recharger les données depuis la base de données
        ObservableList<AvisWithType> updatedAvisList = loadAvisWithTypes();
        avisList.clear(); // Vider la liste existante
        avisList.addAll(updatedAvisList); // Mettre à jour avec les nouvelles données
        avisTreeTable.refresh(); // Rafraîchir la vue pour s'assurer que l'affichage est mis à jour
    }
}