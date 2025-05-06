package com.example.church_project.services;

import com.example.church_project.models.Role;
import com.example.church_project.config.DBConfig;

import java.sql.*;


public class RoleService {
    private final Connection connection;

    public RoleService() {
        try {
            this.connection = DBConfig.getConnection();
            initializeDatabase(); // Création des tables si nécessaire
        } catch (SQLException e) {
            throw new RuntimeException("Erreur d'initialisation du service des rôles", e);
        }
    }
    public void initializeRoles() {
        try {
            for (Role role : Role.values()) {
                if (!roleExists(role)) {
                    createRole(role);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur d'initialisation des rôles : " + e.getMessage());
        }
    }

    private void initializeDatabase() throws SQLException {
        String createRolesTable =
                "CREATE TABLE IF NOT EXISTS roles (" +
                        "roleId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "nameRole TEXT UNIQUE NOT NULL)";

        String createUserRoleTable =
                "CREATE TABLE IF NOT EXISTS user_role (" +
                        "userId INTEGER, " +
                        "roleId INTEGER, " +
                        "PRIMARY KEY (userId, roleId), " +
                        "FOREIGN KEY(userId) REFERENCES User(IdUser), " +
                        "FOREIGN KEY(roleId) REFERENCES roles(idRole))";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createRolesTable);
            stmt.execute(createUserRoleTable);
        }
    }

    private boolean roleExists(Role role) throws SQLException {
        String sql = "SELECT COUNT(*) FROM roles WHERE nameRole = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.name());
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    private void createRole(Role role) throws SQLException {
        String sql = "INSERT INTO roles (nameRole) VALUES (?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role.name());
            stmt.executeUpdate();
        }
    }

    public void assignRoleToUser(int userId, Role role) throws SQLException {
        String sql = "INSERT INTO user_role (userId, roleId) VALUES (?, (SELECT roleId FROM roles WHERE nameRole = ?))";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, role.name());
            stmt.executeUpdate();
        }
    }
}
