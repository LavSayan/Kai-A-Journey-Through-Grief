package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.Theme;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Builds the chrome around the board: the title bar, the battlefield backdrop
 * with both character sprites, and the vitality meter.
 *
 * <p>All of it is derived from the {@link StageConfig} and the shared
 * {@link Theme}, so a new stage needs no changes here.
 */
public final class BattleSceneView {

    /** Title-bar actions, supplied by the controller. */
    public record Actions(Runnable onRestart, Runnable onBack, Runnable onLeave) {}

    private final StageConfig config;
    private final AnchorPane battleArea;
    private final ProgressBar healthBar = new ProgressBar(Theme.STARTING_HEALTH);

    private final VBox playerBox = new VBox();
    private final VBox enemyBox = new VBox();

    public BattleSceneView(StageConfig config, StackPane topPanel,
                           AnchorPane battleArea, Actions actions) {
        this.config = config;
        this.battleArea = battleArea;

        buildTitleBar(topPanel, actions);
        buildBattlefield();
    }

    // ── Accessors ──────────────────────────────────────────────────────────

    public AnchorPane battleArea() { return battleArea; }
    public VBox playerBox()       { return playerBox; }
    public VBox enemyBox()        { return enemyBox; }

    public void setHealth(double value) {
        healthBar.setProgress(value);
    }

    /** Removes any overlay card previously added to the battlefield. */
    public void clearBattlefield() {
        battleArea.getChildren().removeIf(node -> node instanceof StackPane);
    }

    // ── Styling ────────────────────────────────────────────────────────────

    public static void styleScene(StackPane topPanel, StackPane bottomPanel, AnchorPane battleArea) {
        battleArea.setStyle("-fx-background-color: " + StyledButtons.toHex(Theme.CANVAS_BG) + ";");
        bottomPanel.setStyle(
                "-fx-background-color: " + StyledButtons.toHex(Theme.TITLE_BAR) + ";" +
                        "-fx-border-color: "     + StyledButtons.toHex(Theme.BAND_DIVIDER) + ";" +
                        "-fx-border-width: 1 0 0 0;");
        topPanel.setStyle("-fx-background-color: " + StyledButtons.toHex(Theme.TITLE_BAR) + ";");
    }

    // ── Title bar ──────────────────────────────────────────────────────────

    private void buildTitleBar(StackPane topPanel, Actions actions) {
        Label eyebrow = new Label(config.eyebrowLabel());
        eyebrow.setFont(Theme.META_8);
        eyebrow.setTextFill(Theme.META_TEXT);
        eyebrow.setStyle("-fx-letter-spacing: 2;");

        Label title = new Label(config.title());
        title.setFont(Theme.TITLE_15);
        title.setTextFill(Theme.TITLE_TEXT);
        title.setEffect(new DropShadow(8, Theme.fade(Color.BLACK, 0.9)));

        Rectangle accentLine = new Rectangle(120, 1);
        accentLine.setFill(Theme.fade(Theme.ACCENT_LINE, 0.85));

        VBox titleBox = new VBox(3, eyebrow, title, accentLine);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        HBox menuButtons = new HBox(8,
                StyledButtons.menu("↺  Restart", actions.onRestart()),
                StyledButtons.menu("←  Back", actions.onBack()),
                StyledButtons.menu("✕  Leave", actions.onLeave()));
        menuButtons.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(titleBox, spacer, menuButtons);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(8, 20, 8, 20));

        Rectangle divider = new Rectangle();
        divider.setFill(Theme.BAND_DIVIDER);
        divider.setHeight(1);
        divider.widthProperty().bind(topPanel.widthProperty());

