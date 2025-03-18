package com.example.church_project.models;

import com.example.church_project.dbConfig.IDBConfig;
import com.example.church_project.interfaces.UtilisateurInterface;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;

public class User implements UtilisateurInterface {
    private int IdUser;
    private String nomUser;
    private String prenomUser;
    private String emailUser;
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

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
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

    public User(int IdUser,  String emailUser, String passwordUser, String roleUser) {
        this.IdUser = IdUser;
        this.emailUser = emailUser;
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
                user.setEmailUser(resultSet.getString("emailUser"));
                user.setPasswordUser(resultSet.getString("passwordUser"));

                users.add(user);
            }

        }


    }

    @Override
    public void register(User user) throws SQLException {


    }

    @Override
    public boolean login(String username, String password) throws SQLException {

        return false;
    }

}
