package com.example.church_project;

import com.example.church_project.database.DatabaseInitializer;

public class AppBase {
    public static void main(String[] args) {
        // Initialisation de la base de données
        new DatabaseInitializer().seedEssentialData();
        
        // Lancement de l'application principale
        ChurchApplication.main(args);
    }
}
