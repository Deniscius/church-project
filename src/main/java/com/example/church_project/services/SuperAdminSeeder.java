package com.example.church_project.services;

import com.example.church_project.dao.UserDAO;
import com.example.church_project.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class SuperAdminSeeder {

    private static final String SUPER_ADMIN_USERNAME = "devadmin";
    private static final String SUPER_ADMIN_PASSWORD = "MonMotDePasseFort@2024";
    private static final String NOM = "ADMIN";
    private static final String PRENOM = "Super";

    public static void seedSuperAdmin() {
        UserDAO userDAO = new UserDAO();
        try {
            if (userDAO.findByUsername(SUPER_ADMIN_USERNAME) == null) {
                String hashedPassword = BCrypt.hashpw(SUPER_ADMIN_PASSWORD, BCrypt.gensalt());
                User superAdmin = new User( SUPER_ADMIN_USERNAME, hashedPassword, NOM, PRENOM, "SUPER_ADMIN");
                userDAO.register(superAdmin);
                System.out.println("Super admin créé avec succès !");
            } else {
                System.out.println("Super admin déjà existant.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation du super admin : " + e.getMessage());
        }
    }
}
