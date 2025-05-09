package com.easytrip.entities;

public class User {
    private int id_user;
    private String full_name;
    private String user_name;
    private String email_user;
    private String password;
    private String role;
    private String gender;

    public int getId_user() {
        return id_user;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getEmail_user() {
        return email_user;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getGender() {
        return gender;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
