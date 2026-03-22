module com.example.kaiajourneythroughgrief {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.kaiajourneythroughgrief to javafx.fxml;
    exports com.example.kaiajourneythroughgrief;
}