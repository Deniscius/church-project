package com.example.church_project.dbConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public interface IDBConfig {
    static String database = "church_bd.db";
    static String URL = "jdbc:sqlite:" + database;

    public static Connection getConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}