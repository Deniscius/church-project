package com.example.church_project.controllers;

import com.example.church_project.HelloApplication;
import com.example.church_project.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AuthController {
    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private void onRegisterButton() throws IOException {
        String name = nameTextField.getText().trim();
        String lastname = lastNameTextField.getText().trim();
        String username = usernameTextField.getText().trim();
        String password = passwordTextField.getText().trim();

        if (!username.isEmpty() && !password.isEmpty() && !name.isEmpty() && !lastname.isEmpty()) {
            User user = new User();
            user.setNomUser(name);
            user.setPrenomUser(lastname);
            user.setUsernameUser(username);
            user.setPasswordUser(password);

            try {
                user.register();
            } catch (SQLException e) {
                e.printStackTrace();
                return;
            }

            Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("login_form.fxml")));
            Stage stage = (Stage) usernameTextField.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
    }



    @FXML
    private void onLoginButton() throws IOException{

    }

    @FXML
    private void onSwitchLogin() throws IOException{
        onClearButton();
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("login_form.fxml")));
        Stage stage = (Stage) usernameTextField.getScene().getWindow(); stage.setScene(new Scene(root));
        onClearButton();
    }

    @FXML
    private  void onSwitchRegister() throws IOException {
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("register_form.fxml")));
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.setScene(new Scene(root));

    }

    @FXML
    private void onClearButton() throws IOException {
        usernameTextField.clear();;
        nameTextField.clear();
        lastNameTextField.clear();
        passwordTextField.clear();

    }
}
