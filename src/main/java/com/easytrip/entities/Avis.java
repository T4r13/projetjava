package com.easytrip.entities;

import java.util.Date;

public class Avis  {

    private int id_avis;
    private int id_user;
    private int id_reservation;
    private Date date_avis;
    private String description;
    private int note;

    // Vous pouvez juste ajouter cette méthode dans la classe Avis pour l'affichage
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Avis() {
    }

    public Avis(int id_user, int id_reservation, Date date_avis, String description, int note) {
        this.id_user = id_user;
        this.id_reservation = id_reservation;
        this.date_avis = date_avis;
        this.description = description;
        this.note = note;
    }

    public int getId_avis() {
        return id_avis;
    }

    public int getId_user() {
        return id_user;
    }

    public int getId_reservation() {
        return id_reservation;
    }

    public Date getDate_avis() {
        return date_avis;
    }

    public String getDescription() {
        return description;
    }

    public int getNote() {
        return note;
    }

    public void setId_avis(int id_avis) {
        this.id_avis = id_avis;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }

    public void setDate_avis(Date date_avis) {
        this.date_avis = date_avis;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNote(int note) {
        this.note = note;
    }
}
