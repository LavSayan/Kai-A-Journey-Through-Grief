package com.example.kaiajourneythroughgrief.battle.nav;

import com.example.kaiajourneythroughgrief.BattleScene;
import com.example.kaiajourneythroughgrief.DialogueConfig;
import com.example.kaiajourneythroughgrief.DialogueScreen;
import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.stages.StageCatalog;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Moves the player between scenes: back to the level map, on to the next
 * battle, out to the menu, or through the post-victory dialogue.
 *
 * <p>The five original stage controllers each carried a private copy of this
 * logic. It lives here now, driven entirely by {@link StageConfig}.
 */
public final class SceneNavigator {

    private final Node sceneAnchor;

    public SceneNavigator(Node sceneAnchor) {
        this.sceneAnchor = sceneAnchor;
    }

    /** Back to the level map. */
    public void toLevelScreen() {
        showFxml("/com/example/kaiajourneythroughgrief/level_screen.fxml", "level screen");
    }

    /** Out to the title menu — the end of the journey after Stage 5. */
    public void toMenu() {
        showFxml("/com/example/kaiajourneythroughgrief/menu.fxml", "menu");
    }

    /**
     * Plays the stage's post-victory dialogue over the battlefield, then
     * continues to the next stage (or the menu, for the final stage).
     */
    public void playPostVictoryDialogue(StageConfig config, AnchorPane battleArea) {
        DialogueConfig dialogueConfig = config.postVictoryDialogue().get();

        battleArea.getChildren().clear();
        DialogueScreen dialogue = new DialogueScreen(dialogueConfig, () -> {
            if (config.nextStageId() != null) {
                toStage(config.nextStageId());
            } else {
                toMenu();
            }
        });

        dialogue.setPrefSize(battleArea.getWidth(), battleArea.getHeight());
        battleArea.getChildren().add(dialogue);
    }

    /** Loads another battle stage into the current window. */
    public void toStage(String stageId) {
        StageConfig config = StageCatalog.get(stageId);
        if (config == null) {
            System.err.println("[Battle] Unknown stage: " + stageId);
            return;
        }
        try {
            window().setScene(BattleScene.createScene(config));
        } catch (Exception e) {
            System.err.println("[Battle] Failed to load stage " + stageId + ": " + e.getMessage());
        }
    }

    private void showFxml(String path, String description) {
        try {
            java.net.URL resource = getClass().getResource(path);
            if (resource == null) {
                System.err.println("[Battle] " + description + " not found: " + path);
                return;
            }
            window().setScene(new Scene(new javafx.fxml.FXMLLoader(resource).load()));
        } catch (Exception e) {
            System.err.println("[Battle] Failed to load " + description + ": " + e.getMessage());
        }
    }

    private Stage window() {
        return (Stage) sceneAnchor.getScene().getWindow();
    }
}
