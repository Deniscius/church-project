package com.example.church_project.dao;

import com.example.church_project.config.DBConfig;
import com.example.church_project.models.Role;
import com.example.church_project.models.User;

import java.sql.*;

public class UserDAO {

    // Méthode d'inscription : on suppose que 'user.getPasswordUser()' est déjà un hash BCrypt
    public void register(User user) throws SQLException {
        String sql = "INSERT INTO User (nomUser, prenomUser, usernameUser, passwordUser, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getNomUser());
            ps.setString(2, user.getPrenomUser());
            ps.setString(3, user.getUsernameUser());
            // On stocke directement le hash (déjà fait dans AuthService)
            ps.setString(4, user.getPasswordUser());
            ps.setString(5, user.getRole().name());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setIdUser(generatedKeys.getInt(1));
                        // Log pour debug (retirer en production)
                        System.out.println("Mot de passe final avant insertion : " + user.getPasswordUser());
                    }
                }
            }
        }
    }

    // Méthode de connexion retournant l'utilisateur complet si mot de passe OK
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM User WHERE usernameUser = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("passwordUser");
                    if (org.mindrot.jbcrypt.BCrypt.checkpw(password, storedHash)) {
                        return extractUserFromResultSet(rs);
                    }
                }
                return null;
            }
        }
    }

    // Vérifie la disponibilité du nom d'utilisateur
    public boolean isUsernameAvailable(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE usernameUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0;
            }
        }
    }

    // Récupère l'utilisateur sans vérifier le mot de passe
    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM User WHERE usernameUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
                return null;
            }
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("IdUser"),
                rs.getString("usernameUser"),
                rs.getString("passwordUser"),
                rs.getString("nomUser"),
                rs.getString("prenomUser"),
                Role.valueOf(rs.getString("role"))
        );
    }
}
