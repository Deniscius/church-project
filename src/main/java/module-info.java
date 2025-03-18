module com.example.church_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.church_project to javafx.fxml;
    exports com.example.church_project;
}