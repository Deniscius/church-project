package com.example.church_project;

import com.example.church_project.database.DatabaseInitializer;

public class AppBase {
    public static void main(String[] args) {
        new DatabaseInitializer().seedEssentialData();
        HelloApplication.main(args);
    }
}
