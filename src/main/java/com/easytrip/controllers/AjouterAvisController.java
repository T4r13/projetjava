package com.easytrip.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.easytrip.entities.Avis;
import com.easytrip.services.AvisService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;

public class AjouterAvisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField dateField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private Label etoilesLabel;

    @FXML
    private Slider noteSlider;

    @FXML
    void ajouter(ActionEvent event) {
        try {
            // ID statiques temporairement
            int id_user = 1;
            int id_reservation = 1;

            // Récupération des valeurs
            LocalDate dateAvis = LocalDate.parse(dateField.getText());
            int note = (int) noteSlider.getValue();
            String description = descriptionArea.getText();

            // Création de l’objet Avis
            Avis avis = new Avis(id_user, id_reservation, Date.valueOf(dateAvis), description, note);

            // Insertion dans la base
            AvisService avisService = new AvisService();
            avisService.ajouterEntity(avis);

            // Affichage du message de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Avis ajouté avec succès !");
            alert.showAndWait();  // Attendre la fermeture

            // Redirection vers afficherAvis.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAvis.fxml"));
            Parent root = loader.load();
            dateField.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de navigation");
            alert.setHeaderText("Impossible de charger la page d'affichage des avis.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ajout");
            alert.setContentText("Veuillez vérifier vos données.");
            alert.showAndWait();
        }
    }

    @FXML
    void annuler(ActionEvent event) {

    }

    @FXML
    void initialize() {
        // Affichage dynamique des étoiles
        noteSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            int val = newVal.intValue();
            etoilesLabel.setText("⭐".repeat(val));
        });

        // Initialiser la date à aujourd’hui
        dateField.setText(LocalDate.now().toString());
        dateField.setEditable(false);
    }

}
