package com.easytrip.tests;
//import com.easytrip.tools.MyConnection;
import com.easytrip.entities.Avis;
import com.easytrip.services.AvisService;
import java.util.List;

import java.util.Date;


public class MainConnexionTest {


    public static void main(String[] args) {
        /*System.out.println("Testing database connection...");
        MyConnection.getInstance();*/

        AvisService avisService = new AvisService();

        // * Ajout
        Avis avis1 = new Avis();
        avis1.setId_user(1); // Remplacer par un ID valide
        avis1.setId_reservation(1); // Remplacer par un ID valide
        avis1.setDate_avis(new Date());
        avis1.setDescription("Très bon voyage, je recommande !");
        avis1.setNote(5);

        avisService.ajouterEntity(avis1);

        // * Affichage
        List<Avis> liste = avisService.getAllData();
        System.out.println("📋 Liste des avis :");
        for (Avis a : liste) {
            System.out.println("ID: " + a.getId_avis() + " - Note: " + a.getNote() + " - Description: " + a.getDescription());
        }

        // * Modifier le dernier avis
        if (!liste.isEmpty()) {
            Avis dernierAvis = liste.get(liste.size() - 1);
            dernierAvis.setDescription("Avis modifié après test.");
            dernierAvis.setNote(3);
            avisService.modifierEntity(dernierAvis);
        }

        // * Supprimer le dernier avis
        if (!liste.isEmpty()) {
            Avis avisASupprimer = liste.get(liste.size() - 1);
            avisService.supprimerEntity(avisASupprimer);
        }
    }
}



