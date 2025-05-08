package com.example.church_project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class DBConfig {
    private static final Logger logger = LoggerFactory.getLogger(DBConfig.class);
    private static final String DB_URL = "jdbc:sqlite:church_bd";
    private static final String INIT_SCRIPT = "/database/init.sql";
    private static boolean isInitialized = false;

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
        Connection conn = DriverManager.getConnection(DB_URL);
        
        // Initialisation de la base de données
        initializeDatabase(conn);
        
        return conn;
    }

    private static void initializeDatabase(Connection conn) throws SQLException {
        try {
            // Lecture du script d'initialisation
            String initScript = new BufferedReader(
                new InputStreamReader(DBConfig.class.getResourceAsStream(INIT_SCRIPT), StandardCharsets.UTF_8)
            ).lines().collect(Collectors.joining("\n"));

            // Exécution du script
            try (Statement stmt = conn.createStatement()) {
                for (String command : initScript.split(";")) {
                    if (!command.trim().isEmpty()) {
                        stmt.execute(command);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation : " + e.getMessage());
            throw new SQLException("Erreur lors de l'initialisation de la base de données", e);
        }
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