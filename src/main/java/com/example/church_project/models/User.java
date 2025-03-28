package com.example.church_project.models;

import com.example.church_project.dbConfig.IDBConfig;
import com.example.church_project.interfaces.UserInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class User implements UserInterface {
    private int IdUser;
    private String nomUser;
    private String prenomUser;
    private String usernameUser;
    private String passwordUser;
    private Connection connection;



    public void setIdUser(int idUser) {
        this.IdUser = idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public String getPasswordUser() {
        return passwordUser;
    }

    public void setPasswordUser(String passwordUser) {
        this.passwordUser = passwordUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }


    public User() {}

    public User(int IdUser, String usernameUser, String passwordUser, String nomUser, String prenomUser) {
        this.IdUser = IdUser;
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;

    }

    public List<User> list() throws SQLException {
        List<User> users = new ArrayList<>();
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "SELECT * FROM User";
            PreparedStatement preparedStatement = this.connection.prepareStatement(req);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setIdUser(resultSet.getInt("IdUser"));
                user.setUsernameUser(resultSet.getString("usernameUser"));
                user.setPasswordUser(resultSet.getString("passwordUser"));

                users.add(user);
            }

            preparedStatement.close();
            this.connection.close();

        }

        return users;

    }

    @Override
    public void register() throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection == null) {
            throw new SQLException("La connexion à la base de données a échoué");
        }

        String req = "INSERT INTO user (nomUser, prenomUser, usernameUser, passwordUser) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, this.getNomUser());
            preparedStatement.setString(2, this.getPrenomUser());
            preparedStatement.setString(3, this.getUsernameUser());
            preparedStatement.setString(4, this.getPasswordUser());

            int row = preparedStatement.executeUpdate();
            System.out.println("Nombre de lignes insérées : " + row);
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }
    }



    @Override
    public boolean login(String username, String password) throws SQLException {
        int rows = 0;
        // Initialise la connexion
        connection = IDBConfig.getConnection();
        if (connection == null) {
            throw new SQLException("La connexion à la base de données a échoué");
        }

        String req = "SELECT * FROM User WHERE usernameUser = ? AND passwordUser = ?";
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(req);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rows++;
            }
        } finally {
            if (preparedStatement != null) preparedStatement.close();
            if (connection != null) connection.close();
        }

        return rows > 0;
    }

}
