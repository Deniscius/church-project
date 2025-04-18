package com.example.church_project.interfaces;

import com.example.church_project.models.User;

import java.sql.SQLException;
import java.util.List;

public interface UserInterface {
    List<User> list() throws SQLException;
    void register() throws SQLException;
    boolean login(String username, String password) throws SQLException;
}
