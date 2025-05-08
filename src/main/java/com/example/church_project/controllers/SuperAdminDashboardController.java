package com.example.church_project.controllers;

import com.example.church_project.models.User;
import com.example.church_project.models.Role;
import com.example.church_project.services.AuthService;
import com.example.church_project.utils.AlertUtils;
import com.example.church_project.utils.SceneUtils;
import com.example.church_project.dialogs.AddUserDialog;
import com.example.church_project.dialogs.EditUserDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.application.Platform;
import java.util.Optional;
import java.util.List;

public class SuperAdminDashboardController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, Boolean> colState;
    @FXML private Label titleLabel;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button logoutButton;
    @FXML private Button toggleStateButton;

    private static final AuthService authService = AuthService.getInstance();
    private final ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        User currentUser = authService.getCurrentUser();
        if (!authService.canAccessUserManagement(currentUser)) {
            AlertUtils.showError("Accès non autorisé");
            return;
        }

        // Configuration du titre selon le rôle
        if (authService.isSuperAdmin(currentUser)) {
            titleLabel.setText("Dashboard SuperAdmin : Gestion des Utilisateurs");
            addButton.setVisible(true);
            editButton.setVisible(true);
            deleteButton.setVisible(true);
            toggleStateButton.setVisible(true);
            colState.setVisible(true);
        } else if (currentUser.getRole() == Role.ADMIN) {
            titleLabel.setText("Dashboard Admin : Gestion des Utilisateurs");
            addButton.setVisible(true);
            editButton.setVisible(true);
            deleteButton.setVisible(true);
            toggleStateButton.setVisible(false);
            colState.setVisible(false);
        } else {
            titleLabel.setText("Dashboard : Liste des Utilisateurs");
            addButton.setVisible(false);
            editButton.setVisible(false);
            deleteButton.setVisible(false);
            toggleStateButton.setVisible(false);
            colState.setVisible(false);
        }

        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("usernameUser"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomUser"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomUser"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        
        // Personnalisation de l'affichage de l'état
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));
        colState.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean state, boolean empty) {
                super.updateItem(state, empty);
                if (empty || state == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(state ? "Actif" : "Inactif");
                    setStyle(state ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                }
            }
        });

        // Lier la liste à la table
        usersTable.setItems(usersList);

        // Charger les données
        refreshUsersList();
    }

    @FXML
    void handleAddUser() {
        User currentUser = authService.getCurrentUser();
        if (!authService.canAccessUserManagement(currentUser)) {
            AlertUtils.showError("Vous n'avez pas les droits pour ajouter des utilisateurs");
            return;
        }

        AddUserDialog.show().ifPresent(user -> {
            if (authService.register(user)) {
                AlertUtils.showInfo("Utilisateur ajouté avec succès");
                Platform.runLater(this::refreshUsersList);
            } else {
                AlertUtils.showError("Erreur lors de l'ajout de l'utilisateur");
            }
        });
    }

    @FXML
    void handleEditUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showError("Veuillez sélectionner un utilisateur à modifier");
            return;
        }

        User currentUser = authService.getCurrentUser();
        if (!authService.canAccessUserManagement(currentUser)) {
            AlertUtils.showError("Vous n'avez pas les droits pour modifier des utilisateurs");
            return;
        }

        // Vérifier si l'admin essaie de se modifier lui-même
        if (currentUser.getRole() == Role.ADMIN && selectedUser.getIdUser() == currentUser.getIdUser()) {
            AlertUtils.showError("Un administrateur ne peut pas se modifier lui-même");
            return;
        }

        // Ouvrir la boîte de dialogue de modification
        EditUserDialog.show(selectedUser).ifPresent(updatedUser -> {
            if (authService.updateUser(updatedUser)) {
                AlertUtils.showInfo("Utilisateur modifié avec succès");
                Platform.runLater(this::refreshUsersList);
            } else {
                AlertUtils.showError("Erreur lors de la modification de l'utilisateur");
            }
        });
    }

    @FXML
    void handleDeleteUser() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showError("Veuillez sélectionner un utilisateur à supprimer");
            return;
        }

        User currentUser = authService.getCurrentUser();
        
        // Vérifier si l'admin essaie de se supprimer lui-même
        if (currentUser.getRole() == Role.ADMIN && selectedUser.getIdUser() == currentUser.getIdUser()) {
            AlertUtils.showError("Un administrateur ne peut pas se supprimer lui-même");
            return;
        }

        if (!authService.canDeleteUser(currentUser, selectedUser)) {
            AlertUtils.showError("Vous n'avez pas les droits pour supprimer cet utilisateur");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer l'utilisateur");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer l'utilisateur " + selectedUser.getUsernameUser() + " ?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (authService.logicalDeleteUser(selectedUser.getIdUser())) {
                AlertUtils.showInfo("Utilisateur supprimé avec succès");
                Platform.runLater(this::refreshUsersList);
            } else {
                AlertUtils.showError("Erreur lors de la suppression de l'utilisateur");
            }
        }
    }

    @FXML
    void handleToggleState() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            AlertUtils.showError("Veuillez sélectionner un utilisateur");
            return;
        }

        User currentUser = authService.getCurrentUser();
        if (!authService.isSuperAdmin(currentUser)) {
            AlertUtils.showError("Seul le SuperAdmin peut changer l'état des utilisateurs");
            return;
        }

        // Vérifier si on essaie de désactiver le dernier SuperAdmin
        if (!selectedUser.isState() && selectedUser.getRole() == Role.SUPER_ADMIN) {
            AlertUtils.showError("Impossible de désactiver le dernier SuperAdmin actif");
            return;
        }

        // Vérifier si on essaie de désactiver son propre compte
        if (!selectedUser.isState() && selectedUser.getIdUser() == currentUser.getIdUser()) {
            AlertUtils.showError("Vous ne pouvez pas désactiver votre propre compte");
            return;
        }

        String action = selectedUser.isState() ? "désactiver" : "activer";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de changement d'état");
        alert.setHeaderText("Changer l'état de l'utilisateur");
        alert.setContentText("Êtes-vous sûr de vouloir " + action + " l'utilisateur " + selectedUser.getUsernameUser() + " ?\n" +
                           (selectedUser.isState() ? "L'utilisateur ne pourra plus se connecter." : "L'utilisateur pourra à nouveau se connecter."));

        if (alert.showAndWait().get() == ButtonType.OK) {
            if (authService.updateUserState(selectedUser.getIdUser(), !selectedUser.isState())) {
                AlertUtils.showInfo("État de l'utilisateur modifié avec succès");
                
                // Si on a désactivé notre propre compte, se déconnecter
                if (!selectedUser.isState() && selectedUser.getIdUser() == currentUser.getIdUser()) {
                    authService.logout();
                    SceneUtils.loadScene("login_form.fxml");
                } else {
                    // Sinon, actualiser la liste
                    Platform.runLater(this::refreshUsersList);
                }
            } else {
                AlertUtils.showError("Erreur lors de la modification de l'état de l'utilisateur");
            }
        }
    }

    @FXML
    void handleRefresh() {
        refreshUsersList();
    }

    @FXML
    void handleLogout() {
        authService.logout();
        SceneUtils.loadScene("login_form.fxml");
    }

    private void refreshUsersList() {
        usersList.clear();
        User currentUser = authService.getCurrentUser();
        List<User> visibleUsers = authService.getVisibleUsers(currentUser);
        usersList.addAll(visibleUsers);
        usersTable.refresh();
    }
}