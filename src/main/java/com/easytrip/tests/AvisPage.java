package com.easytrip.tests;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AvisPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) {
        try {
            //Parent root = FXMLLoader.load(getClass().getResource("/AjouterAvis.fxml"));
            Parent root = FXMLLoader.load(getClass().getResource("/VisualisertoutlesAvis.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();



        } catch (IOException e) {
            System.out.println(e.getMessage());
            }
        }


/*  controle saisie 3al decription
    metier : bad words fe desciption
    api : notification par sms
    nkhamem na3ml tree par la note
 */



}


