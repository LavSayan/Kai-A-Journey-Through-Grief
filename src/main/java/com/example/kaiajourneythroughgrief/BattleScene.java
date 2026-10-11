package com.example.kaiajourneythroughgrief;

import com.example.kaiajourneythroughgrief.battle.BattleController;
import com.example.kaiajourneythroughgrief.battle.StageConfig;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Builds a battle scene for any stage.
 *
 * <p>{@code battle.fxml} deliberately declares no {@code fx:controller}, so the
 * controller can be installed with its {@link StageConfig} already in hand. That
 * is what lets all five stages share one view and one controller.
 */
public final class BattleScene {

    private BattleScene() {}

    public static Scene createScene(StageConfig config) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                BattleScene.class.getResource("/com/example/kaiajourneythroughgrief/battle.fxml"));
        loader.setController(new BattleController(config));
        return new Scene(loader.load());
    }
}
