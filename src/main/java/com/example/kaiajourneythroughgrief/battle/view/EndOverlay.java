package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.Theme;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The victory / defeat card shown over the battlefield.
 *
 * <p>Unlike the original, this card is not dismissible by clicking behind it:
 * the canvas input handlers are already torn down at that point, so dismissing
 * left the player on a dead screen with nothing to click.
 */
public final class EndOverlay {

    private EndOverlay() {}

    public static void show(AnchorPane battleArea, StageConfig config,
                            boolean won, Runnable onRestart, Runnable onNext) {
        Color accent = won ? Theme.PLAYER_GREEN : Theme.ENEMY_RED;

        Rectangle backdrop = new Rectangle();
        backdrop.widthProperty().bind(battleArea.widthProperty());
        backdrop.heightProperty().bind(battleArea.heightProperty());
        backdrop.setFill(Theme.fade(Color.BLACK, 0.78));

        Label eyebrow = new Label(won ? "RESULT — VICTORY" : "RESULT — DEFEAT");
        eyebrow.setFont(Theme.META_8);
        eyebrow.setTextFill(Theme.META_TEXT);
        eyebrow.setStyle("-fx-letter-spacing: 2;");

        Label heading = new Label(won ? "Victory" : "Defeat");
        heading.setFont(Theme.HEADING_36);
        heading.setTextFill(won ? Theme.COMPLETION : Theme.ENEMY_RED);
        heading.setEffect(new DropShadow(14,
                won ? Theme.fade(Theme.COMPLETION, 0.35) : Theme.fade(Theme.ENEMY_RED, 0.35)));

        Label subtitle = new Label(won ? config.victorySubtitle() : "The enemy overcame you.");
        subtitle.setFont(Theme.SUBTITLE_13);
        subtitle.setTextFill(Theme.COMPLETE_TEXT);

        Rectangle divider = new Rectangle(260, 1);
        divider.setFill(Theme.BAND_DIVIDER);

        VBox popup = new VBox(16, eyebrow, heading, subtitle, divider);

        if (won && onNext != null) {
            popup.getChildren().add(StyledButtons.overlay("▶  Next Stage", Theme.COMPLETION, onNext));
        }
        popup.getChildren().add(StyledButtons.overlay("↺  Play Again", accent, onRestart));

        popup.setAlignment(Pos.CENTER);
        popup.setMaxWidth(400);
        popup.setStyle(
                "-fx-background-color: " + StyledButtons.toHex(Theme.CARD_BG) + ";" +
                        "-fx-padding: 32 36 28 36;" +
                        "-fx-border-color: " + StyledButtons.toHex(accent) + ";" +
                        "-fx-border-width: 1;-fx-border-radius: 5;-fx-background-radius: 5;");
        popup.setEffect(new DropShadow(24, Theme.fade(accent, 0.75)));

        StackPane centred = new StackPane(backdrop, popup);
        AnchorPane.setTopAnchor(centred, 0.0);
        AnchorPane.setBottomAnchor(centred, 0.0);
        AnchorPane.setLeftAnchor(centred, 0.0);
        AnchorPane.setRightAnchor(centred, 0.0);

        battleArea.getChildren().add(centred);
        Effects.fadeIn(popup, 350);
    }
}
