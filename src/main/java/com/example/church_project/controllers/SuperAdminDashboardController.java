package com.example.church_project.controllers;

import com.example.church_project.dao.UserDAO;
import com.example.church_project.models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

public class SuperAdminDashboardController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Number> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colRole;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        // Lier les colonnes avec les propriétés
        colId.setCellValueFactory(data -> data.getValue().idUserProperty());
        colUsername.setCellValueFactory(data -> data.getValue().usernameUserProperty());
        colNom.setCellValueFactory(data -> data.getValue().nomUserProperty());
        colPrenom.setCellValueFactory(data -> data.getValue().prenomUserProperty());
        colRole.setCellValueFactory(data -> data.getValue().roleUserProperty());

        // Charger les données
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<User> userList = userDAO.findAll();
            ObservableList<User> observableUserList = FXCollections.observableArrayList(userList);
            usersTable.setItems(observableUserList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les utilisateurs.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Tu pourras ensuite ajouter les méthodes suivantes :
    public void handleAddUser() {}
    public void handleEditUser() {}
    public void handleDeleteUser() {}
    public void handleRefresh() {
        loadUsers(); // Recharge les utilisateurs
    }
}
