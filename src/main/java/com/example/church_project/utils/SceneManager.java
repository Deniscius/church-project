package com.example.church_project.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestionnaire de scènes pour l'application.
 * Cette classe centralise la gestion de la navigation entre les différentes vues.
 */
public class SceneManager {
    private static final Logger LOGGER = Logger.getLogger(SceneManager.class.getName());
    private static final String BASE_PATH = "/com/example/church_project/";
    private static final String CSS_PATH = BASE_PATH + "styles/main.css";
    
    private static Stage primaryStage;
    
    /**
     * Initialise le gestionnaire de scènes avec la fenêtre principale.
     * @param stage La fenêtre principale de l'application
     */
    public static void initialize(Stage stage) {
        primaryStage = stage;
    }
    
    /**
     * Charge et affiche une nouvelle scène.
     * @param fxmlPath Le chemin vers le fichier FXML
     * @param title Le titre de la fenêtre
     * @return true si le chargement a réussi, false sinon
     */
    public static boolean loadScene(String fxmlPath, String title) {
        try {
            String fullPath = BASE_PATH + fxmlPath;
            LOGGER.info("Tentative de chargement de la scène : " + fullPath);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SceneManager.class.getResource(fullPath));
            
            if (loader.getLocation() == null) {
                LOGGER.severe("Impossible de trouver le fichier FXML : " + fullPath);
                return false;
            }
            
            Scene scene = new Scene(loader.load());
            
            // Application des styles CSS
            String cssUrl = SceneManager.class.getResource(CSS_PATH).toExternalForm();
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl);
            } else {
                LOGGER.warning("Fichier CSS non trouvé : " + CSS_PATH);
            }
            
            // Configuration de la fenêtre
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            
            LOGGER.info("Chargement réussi de la scène : " + fxmlPath);
            return true;
            
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement de la scène : " + fxmlPath, e);
            return false;
        }
    }
    
    /**
     * Charge la scène de connexion.
     * @return true si le chargement a réussi, false sinon
     */
    public static boolean loadLoginScene() {
        return loadScene("login_form.fxml", "Connexion - Gestion Église");
    }
    
    /**
     * Charge le tableau de bord principal.
     * @return true si le chargement a réussi, false sinon
     */
    public static boolean loadDashboardScene() {
        return loadScene("dashboard.fxml", "Tableau de bord - Gestion Église");
    }
    
    /**
     * Charge le tableau de bord administrateur.
     * @return true si le chargement a réussi, false sinon
     */
    public static boolean loadSuperAdminDashboardScene() {
        return loadScene("super_admin_dashboard.fxml", "Administration - Gestion Église");
    }
    
    /**
     * Charge le formulaire d'inscription.
     * @return true si le chargement a réussi, false sinon
     */
    public static boolean loadRegisterScene() {
        return loadScene("register_form.fxml", "Inscription - Gestion Église");
    }
} 