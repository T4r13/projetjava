package com.easytrip.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

public class AjouterAvisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn_ajouter;

    @FXML
    private Button btn_annuler;

    @FXML
    private DatePicker id_date;

    @FXML
    private TextArea id_description;

    @FXML
    private ComboBox<?> id_reservation;

    @FXML
    private RadioButton note_1;

    @FXML
    private RadioButton note_2;

    @FXML
    private RadioButton note_3;

    @FXML
    private RadioButton note_4;

    @FXML
    private RadioButton note_5;

    @FXML
    void ajouter(ActionEvent event) {

    }

    @FXML
    void annulerAjout(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btn_ajouter != null : "fx:id=\"btn_ajouter\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert btn_annuler != null : "fx:id=\"btn_annuler\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert id_date != null : "fx:id=\"id_date\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert id_description != null : "fx:id=\"id_description\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert id_reservation != null : "fx:id=\"id_reservation\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert note_1 != null : "fx:id=\"note_1\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert note_2 != null : "fx:id=\"note_2\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert note_3 != null : "fx:id=\"note_3\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert note_4 != null : "fx:id=\"note_4\" was not injected: check your FXML file 'AjouterAvis.fxml'.";
        assert note_5 != null : "fx:id=\"note_5\" was not injected: check your FXML file 'AjouterAvis.fxml'.";

    }

}
