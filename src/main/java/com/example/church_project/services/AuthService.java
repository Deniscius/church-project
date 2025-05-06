package com.example.church_project.services;

import com.example.church_project.dao.UserDAO;
import com.example.church_project.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private User currentUser;

    public boolean userExists(String username) throws SQLException {
        return !userDAO.isUsernameAvailable(username);
    }


    public boolean register(User user) {
        try {
            if (!userDAO.isUsernameAvailable(user.getUsernameUser())) {
                return false;
            }

            // Toujours hasher, pas besoin de vérifier
            String hashedPassword = BCrypt.hashpw(user.getPasswordUser(), BCrypt.gensalt());
            user.setPasswordUser(hashedPassword);

            userDAO.register(user);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur d'inscription : " + e.getMessage());
            return false;
        }
    }

    private boolean isPasswordHashed(String password) {
        // Vérifie le pattern typique d'un hash BCrypt ($2a$..$..)
        return password.startsWith("$2a$")
                || password.startsWith("$2b$")
                || password.startsWith("$2y$");
    }

    public boolean login(String username, String password) {
        try {
            System.out.println("Tentative de login pour : " + username); // 🔍
            User user = userDAO.findByUsername(username);

            if (user == null) {
                System.out.println("Utilisateur non trouvé"); // 🔍
                return false;
            }

            System.out.println("Hash stocké : " + user.getPasswordUser()); // 🔍
            boolean passwordMatch = BCrypt.checkpw(password, user.getPasswordUser());
            System.out.println("Résultat vérification mot de passe : " + passwordMatch); // 🔍

            if (passwordMatch) {
                currentUser = user;
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }



    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}