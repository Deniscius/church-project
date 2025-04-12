package com.example.church_project.controllers;
import com.example.church_project.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import com.example.church_project.HelloApplication;


import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private StackPane contentAreatStackPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("home.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        contentAreatStackPane.getChildren().removeAll();
        contentAreatStackPane.getChildren().setAll(root);
    }
    @FXML
    private void onBackToHome() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("home.fxml")));
        contentAreatStackPane.getChildren().removeAll();
        contentAreatStackPane.getChildren().setAll(root);
    }
    @FXML
    private void onReadCategories() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("categories.fxml")));
        contentAreatStackPane.getChildren().removeAll();
        contentAreatStackPane.getChildren().setAll(root);
    }
    @FXML
    private void onReadProducts() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("products.fxml")));
        contentAreatStackPane.getChildren().removeAll();
        contentAreatStackPane.getChildren().setAll(root);
    }
}

