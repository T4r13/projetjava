package com.easytrip.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private String url = "jdbc:mysql://localhost:3306/projetdev";
    private String userName = "root";
    private String pwd = "";
    private Connection cnx;
    private static MyConnection instance;

    public MyConnection() {
        try {
            this.cnx = DriverManager.getConnection(this.url, this.userName, this.pwd);
            System.out.println("Connexion établie...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static MyConnection getInstance() {
        if (instance == null) {
            instance = new MyConnection();
        }
        return instance;
    }

    public Connection getCnx() {
        return this.cnx;
    }
}