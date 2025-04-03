package com.example.church_project.models;

public class User {
    private int IdUser;
    private String nomUser;
    private String prenomUser;
    private String usernameUser;
    private String passwordUser;


    public User() {}

    public User(String usernameUser, String passwordUser, String nomUser, String prenomUser) {
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
    }


    public User(int IdUser, String usernameUser, String passwordUser, String nomUser, String prenomUser) {
        this.IdUser = IdUser;
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
    }

    // Getters et Setters (gardez les validations)
    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        if (idUser < 0) throw new IllegalArgumentException("ID invalide");
        this.IdUser = idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
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
}