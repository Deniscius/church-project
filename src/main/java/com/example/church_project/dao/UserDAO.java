package com.example.church_project.dao;

import com.example.church_project.config.DBConfig;
import com.example.church_project.models.Role;
import com.example.church_project.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Méthode d'inscription : on suppose que 'user.getPasswordUser()' est déjà un hash BCrypt
    public void register(User user) throws SQLException {
        String sql = "INSERT INTO User (nomUser, prenomUser, usernameUser, passwordUser, role, state) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getNomUser());
            ps.setString(2, user.getPrenomUser());
            ps.setString(3, user.getUsernameUser());
            ps.setString(4, user.getPasswordUser());
            ps.setString(5, user.getRole().name());
            ps.setBoolean(6, user.isState());

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
        String sql = "SELECT * FROM User WHERE usernameUser = ? AND state = true";
        System.out.println("Recherche de l'utilisateur : " + username);
        System.out.println("Requête SQL : " + sql);
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Utilisateur trouvé dans la base de données");
                    System.out.println("Rôle stocké : " + rs.getString("role"));
                    System.out.println("État stocké : " + rs.getBoolean("state"));
                    User user = extractUserFromResultSet(rs);
                    System.out.println("Rôle extrait : " + user.getRole());
                    return user;
                }
                System.out.println("Aucun utilisateur trouvé avec le nom d'utilisateur : " + username);
                
                // Vérifier si l'utilisateur existe mais est inactif
                String checkInactiveSql = "SELECT * FROM User WHERE usernameUser = ? AND state = false";
                try (PreparedStatement checkPs = conn.prepareStatement(checkInactiveSql)) {
                    checkPs.setString(1, username);
                    try (ResultSet inactiveRs = checkPs.executeQuery()) {
                        if (inactiveRs.next()) {
                            System.out.println("L'utilisateur existe mais est inactif");
                        }
                    }
                }
                
                return null;
            }
        }
    }

    public List<User> getAllUsers(boolean includeInactive) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";
        if (!includeInactive) {
            sql += " WHERE state = true";
        }

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        }
        return users;
    }

    public boolean logicalDelete(int userId) throws SQLException {
        String sql = "UPDATE User SET state = false WHERE IdUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public boolean updateUser(User user) throws SQLException {
        String query = "UPDATE User SET usernameUser = ?, nomUser = ?, prenomUser = ?, role = ? WHERE IdUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user.getUsernameUser());
            stmt.setString(2, user.getNomUser());
            stmt.setString(3, user.getPrenomUser());
            stmt.setString(4, user.getRole().name());
            stmt.setInt(5, user.getIdUser());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM User WHERE IdUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
                return null;
            }
        }
    }

    public boolean updateUserState(int userId, boolean state) throws SQLException {
        String query = "UPDATE User SET state = ? WHERE IdUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, state);
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        String roleStr = rs.getString("role");
        System.out.println("Extraction du rôle depuis la base : " + roleStr);
        Role role = Role.valueOf(roleStr);
        System.out.println("Rôle converti : " + role);
        
        return new User(
                rs.getInt("IdUser"),
                rs.getString("usernameUser"),
                rs.getString("passwordUser"),
                rs.getString("nomUser"),
                rs.getString("prenomUser"),
                role,
                rs.getBoolean("state")
        );
    }
}
