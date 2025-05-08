package com.example.church_project.dialogs;

import com.example.church_project.models.User;
import com.example.church_project.models.Role;
import com.example.church_project.utils.AlertUtils;
import com.example.church_project.services.AuthService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class AddUserDialog {
    private static final AuthService authService = AuthService.getInstance();

    public static Optional<User> show() {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un utilisateur");
        dialog.setHeaderText("Entrez les informations de l'utilisateur");

        // Créer les champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        TextField nomField = new TextField();
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField();
        prenomField.setPromptText("Prénom");
        ComboBox<Role> roleComboBox = new ComboBox<>();
        
        // Définir les rôles disponibles selon l'utilisateur connecté
        User currentUser = authService.getCurrentUser();
        if (authService.isSuperAdmin(currentUser)) {
            roleComboBox.getItems().addAll(Role.values());
        } else if (currentUser.getRole() == Role.ADMIN) {
            roleComboBox.getItems().add(Role.USER);
            roleComboBox.setValue(Role.USER); // Définir USER comme valeur par défaut
        }
        roleComboBox.setPromptText("Sélectionnez un rôle");

        grid.add(new Label("Nom d'utilisateur:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Mot de passe:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Nom:"), 0, 2);
        grid.add(nomField, 1, 2);
        grid.add(new Label("Prénom:"), 0, 3);
        grid.add(prenomField, 1, 3);
        grid.add(new Label("Rôle:"), 0, 4);
        grid.add(roleComboBox, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Ajouter les boutons
        ButtonType addButtonType = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Convertir le résultat en objet User
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty() ||
                    nomField.getText().isEmpty() || prenomField.getText().isEmpty() ||
                    roleComboBox.getValue() == null) {
                    AlertUtils.showError("Tous les champs sont obligatoires");
                    return null;
                }
                return new User(
                    usernameField.getText(),
                    passwordField.getText(),
                    nomField.getText(),
                    prenomField.getText(),
                    roleComboBox.getValue()
                );
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 