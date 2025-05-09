package com.easytrip.services;

import com.easytrip.entities.Avis;
import com.easytrip.interfaces.IService;
import com.easytrip.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvisService implements IService<Avis> {



    @Override
    public void ajouterEntity(Avis avis) {
        try {
            String req = "INSERT INTO avis (id_user, id_reservation, date_avis, description, note) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setInt(1, avis.getId_user());
            pst.setInt(2, avis.getId_reservation());
            pst.setDate(3, new java.sql.Date(avis.getDate_avis().getTime()));
            pst.setString(4, avis.getDescription());
            pst.setInt(5, avis.getNote());

            pst.executeUpdate();
            System.out.println("✅ Avis ajouté.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur ajout avis : " + e.getMessage());
        }
    }

    @Override
    public void supprimerEntity(Avis avis) {
        try {
            String req = "DELETE FROM avis WHERE id_avis = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setInt(1, avis.getId_avis());
            pst.executeUpdate();
            System.out.println("🗑️ Avis supprimé.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur suppression avis : " + e.getMessage());
        }
    }

    @Override
    public void modifierEntity(Avis avis) {
        try {
            String req = "UPDATE avis SET id_user = ?, id_reservation = ?, date_avis = ?, description = ?, note = ? WHERE id_avis = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            pst.setInt(1, avis.getId_user());
            pst.setInt(2, avis.getId_reservation());
            pst.setDate(3, new java.sql.Date(avis.getDate_avis().getTime()));
            pst.setString(4, avis.getDescription());
            pst.setInt(5, avis.getNote());
            pst.setInt(6, avis.getId_avis());

            pst.executeUpdate();
            System.out.println("🔁 Avis modifié.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur modification avis : " + e.getMessage());
        }
    }

    @Override
    public List<Avis> getAllData() {
        List<Avis> avisList = new ArrayList<>();
        try {
            String req = "SELECT * FROM avis";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                Avis a = new Avis();
                a.setId_avis(rs.getInt("id_avis"));
                a.setId_user(rs.getInt("id_user"));
                a.setId_reservation(rs.getInt("id_reservation"));
                a.setDate_avis(rs.getDate("date_avis"));
                a.setDescription(rs.getString("description"));
                a.setNote(rs.getInt("note"));

                avisList.add(a);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lecture avis : " + e.getMessage());
        }
        return avisList;
    }


    public List<Avis> getAllAvisWithUserInfo() {
        List<Avis> avisList = new ArrayList<>();
        try {
            String req = """
        SELECT a.*, u.full_name, u.email_user, u.user_name
        FROM avis a
        JOIN user u ON a.id_user = u.id_user
        """;

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(req);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Avis a = new Avis();
                a.setId_avis(rs.getInt("id_avis"));
                a.setId_user(rs.getInt("id_user"));
                a.setId_reservation(rs.getInt("id_reservation"));
                a.setDate_avis(rs.getDate("date_avis"));
                a.setDescription(rs.getString("description"));
                a.setNote(rs.getInt("note"));

                a.setNomUtilisateur(rs.getString("full_name"));
                a.setPrenomUtilisateur(rs.getString("user_name")); // ← correspond au champ disponible dans `user`
                a.setEmailUtilisateur(rs.getString("email_user"));

                avisList.add(a);
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur jointure avis-utilisateur : " + e.getMessage());
        }
        return avisList;
    }



}