        topPanel.getChildren().setAll(new VBox(topBar, divider));
    }

    // ── Battlefield ────────────────────────────────────────────────────────

    private void buildBattlefield() {
        ImageView background = loadImage(config.backgroundPath(), null, null, false);
        if (background != null) {
            background.fitWidthProperty().bind(battleArea.widthProperty());
            background.fitHeightProperty().bind(battleArea.heightProperty());
            background.setPreserveRatio(false);
            AnchorPane.setTopAnchor(background, 0.0);
            AnchorPane.setBottomAnchor(background, 0.0);
            AnchorPane.setLeftAnchor(background, 0.0);
            AnchorPane.setRightAnchor(background, 0.0);
            battleArea.getChildren().add(background);
        }

        buildCharacter(playerBox,
                loadImage(config.playerPath(), Theme.CHAR_WIDTH, Theme.CHAR_HEIGHT, true),
                Theme.PLAYER_NAME, Theme.PLAYER_GREEN);
        AnchorPane.setLeftAnchor(playerBox, Theme.CHAR_LEFT);
        AnchorPane.setBottomAnchor(playerBox, Theme.CHAR_BOTTOM);
        battleArea.getChildren().add(playerBox);

        buildCharacter(enemyBox,
                loadImage(config.enemyPath(), Theme.CHAR_WIDTH, Theme.CHAR_HEIGHT, true),
                config.enemyName(), Theme.ENEMY_RED);
        AnchorPane.setRightAnchor(enemyBox, Theme.CHAR_LEFT);
        AnchorPane.setBottomAnchor(enemyBox, Theme.CHAR_BOTTOM);
        battleArea.getChildren().add(enemyBox);

        buildVitalityBar();
    }

    private static void buildCharacter(VBox box, ImageView sprite, String name, Color color) {
        box.setAlignment(Pos.BOTTOM_CENTER);
        if (sprite != null) box.getChildren().add(sprite);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Theme.META_9);
        nameLabel.setTextFill(color);
        nameLabel.setStyle(
                "-fx-background-color: rgba(7,7,14,0.75);" +
                        "-fx-border-color: " + StyledButtons.toHex(color) + ";" +
                        "-fx-border-width: 1;-fx-border-radius: 3;" +
                        "-fx-background-radius: 3;-fx-padding: 2 10 2 10;" +
                        "-fx-letter-spacing: 1;");
        box.getChildren().add(nameLabel);
    }

    private void buildVitalityBar() {
        healthBar.setPrefWidth(340);
        healthBar.setPrefHeight(14);
        healthBar.setStyle(
                "-fx-accent: "                   + StyledButtons.toHex(Theme.PLAYER_GREEN) + ";" +
                        "-fx-control-inner-background: " + StyledButtons.toHex(Theme.ENEMY_RED) + ";" +
                        "-fx-background-radius: 2;" +
                        "-fx-border-radius: 2;");

        Label vitality = new Label("VITALITY");
        vitality.setFont(Theme.META_10);
        vitality.setTextFill(Theme.META_TEXT);
        vitality.setStyle("-fx-letter-spacing: 2;");

        HBox barRow = new HBox(10,
                tag("PLAYER", Theme.PLAYER_GREEN),
                healthBar,
                tag("ENEMY", Theme.ENEMY_RED));
        barRow.setAlignment(Pos.CENTER);

        VBox container = new VBox(4, vitality, barRow);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 20, 10, 20));
        container.setStyle(
                "-fx-background-color: rgba(5,5,16,0.72);" +
                        "-fx-border-color: " + StyledButtons.toHex(Theme.BAND_DIVIDER) + ";" +
                        "-fx-border-width: 0 0 1 0;");

        AnchorPane.setTopAnchor(container, 0.0);
        AnchorPane.setLeftAnchor(container, 0.0);
        AnchorPane.setRightAnchor(container, 0.0);
        battleArea.getChildren().add(container);
    }

    private static Label tag(String text, Color color) {
        Label label = new Label(text);
        label.setFont(Theme.META_9);
        label.setTextFill(color);
        return label;
    }

    // ── Assets ─────────────────────────────────────────────────────────────

    /**
     * Stage assets are addressed absolutely: they live next to the root package,
     * but this view no longer does.
     */
    private static final String ASSET_ROOT = "/com/example/kaiajourneythroughgrief/";

    private ImageView loadImage(String path, Double fitWidth, Double fitHeight, boolean preserve) {
        java.net.URL res = getClass().getResource(ASSET_ROOT + path);
        if (res == null) {
            System.err.println("[Battle] Asset not found: " + ASSET_ROOT + path);
            return null;
        }
        try {
            ImageView view = new ImageView(new Image(res.toExternalForm(), false));
            if (fitWidth != null) view.setFitWidth(fitWidth);
            if (fitHeight != null) view.setFitHeight(fitHeight);
            view.setPreserveRatio(preserve);
            view.setSmooth(true);
            return view;
        } catch (Exception e) {
            System.err.println("[Battle] Could not load " + path + " — " + e.getMessage());
            return null;
        }
    }
}
