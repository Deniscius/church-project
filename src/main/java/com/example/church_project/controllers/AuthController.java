package com.example.church_project.controllers;

import com.example.church_project.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AuthController {
    @FXML
    private Button buttonClear;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Button loginButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private void onClearButton() throws IOException {
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("register_form.fxml")));
        Stage stage = (Stage) usernameTextField.getScene().getWindow();
        stage.setScene(new Scene(root));

    }

    @FXML
    private void onLoginButton(ActionEvent event) {

    }

    @FXML
    private void onLoginForm(ActionEvent event) {
    }

}
