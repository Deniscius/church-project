package com.example.church_project.models;

import javafx.beans.property.*;

public class User {
    private final IntegerProperty idUser = new SimpleIntegerProperty();
    private final StringProperty nomUser = new SimpleStringProperty();
    private final StringProperty prenomUser = new SimpleStringProperty();
    private final StringProperty usernameUser = new SimpleStringProperty();
    private final StringProperty passwordUser = new SimpleStringProperty();
    private final StringProperty roleUser = new SimpleStringProperty();

    public User() {}

    public User(String usernameUser, String passwordUser, String nomUser, String prenomUser, String roleUser) {
        this.usernameUser.set(usernameUser);
        this.passwordUser.set(passwordUser);
        this.nomUser.set(nomUser);
        this.prenomUser.set(prenomUser);
        this.roleUser.set(roleUser);
    }

    public User(String usernameUser, String passwordUser, String nomUser, String prenomUser) {
        this(usernameUser, passwordUser, nomUser, prenomUser, "USER");
    }

    public User(int idUser, String usernameUser, String passwordUser, String nomUser, String prenomUser, String roleUser) {
        this.idUser.set(idUser);
        this.usernameUser.set(usernameUser);
        this.passwordUser.set(passwordUser);
        this.nomUser.set(nomUser);
        this.prenomUser.set(prenomUser);
        this.roleUser.set(roleUser);
    }

    public User(int idUser, String usernameUser, String passwordUser, String nomUser, String prenomUser) {
        this(idUser, usernameUser, passwordUser, nomUser, prenomUser, "USER");
    }

    // Getters et Setters standards
    public int getIdUser() { return idUser.get(); }
    public void setIdUser(int idUser) { this.idUser.set(idUser); }

    public String getNomUser() { return nomUser.get(); }
    public void setNomUser(String nomUser) { this.nomUser.set(nomUser); }

    public String getPrenomUser() { return prenomUser.get(); }
    public void setPrenomUser(String prenomUser) { this.prenomUser.set(prenomUser); }

    public String getUsernameUser() { return usernameUser.get(); }
    public void setUsernameUser(String usernameUser) { this.usernameUser.set(usernameUser); }

    public String getPasswordUser() { return passwordUser.get(); }
    public void setPasswordUser(String passwordUser) { this.passwordUser.set(passwordUser); }

    public String getRoleUser() { return roleUser.get(); }
    public void setRoleUser(String roleUser) { this.roleUser.set(roleUser); }

    // Propriétés observables pour JavaFX
    public IntegerProperty idUserProperty() { return idUser; }
    public StringProperty nomUserProperty() { return nomUser; }
    public StringProperty prenomUserProperty() { return prenomUser; }
    public StringProperty usernameUserProperty() { return usernameUser; }
    public StringProperty passwordUserProperty() { return passwordUser; }
    public StringProperty roleUserProperty() { return roleUser; }
}
