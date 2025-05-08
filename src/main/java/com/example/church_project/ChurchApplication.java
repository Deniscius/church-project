package com.example.church_project;

import com.example.church_project.utils.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application principale de gestion d'église.
 * Cette classe gère le démarrage de l'application et la configuration de la fenêtre principale.
 */
public class ChurchApplication extends Application {
    private static final Logger LOGGER = Logger.getLogger(ChurchApplication.class.getName());
    private static final String APP_TITLE = "Gestion Église";
    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 768;
    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 800;

    @Override
    public void start(Stage stage) {
        try {
            configureStage(stage);
            SceneManager.initialize(stage);
            
            if (!SceneManager.loadLoginScene()) {
                throw new RuntimeException("Impossible de charger la scène de connexion");
            }
            
            stage.show();
            LOGGER.info("Application démarrée avec succès");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du démarrage de l'application", e);
            System.exit(1);
        }
    }

    /**
     * Configure les propriétés de base de la fenêtre principale.
     * @param stage La fenêtre à configurer
     */
    private void configureStage(Stage stage) {
        stage.setTitle(APP_TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setWidth(DEFAULT_WIDTH);
        stage.setHeight(DEFAULT_HEIGHT);
        stage.setResizable(true);
        stage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 