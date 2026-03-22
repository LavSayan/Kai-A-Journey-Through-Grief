module com.example.kaiajourneythroughgrief {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.prefs;


    opens com.example.kaiajourneythroughgrief to javafx.fxml;
    exports com.example.kaiajourneythroughgrief;
}