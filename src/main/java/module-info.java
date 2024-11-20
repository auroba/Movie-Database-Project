module com.example.aurobaahmad_assignment4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.healthmarketscience.jackcess;
    requires com.google.gson;



    opens com.example.aurobaahmad_assignment4 to javafx.fxml, com.google.gson;
    exports com.example.aurobaahmad_assignment4;
}