package com.example.church_project.utils;

import com.example.church_project.HelloApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneUtils {
    public static void loadScene(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(HelloApplication.class.getResource(fxmlPath));
            Stage stage = (Stage) Stage.getWindows().get(0);
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("Erreur de chargement de la scène: " + e.getMessage());
        }
    }
}