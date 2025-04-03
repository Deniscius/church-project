package com.example.church_project.dao;

import com.example.church_project.config.DBConfig;
import com.example.church_project.models.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public class UserDAO {

    public void register(User user) throws SQLException {
        String sql = "INSERT INTO User (nomUser, prenomUser, usernameUser, passwordUser) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            String hashedPassword = BCrypt.hashpw(user.getPasswordUser(), BCrypt.gensalt());

            ps.setString(1, user.getNomUser());
            ps.setString(2, user.getPrenomUser());
            ps.setString(3, user.getUsernameUser());
            ps.setString(4, hashedPassword);

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

    public boolean login(String username, String password) throws SQLException {
        String sql = "SELECT passwordUser FROM User WHERE usernameUser = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("passwordUser");
                    return BCrypt.checkpw(password, storedHash);
                }
                return false;
            }
        }
    }

    public boolean isUsernameAvailable(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM User WHERE usernameUser = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) == 0; // True si le compte n'existe pas
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
                    return new User(
                            rs.getInt("IdUser"),
                            rs.getString("usernameUser"),
                            rs.getString("passwordUser"),
                            rs.getString("nomUser"),
                            rs.getString("prenomUser")
                    );
                }
                return null;
            }
        }
    }
}