module com.example.church_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.church_project to javafx.fxml;
    exports com.example.church_project;

    opens com.example.church_project.controllers to javafx.fxml;
    exports com.example.church_project.controllers;

    opens com.example.church_project.models to javafx.fxml;
    exports com.example.church_project.models;

    opens com.example.church_project.interfaces to javafx.fxml;
    exports com.example.church_project.interfaces;
}