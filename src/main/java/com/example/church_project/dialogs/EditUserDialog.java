package com.example.church_project.dialogs;

import com.example.church_project.models.User;
import com.example.church_project.models.Role;
import com.example.church_project.utils.AlertUtils;
import com.example.church_project.services.AuthService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;

public class EditUserDialog {
    private static final AuthService authService = AuthService.getInstance();

    public static Optional<User> show(User userToEdit) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Modifier un utilisateur");
        dialog.setHeaderText("Modifiez les informations de l'utilisateur");

        // Créer les champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField(userToEdit.getUsernameUser());
        usernameField.setPromptText("Nom d'utilisateur");
        TextField nomField = new TextField(userToEdit.getNomUser());
        nomField.setPromptText("Nom");
        TextField prenomField = new TextField(userToEdit.getPrenomUser());
        prenomField.setPromptText("Prénom");
        ComboBox<Role> roleComboBox = new ComboBox<>();
        
        // Définir les rôles disponibles selon l'utilisateur connecté
        User currentUser = authService.getCurrentUser();
        if (authService.isSuperAdmin(currentUser)) {
            roleComboBox.getItems().addAll(Role.values());
            roleComboBox.setValue(userToEdit.getRole());
        } else if (currentUser.getRole() == Role.ADMIN) {
            roleComboBox.getItems().add(Role.USER);
            roleComboBox.setValue(Role.USER);
            roleComboBox.setDisable(true); // L'admin ne peut pas changer le rôle
        }
        roleComboBox.setPromptText("Sélectionnez un rôle");

        grid.add(new Label("Nom d'utilisateur:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Prénom:"), 0, 2);
        grid.add(prenomField, 1, 2);
        grid.add(new Label("Rôle:"), 0, 3);
        grid.add(roleComboBox, 1, 3);

        dialog.getDialogPane().setContent(grid);

        // Ajouter les boutons
        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Convertir le résultat en objet User
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (usernameField.getText().isEmpty() || 
                    nomField.getText().isEmpty() || 
                    prenomField.getText().isEmpty() ||
                    roleComboBox.getValue() == null) {
                    AlertUtils.showError("Tous les champs sont obligatoires");
                    return null;
                }

                // Créer un nouvel utilisateur avec les informations modifiées
                User updatedUser = new User(
                    userToEdit.getIdUser(),
                    usernameField.getText(),
                    userToEdit.getPasswordUser(), // Garder le même mot de passe
                    nomField.getText(),
                    prenomField.getText(),
                    roleComboBox.getValue(),
                    userToEdit.isState() // Garder le même état
                );
                return updatedUser;
            }
            return null;
        });

        return dialog.showAndWait();
    }
} 