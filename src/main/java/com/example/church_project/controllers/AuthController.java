package com.example.church_project.controllers;

import com.example.church_project.services.AuthService;
import com.example.church_project.utils.AlertUtils;
import com.example.church_project.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.example.church_project.models.User;

import static java.lang.Character.isAlphabetic;

public class AuthController {

    @FXML private TextField lastNameTextField;
    @FXML private TextField nameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private TextField usernameTextField;


    private final AuthService authService = new AuthService();

    @FXML
    private void onRegisterButton() {
        String nom = nameTextField.getText().trim();
        String prenom = lastNameTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        // Vérification que tous les champs sont remplis
        if (!validateFields(nom, prenom, username, password)) return;

        // Vérification que nom et prénom ne contiennent que des lettres
        if (!isAlphabetic(nom) || !isAlphabetic(prenom)) {
            AlertUtils.showError("Le nom et le prénom doivent contenir uniquement des lettres.");
            return;
        }

        // Vérification que le mot de passe est fort
        if (!isStrongPassword(password)) {
            AlertUtils.showError("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
            return;
        }

        // Mise en majuscule du nom
        nom = nom.toUpperCase();

        // Création de l'utilisateur
        User user = new User(username, password, nom, prenom);

        // Enregistrement
        if (authService.register(user)) {
            showSuccessAndNavigate("Inscription réussie !", "login_form.fxml");
        } else {
            AlertUtils.showError("Échec de l'inscription - Nom d'utilisateur déjà pris");
        }
    }

    @FXML
    private void onLoginButton() {
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (validateFields(username, password)) {
            if (authService.login(username, password)) {
                showSuccessAndNavigate("Connexion réussie !", "dashboard.fxml");
            } else {
                AlertUtils.showError("Identifiants incorrects");
            }
        }
    }

    @FXML
    private void onSwitchLogin() {
        clearFields();
        SceneUtils.loadScene("login_form.fxml");
    }

    @FXML
    private void onSwitchRegister() {
        SceneUtils.loadScene("register_form.fxml");

    }

    @FXML
    private void onClearButton() {  }

    private boolean validateFields(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                AlertUtils.showError("Tous les champs doivent être remplis.");
                return false;
            }
        }
        return true;
    }

    private void clearFields() {
        usernameTextField.clear();
        nameTextField.clear();
        lastNameTextField.clear();
        passwordTextField.clear();
    }

    private void showSuccessAndNavigate(String message, String fxmlPath) {
        AlertUtils.showInfo(message);
        SceneUtils.loadScene(fxmlPath);
        clearFields();
    }

    private boolean isAlphabetic(String input) {
        return input.matches("^[a-zA-ZÀ-ÿ\\s'-]+$");
    }


    private boolean isStrongPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(regex);
    }
}