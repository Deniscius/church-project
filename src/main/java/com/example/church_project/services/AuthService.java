package com.example.church_project.services;

import com.example.church_project.dao.UserDAO;
import com.example.church_project.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();
    private User currentUser;

    public boolean register(User user) {
        try {
            if (!userDAO.isUsernameAvailable(user.getUsernameUser())) {
                return false;
            }

            // Hachage du mot de passe
            String hashedPassword = BCrypt.hashpw(user.getPasswordUser(), BCrypt.gensalt());
            user.setPasswordUser(hashedPassword);

            // Détecter s'il s'agit du premier utilisateur
            boolean isFirstUser = userDAO.countUsers() == 0;

            // Attribution automatique du rôle
            user.setRoleUser(isFirstUser ? "SUPER_ADMIN" : "USER");

            userDAO.register(user);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur d'inscription : " + e.getMessage());
            return false;
        }
    }

    public boolean login(String username, String password) {
        try {
            User user = userDAO.login(username, password);
            if (user != null) {
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