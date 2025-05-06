package com.example.church_project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConfig {
    private static final Logger logger = LoggerFactory.getLogger(DBConfig.class);
    private static final String DATABASE_NAME = "church_bd";
    private static final String JDBC_URL = "jdbc:sqlite:" + DATABASE_NAME;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            logger.info("Driver SQLite chargé avec succès");
        } catch (ClassNotFoundException e) {
            logger.error("Driver SQLite non trouvé", e);
            throw new RuntimeException("Échec du chargement du driver", e);
        }
    }

    private DBConfig() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn.createStatement().execute("SELECT 1");
        } catch (SQLException e) {
            logger.error("Test de connexion échoué", e);
            return false;
        }
    }
}