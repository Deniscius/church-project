package com.example.church_project.controllers;

import com.example.church_project.HelloApplication;
import com.example.church_project.dao.UserDAO;
import com.example.church_project.models.User;
import com.example.church_project.utils.AlertUtils;
import com.example.church_project.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class AuthController {

    @FXML private TextField lastNameTextField;
    @FXML private TextField nameTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private TextField usernameTextField;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void onRegisterButton() {
        String name = nameTextField.getText().trim();
        String lastname = lastNameTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (!validateFields(name, lastname, username, password)) {
            return;
        }

        try {
            if (userDAO.isUsernameAvailable(username)) {
                User user = new User(username, password, name, lastname);
                userDAO.register(user);
                showSuccessAndNavigate("Inscription réussie !", "login_form.fxml");
            } else {
                AlertUtils.showError("Ce nom d'utilisateur est déjà pris");
            }
        } catch (SQLException e) {
            handleDatabaseError(e);
        }
    }

    @FXML
    private void onLoginButton() {

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
    private void onClearButton() {
        clearFields();
    }

    private boolean validateFields(String... fields) {
        for (String field : fields) {
            if (field.isEmpty()) {
                AlertUtils.showError("Tous les champs doivent être remplis");
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

    private void handleDatabaseError(SQLException e) {
        System.err.println("Erreur SQL: " + e.getMessage());
        AlertUtils.showError("Erreur de base de données : " + e.getErrorCode());
    }
}