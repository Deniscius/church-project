package com.example.church_project.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import com.example.church_project.utils.SceneUtils;

public class HomeController {
    @FXML private StackPane contentAreatStackPane;
    @FXML private Text onClickUserManage;

    @FXML
    private void initialize() {
        // Initialisation du tableau de bord
        System.out.println("Tableau de bord initialisé");
    }

    @FXML
    private void onHomeClick() {
        // Action pour le bouton Home
        System.out.println("Accueil cliqué");
    }

    @FXML
    private void onProductsClick() {
        // Action pour le bouton Liste des Produits
        System.out.println("Liste des produits cliqué");
    }

    @FXML
    private void onCategoriesClick() {
        // Action pour le bouton Liste des catégories
        System.out.println("Liste des catégories cliqué");
    }

    @FXML
    private void onSalesClick() {
        // Action pour le bouton Liste des ventes
        System.out.println("Liste des ventes cliqué");
    }

    @FXML
    private void onStatsClick() {
        // Action pour le bouton Statistiques
        System.out.println("Statistiques cliqué");
    }

    @FXML
    private void onSettingsClick() {
        // Action pour le bouton Paramètres
        System.out.println("Paramètres cliqué");
    }

    @FXML
    private void onUserManageClick() {
        // Action pour le bouton Gestion des utilisateurs
        System.out.println("Gestion des utilisateurs cliqué");
    }

    @FXML
    private void onHelpClick() {
        // Action pour le bouton Aide
        System.out.println("Aide cliqué");
    }
} 