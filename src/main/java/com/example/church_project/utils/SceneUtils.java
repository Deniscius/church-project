package com.example.church_project.utils;

import com.example.church_project.ChurchApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SceneUtils {
    private static final Logger LOGGER = Logger.getLogger(SceneUtils.class.getName());
    private static final String BASE_PATH = "/com/example/church_project/";

    public static void loadScene(String fxmlPath) {
        try {
            String fullPath = BASE_PATH + fxmlPath;
            LOGGER.info("Tentative de chargement de la scène : " + fullPath);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneUtils.class.getResource(fullPath));
            
            if (loader.getLocation() == null) {
                LOGGER.severe("Impossible de trouver le fichier FXML : " + fullPath);
                throw new RuntimeException("Fichier FXML non trouvé : " + fullPath);
            }
            
            Parent root = loader.load();
            Stage stage = (Stage) Stage.getWindows().get(0);
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            
            LOGGER.info("Chargement réussi de la scène : " + fxmlPath);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur de chargement de la scène : " + e.getMessage(), e);
            throw new RuntimeException("Erreur lors du chargement de la scène : " + e.getMessage(), e);
        }
    }
}