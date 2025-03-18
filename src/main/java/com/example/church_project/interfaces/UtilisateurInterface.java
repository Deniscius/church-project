package com.example.church_project.interfaces;

import com.example.church_project.models.User;

import java.sql.SQLException;
import java.util.List;

public interface UtilisateurInterface {
    List<User> list() throws SQLException;
    void register(User user) throws SQLException;
    boolean login(String username, String password) throws SQLException;
}
