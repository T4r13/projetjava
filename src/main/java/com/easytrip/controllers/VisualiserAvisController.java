package com.easytrip.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.easytrip.entities.Avis;

public class VisualiserAvisController {

    @FXML
    private Label lblId;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblReservation; // Nouveau label pour le type à la place de lblReservation
    @FXML
    private Label lblDate;
    @FXML
    private Label lblNote;
    @FXML
    private TextArea txtDescription;

    private Avis avis; // L'avis à afficher

    // Méthode pour initialiser les données
    public void setAvis(Avis avis) {
        this.avis = avis;
        lblId.setText(String.valueOf(avis.getId_avis()));
        lblUser.setText(String.valueOf(avis.getId_user()));
        lblDate.setText(avis.getDate_avis().toString());
        lblNote.setText(String.valueOf(avis.getNote()));
        txtDescription.setText(avis.getDescription());
    }

    // Méthode pour définir le type final (par exemple : "Hôtel", "Circuit", etc.)
    public void setType(String type) {
        lblReservation.setText(type);
    }



    @FXML
    private void handleClose() {
        Stage stage = (Stage) txtDescription.getScene().getWindow();
        stage.close();
    }
}
