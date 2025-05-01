package com.easytrip.controllers;

import com.easytrip.entities.Avis;
import com.easytrip.services.AvisService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;  // Utilise ceci et supprime 'java.awt.event.ActionEvent'
import java.net.URL;
import java.util.ResourceBundle;

public class ModifierAvisController implements Initializable {
    @FXML private TextField dateField;
    @FXML
    private Slider noteSlider;
    @FXML private Label etoilesLabel;
    @FXML private TextArea descriptionArea;

    private Avis avisToModify;

    public void setAvis(Avis avis) {
        this.avisToModify = avis;

        // Remplir les champs
        dateField.setText(avis.getDate_avis().toString());
        noteSlider.setValue(avis.getNote());
        descriptionArea.setText(avis.getDescription());
        updateStars(avis.getNote());
    }

    @FXML
    private void modifier(ActionEvent event) {
        // Création de l'alerte de confirmation
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Voulez-vous vraiment modifier cet avis ?");
        confirmationAlert.setContentText("Les modifications seront enregistrées.");

        // Attendre la réponse de l'utilisateur
        confirmationAlert.showAndWait().ifPresent(response -> {
            // Si l'utilisateur confirme (OK), procéder à la modification
            if (response == ButtonType.OK) {
                // Mettre à jour l'avis avec les nouvelles valeurs
                avisToModify.setNote((int) noteSlider.getValue());
                avisToModify.setDescription(descriptionArea.getText());

                // Appeler le service pour modifier l'avis dans la base de données
                new AvisService().modifierEntity(avisToModify);

                // Fermer la fenêtre après la modification
                ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
            } else {
                // Si l'utilisateur annule, ne rien faire
                System.out.println("Modification annulée.");
            }
        });
    }


    @FXML
    private void annuler(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        noteSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            updateStars(newVal.intValue());
        });
    }

    private void updateStars(int note) {
        String stars = "⭐".repeat(note);
        etoilesLabel.setText(stars);
    }
}
