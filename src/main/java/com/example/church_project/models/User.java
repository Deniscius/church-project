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
    private String roleUser;
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

    public String getRoleUser() {
        return roleUser;
    }

    public void setRoleUser(String roleUser) {
        this.roleUser = roleUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }


    public User() {

    }

    public User(int IdUser, String usernameUser, String passwordUser, String roleUser) {
        this.IdUser = IdUser;
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
        this.roleUser = roleUser;

    }

    public List<User> list() throws SQLException {
        List<User> users = new ArrayList<>();
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "SELECET * FROM User";
            PreparedStatement preparedStatement = this.connection.prepareStatement(req);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setIdUser(resultSet.getInt("IdUser"));
                user.setUsernameUser(resultSet.getString("emailUser"));
                user.setPasswordUser(resultSet.getString("passwordUser"));

                users.add(user);
            }

            preparedStatement.close();
            this.connection.close();

        }

        return users;

    }

    @Override
    public void register(User user) throws SQLException {
        connection = IDBConfig.getConnection();
        if (connection != null) {
            String req = "INSERT INTO user (nomUser, prenomUser,usernameUser, password) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(req);

            preparedStatement.setString(1,user.getNomUser());
            preparedStatement.setString(2, user.getPrenomUser());
            preparedStatement.setString(3, user.getUsernameUser());
            preparedStatement.setString(4, user.getPasswordUser());

            int row = preparedStatement.executeUpdate();
            System.out.println(String.valueOf(row));

            preparedStatement.close();;
            this.connection.close();
        }


    }

    @Override
    public boolean login(String username, String password) throws SQLException {

        return false;
    }

}
