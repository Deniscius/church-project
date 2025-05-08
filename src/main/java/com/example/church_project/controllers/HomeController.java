package com.example.church_project.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import com.example.church_project.utils.AlertUtils;
import java.io.IOException;
import com.example.church_project.services.AuthService;
import com.example.church_project.models.User;
import com.example.church_project.models.Role;
import com.example.church_project.utils.SceneUtils;

public class HomeController {

    @FXML
    private StackPane contentAreatStackPane;

    @FXML
    private Text onClickUserManage;

    @FXML
    private Text titleLabel;

    @FXML
    private Button userManageButton;

    private final AuthService authService = AuthService.getInstance();

    @FXML
    void initialize() {
        User currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            SceneUtils.loadScene("login_form.fxml");
            return;
        }

        // Configurer le titre et les boutons selon le rôle
        configureDashboardForRole(currentUser);

        // Charger le contenu par défaut
        loadContent("home_content.fxml");
    }

    private void configureDashboardForRole(User currentUser) {
        if (authService.isSuperAdmin(currentUser)) {
            titleLabel.setText("Dashboard SuperAdmin");
            userManageButton.setVisible(true);
            onClickUserManage.setText("Gestion des utilisateurs");
        } else if (currentUser.getRole() == Role.ADMIN) {
            titleLabel.setText("Dashboard Admin");
            userManageButton.setVisible(true);
            onClickUserManage.setText("Gestion des utilisateurs");
        } else {
            titleLabel.setText("Dashboard Utilisateur");
            userManageButton.setVisible(false);
        }
    }

    private void loadContent(String fxmlPath) {
        try {
            System.out.println("Tentative de chargement de : " + fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/church_project/" + fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("Le fichier FXML n'a pas été trouvé : " + fxmlPath);
            }
            Parent content = loader.load();
            contentAreatStackPane.getChildren().clear();
            contentAreatStackPane.getChildren().add(content);
        } catch (IOException e) {
            System.err.println("Erreur détaillée lors du chargement de la vue : " + e.getMessage());
            e.printStackTrace();
            AlertUtils.showError("Erreur lors du chargement de la vue : " + e.getMessage());
        }
    }

    @FXML
    void onCategoriesClick(ActionEvent event) {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null && (currentUser.getRole() == Role.ADMIN || authService.isSuperAdmin(currentUser))) {
            loadContent("categories_view.fxml");
        } else {
            AlertUtils.showError("Accès non autorisé");
        }
    }

    @FXML
    void onHelpClick(ActionEvent event) {
        AlertUtils.showInfo("Pour obtenir de l'aide, veuillez contacter l'administrateur système.");
    }

    @FXML
    void onHomeClick(ActionEvent event) {
        loadContent("home_content.fxml");
    }

    @FXML
    void onProductsClick(ActionEvent event) {
        loadContent("products_view.fxml");
    }

    @FXML
    void onSalesClick(ActionEvent event) {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null && (currentUser.getRole() == Role.ADMIN || authService.isSuperAdmin(currentUser))) {
            loadContent("sales_view.fxml");
        } else {
            AlertUtils.showError("Accès non autorisé");
        }
    }

    @FXML
    void onSettingsClick(ActionEvent event) {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null) {
            loadContent("settings_view.fxml");
        }
    }

    @FXML
    void onStatsClick(ActionEvent event) {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null && (currentUser.getRole() == Role.ADMIN || authService.isSuperAdmin(currentUser))) {
            loadContent("stats_view.fxml");
        } else {
            AlertUtils.showError("Accès non autorisé");
        }
    }

    @FXML
    void onUserManageClick(ActionEvent event) {
        User currentUser = authService.getCurrentUser();
        if (currentUser != null && authService.isSuperAdmin(currentUser)) {
            loadContent("super_admin_dashboard.fxml");
        } else {
            AlertUtils.showError("Accès non autorisé - Seuls les SuperAdmin peuvent gérer les utilisateurs");
        }
    }

    @FXML
    void onLogoutClick(ActionEvent event) {
        authService.logout();
        SceneUtils.loadScene("login_form.fxml");
    }
} 