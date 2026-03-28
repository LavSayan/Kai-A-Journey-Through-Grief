module com.example.kaiajourneythroughgrief {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;
    requires com.fasterxml.jackson.databind;  // <-- add this
    requires com.fasterxml.jackson.core;      // often needed too
    requires com.fasterxml.jackson.annotation; // for annotations


    opens com.example.kaiajourneythroughgrief to javafx.fxml;
    exports com.example.kaiajourneythroughgrief;
}