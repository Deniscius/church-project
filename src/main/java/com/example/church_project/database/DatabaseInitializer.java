package com.example.church_project.database;

import com.example.church_project.models.Role;
import com.example.church_project.models.User;
import com.example.church_project.services.AuthService;
import com.example.church_project.services.RoleService;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class DatabaseInitializer {
    private static final String SUPERADMIN_USERNAME = "superadmin@church";
    private static final String SUPERADMIN_PASSWORD = "TempPass123!";
    private static final String SUPERADMIN_NOM = "SYSTEM";
    private static final String SUPERADMIN_PRENOM = "ADMIN";

    private final AuthService authService;
    private final RoleService roleService;

    public DatabaseInitializer() {
        this.authService = AuthService.getInstance();
        this.roleService = new RoleService();
    }

    public void seedEssentialData() {
        try {
            roleService.initializeRoles();

            if (!authService.userExists(SUPERADMIN_USERNAME)) {
                User superAdmin = new User(
                        SUPERADMIN_USERNAME,
                        SUPERADMIN_PASSWORD,
                        SUPERADMIN_NOM,
                        SUPERADMIN_PRENOM,
                        Role.SUPER_ADMIN);

                if (authService.register(superAdmin)) {
                    System.out.println("Super admin créé avec succès");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'initialisation : " + e.getMessage());
        }
    }
}