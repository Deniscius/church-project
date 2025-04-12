package com.example.church_project.controllers;

import com.example.church_project.services.AuthService;
import com.example.church_project.utils.AlertUtils;
import com.example.church_project.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        // Déclenche le login lorsque l'utilisateur appuie sur "Entrée" dans le champ mot de passe
        passwordTextField.setOnAction(event -> onLoginButton(null));
    }

    @FXML
    public void onLoginButton(ActionEvent event) {
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            AlertUtils.showError("Tous les champs doivent être remplis.");
            return;
        }

        if (authService.login(username, password)) {
            AlertUtils.showInfo("Connexion réussie !");
            SceneUtils.loadScene("home.fxml");
            clearFields();
        } else {
            AlertUtils.showError("Nom d'utilisateur ou mot de passe incorrect.");
        }
    }

    @FXML
    public void onClearButton(ActionEvent event) {
        clearFields();
    }

    @FXML
    public void onSwitchRegister(ActionEvent event) {
        clearFields();
        SceneUtils.loadScene("register_form.fxml");
    }

    private void clearFields() {
        usernameTextField.clear();
        passwordTextField.clear();
    }
}
