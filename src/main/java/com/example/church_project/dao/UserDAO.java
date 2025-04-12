package com.example.church_project.dao;

import com.example.church_project.config.DBConfig;
import com.example.church_project.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void register(User user) throws SQLException {
        String sql = "INSERT INTO User (nomUser, prenomUser, usernameUser, passwordUser, roleUser) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getNomUser());
            ps.setString(2, user.getPrenomUser());
            ps.setString(3, user.getUsernameUser());
            ps.setString(4, user.getPasswordUser()); // Utilise tel quel (hashé ou non)
            ps.setString(5, user.getRoleUser());

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setIdUser(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }


    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM User WHERE usernameUser = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("passwordUser");

                    if (BCrypt.checkpw(password, storedHash)) {

                        User user = new User();
                        user.setIdUser(rs.getInt("idUser"));
                        user.setUsernameUser(rs.getString("usernameUser"));
                        user.setPasswordUser(storedHash);
                        user.setNomUser(rs.getString("nomUser"));
                        user.setPrenomUser(rs.getString("prenomUser"));
                        user.setRoleUser(rs.getString("roleUser"));
                        return user;
                    }
                }
            }
        }
        return null; // Mauvais identifiants
    }



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


    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM User WHERE usernameUser = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("IdUser"),
                            rs.getString("usernameUser"),
                            rs.getString("passwordUser"),
                            rs.getString("nomUser"),
                            rs.getString("prenomUser")
                    );
                    user.setRoleUser(rs.getString("roleUser"));
                    return user;
                }
                return null;
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM User";

        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("IdUser"),
                        rs.getString("usernameUser"),
                        rs.getString("passwordUser"),
                        rs.getString("nomUser"),
                        rs.getString("prenomUser")
                );
                user.setRoleUser(rs.getString("roleUser"));
                users.add(user);
            }
        }
        return users;
    }


    public void updateUser(User user) throws SQLException {
        String sql = "UPDATE User SET nomUser = ?, prenomUser = ?, usernameUser = ?, passwordUser = ?, roleUser = ? WHERE IdUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getNomUser());
            ps.setString(2, user.getPrenomUser());
            ps.setString(3, user.getUsernameUser());
            ps.setString(4, user.getPasswordUser());
            ps.setString(5, user.getRoleUser());
            ps.setInt(6, user.getIdUser());
            ps.executeUpdate();
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM User WHERE IdUser = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }


    public void registerWithDefaultRole(User user) throws SQLException {
        if (user.getRoleUser() == null || user.getRoleUser().isEmpty()) {
            user.setRoleUser("USER");
        }

        register(user);
    }

    public int countUsers() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM User";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    public List<User> findAll() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM User";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User(
                        rs.getInt("idUser"),
                        rs.getString("usernameUser"),
                        rs.getString("passwordUser"),
                        rs.getString("nomUser"),
                        rs.getString("prenomUser"),
                        rs.getString("roleUser")
                );
                userList.add(user);
            }
        }
        return userList;
    }



}
