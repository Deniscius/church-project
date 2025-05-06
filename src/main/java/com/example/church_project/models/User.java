package com.example.church_project.models;

public class User {
    private int idUser;
    private String nomUser;
    private String prenomUser;
    private String usernameUser;
    private String passwordUser;
    private Role role;

    public User() {}

    // Constructeur pour l'inscription standard
    public User(String usernameUser, String passwordUser, String nomUser, String prenomUser) {
        this(usernameUser, passwordUser, nomUser, prenomUser, Role.USER);
    }

    // Constructeur complet avec rôle
    public User(String usernameUser, String passwordUser, String nomUser, String prenomUser, Role role) {
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.role = role;
    }

    public User(int idUser, String usernameUser, String passwordUser,
                String nomUser, String prenomUser, Role role) {
        this.idUser = idUser;
        this.usernameUser = usernameUser;
        this.passwordUser = passwordUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.role = role;
    }




    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        if (idUser < 0) throw new IllegalArgumentException("ID invalide");
        this.idUser = idUser;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean hasRole(Role roleToCheck) {
        return this.role == roleToCheck;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", usernameUser='" + usernameUser + '\'' +
                ", role=" + role +
                '}';
    }

}