package com.example.kaiajourneythroughgrief;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/kaiajourneythroughgrief/stage_one.fxml")
        );
        Scene scene = new Scene(loader.load(), 1000, 740);
        stage.setScene(scene);
        stage.setTitle("Kai: A Journey Through Grief");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}