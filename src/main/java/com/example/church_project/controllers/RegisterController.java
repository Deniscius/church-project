package com.example.church_project.controllers;

import com.example.church_project.models.User;
import com.example.church_project.services.AuthService;
import com.example.church_project.utils.AlertUtils;
import com.example.church_project.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField lastNameTextField;
    @FXML private TextField nameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private TextField usernameTextField;

    private final AuthService authService = new AuthService();

    @FXML
    private void onRegisterButton() {
        String nom      = nameTextField.getText().trim();
        String prenom   = lastNameTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (!validateFields(nom, prenom, username, password)) return;
        if (!isAlphabetic(nom) || !isAlphabetic(prenom)) {
            AlertUtils.showError("Le nom et le prénom doivent contenir uniquement des lettres.");
            clearFields();                                       // ← on vide après cette erreur
            return;
        }
        if (!isStrongPassword(password)) {
            AlertUtils.showError("Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.");
            clearFields();                                       // ← idem
            return;
        }

        nom = nom.toUpperCase();
        User user = new User(username, password, nom, prenom);

        if (authService.register(user)) {
            AlertUtils.showInfo("Inscription réussie !");
            clearFields();                                       // ← on vide avant le changement de vue
            SceneUtils.loadScene("login_form.fxml");
        } else {
            AlertUtils.showError("Échec de l'inscription - Nom d'utilisateur déjà pris");
            clearFields();                                       // ← on vide aussi après échec
        }
    }

    @FXML
    private void onClearButton () {
        clearFields();
    }

    @FXML
    private void onSwitchLogin() {
        clearFields();
        SceneUtils.loadScene("login_form.fxml");
    }

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
