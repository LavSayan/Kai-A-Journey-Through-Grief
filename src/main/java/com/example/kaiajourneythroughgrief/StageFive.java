package com.example.kaiajourneythroughgrief;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class StageFive implements Initializable {

    // ── FXML injected nodes ────────────────────────────────────────────────
    @FXML private StackPane   topPanel;
    @FXML private ProgressBar healthBar;
    @FXML private AnchorPane  battleArea;
    @FXML private StackPane   bottomPanel;

    // ── Sort modes ─────────────────────────────────────────────────────────
    private enum SortMode { ASCENDING, DESCENDING }

    // =========================================================================
    // DESIGN SYSTEM
    // =========================================================================

    private static final String C_CANVAS_BG    = "#07070F";
    private static final String C_TITLE_BAR    = "#050510";
    private static final String C_CARD_BG      = "#07070E";
    private static final String C_BAND_DIVIDER = "#1C1C30";
    private static final String C_TITLE_TEXT   = "#C4C4DC";
    private static final String C_ACTIVE_TITLE = "#D8D8EC";
    private static final String C_META_TEXT    = "#F9F6EE";
    private static final String C_COMPLETE_TEXT= "#5E5E78";
    private static final String C_COMPLETION   = "#00FF88";
    private static final String C_ACCENT_LINE  = "#B8860B";
    private static final String C_PLAYER_GREEN = "#0a6f0b";
    private static final String C_ENEMY_RED    = "#af0000";
    private static final String C_ASCENDING    = "#ADD8E6";
    private static final String C_DESCENDING   = "#DC143C";
    private static final String C_LOCKED       = "#9370DB";

    // ── Assets ────────────────────────────────────────────────────────────
    private static final String IMG_BACKGROUND = "assets/stage2/background.png";
    private static final String IMG_PLAYER     = "assets/stage2/player.gif";
    private static final String IMG_ENEMY      = "assets/stage2/enemy.gif";

    // ── Character size ────────────────────────────────────────────────────
    private static final double CHAR_WIDTH  = 300;
    private static final double CHAR_HEIGHT = 300;

    // ── Sorting config ────────────────────────────────────────────────────
    private static final int    NUMBER_COUNT = 10;
    private static final double BOX_HEIGHT   = 56;
    private static final double SPACING      = 8;
    private static final double MARGIN_X     = 20;
    private static final int    SWAP_FRAMES  = 60;
    private static final int    SWAP_MILLIS  = 300;
    private static final double CARD_RADIUS  = 5;
    private static final double ACCENT_W     = 3;

    // ── Health ────────────────────────────────────────────────────────────
    private static final double DRAIN_PER_SECOND = 0.04;
    private static final double GAIN_PER_CORRECT = 0.08;

    // ── Locked tiles config ────────────────────────────────────────────────
    private static final int LOCKED_TILES_INITIAL    = 4;
    private static final int CORRECT_COUNT_TO_UNLOCK = 2;

    // =========================================================================
    // ★ DEVELOPER SETTINGS
    // =========================================================================

    /** How many arrays the player must solve before victory. */
    private static final int ARRAYS_TO_WIN = 1;

    /**
     * How much health the player loses on a wrong move (a swap that increases
     * or does not reduce the number of inversions in the array).
     * Range: 0.0 – 1.0  (e.g. 0.05 = 5 % of the bar per wrong swap)
     */
    private static final double LOSS_PER_WRONG = 0.05;

    // =========================================================================
    // STATE
    // =========================================================================

    // Health
    private double         playerMeter   = 0.5;
    private long           lastNanoTime  = -1;
    private boolean        gameOver      = false;
    private AnimationTimer gameLoop      = null;
    private ProgressBar    battleHealthBar;
    private VBox           healthBarContainer;

    // ── Array solve counter ───────────────────────────────────────────────
    private int arraysSolved = 0;

    // ── Locked tiles ───────────────────────────────────────────────────────
    private boolean[] tileIsLocked   = new boolean[NUMBER_COUNT];
    private int       correctsPending = 0;

    // Sorting
    private SortMode             currentMode;
    private String[]             levelData;
    private Map<String, Integer> currentValues;

    // Drag
    private boolean dragging    = false;
    private boolean animating   = false;
    private int     dragIndex   = -1;
    private double  dragOffsetX, dragOffsetY, dragX, dragY;

    // Layout
    private double boxWidth;
    private int    maxPerRow;

    // Canvas
    private Canvas          canvas;
    private GraphicsContext gc;
    private final Random    random = new Random();

    // ── Character VBox references (needed for shake animations) ──────────
    private VBox playerBox;
    private VBox enemyBox;

    // =========================================================================
    // INIT
    // =========================================================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        healthBar.setVisible(false);
        healthBar.setManaged(false);

        styleScene();
        buildTitleBar();
        buildBattleScene();
        buildSortingCanvas();
        startGameLoop();
    }

    private void styleScene() {
        battleArea.setStyle("-fx-background-color: " + C_CANVAS_BG + ";");
        bottomPanel.setStyle(
                "-fx-background-color: " + C_TITLE_BAR + ";" +
                        "-fx-border-color: "     + C_BAND_DIVIDER + ";" +
                        "-fx-border-width: 1 0 0 0;"
        );
        topPanel.setStyle("-fx-background-color: " + C_TITLE_BAR + ";");
    }

    // =========================================================================
    // TITLE BAR
    // =========================================================================

    private void buildTitleBar() {
        Label eyebrow = new Label("STAGE 5");
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 8));
        eyebrow.setTextFill(Color.web(C_META_TEXT));
        eyebrow.setStyle("-fx-letter-spacing: 2;");

        Label title = new Label("A Garden at Dawn");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        title.setTextFill(Color.web(C_TITLE_TEXT));
        title.setEffect(new DropShadow(8, Color.web("#000000", 0.9)));

        Rectangle accentLine = new Rectangle(120, 1);
        accentLine.setFill(Color.web(C_ACCENT_LINE, 0.85));

        VBox titleBox = new VBox(3, eyebrow, title, accentLine);
        titleBox.setAlignment(Pos.CENTER_LEFT);

        Button restartBtn = makeMenuButton("↺  Restart", this::restartGame);
        Button backBtn    = makeMenuButton("←  Back",    this::handleBack);
        Button leaveBtn   = makeMenuButton("✕  Leave",   this::handleLeave);

        HBox menuButtons = new HBox(8, restartBtn, backBtn, leaveBtn);
        menuButtons.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topBar = new HBox(titleBox, spacer, menuButtons);
        topBar.setAlignment(Pos.CENTER);
        topBar.setPadding(new Insets(8, 20, 8, 20));

        Rectangle divider = new Rectangle();
        divider.setFill(Color.web(C_BAND_DIVIDER));
        divider.setHeight(1);
        divider.widthProperty().bind(topPanel.widthProperty());

        topPanel.getChildren().setAll(new VBox(topBar, divider));
    }

    private Button makeMenuButton(String label, Runnable action) {
        Button btn = new Button(label);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        btn.setTextFill(Color.web(C_META_TEXT));
        String base  = "-fx-background-color: transparent;"
                + "-fx-border-color: " + C_BAND_DIVIDER + ";"
                + "-fx-border-width: 1;-fx-border-radius: 3;"
                + "-fx-padding: 4 10;-fx-cursor: hand;";
        String hover = "-fx-background-color: " + C_BAND_DIVIDER + ";"
                + "-fx-border-color: " + C_ACTIVE_TITLE + ";"
                + "-fx-border-width: 1;-fx-border-radius: 3;"
                + "-fx-padding: 4 10;-fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> { btn.setStyle(hover); btn.setTextFill(Color.web(C_ACTIVE_TITLE)); });
        btn.setOnMouseExited(e  -> { btn.setStyle(base);  btn.setTextFill(Color.web(C_META_TEXT));   });
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void handleBack() {
        if (gameLoop != null) gameLoop.stop();
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/kaiajourneythroughgrief/level_screen.fxml"));
            Scene scene = new Scene(loader.load());
            ((Stage) battleArea.getScene().getWindow()).setScene(scene);
        } catch (Exception e) {
            System.err.println("[StageFive] Back failed: " + e.getMessage());
        }
    }

    private void handleLeave() {
        if (gameLoop != null) gameLoop.stop();
        javafx.application.Platform.exit();
    }

    // =========================================================================
    // BATTLE SCENE
    // =========================================================================

    private void buildBattleScene() {
        ImageView bg = loadImage(IMG_BACKGROUND, null, null, false);
        if (bg != null) {
            bg.fitWidthProperty().bind(battleArea.widthProperty());
            bg.fitHeightProperty().bind(battleArea.heightProperty());
            bg.setPreserveRatio(false);
            AnchorPane.setTopAnchor(bg,    0.0);
            AnchorPane.setBottomAnchor(bg, 0.0);
            AnchorPane.setLeftAnchor(bg,   0.0);
            AnchorPane.setRightAnchor(bg,  0.0);
            battleArea.getChildren().add(bg);
        }

        // Store references so shake animations can target them
        playerBox = buildCharacterNode(
                loadImage(IMG_PLAYER, CHAR_WIDTH, CHAR_HEIGHT, true), "PLAYER", true);
        AnchorPane.setLeftAnchor(playerBox,   80.0);
        AnchorPane.setBottomAnchor(playerBox, 40.0);
        battleArea.getChildren().add(playerBox);

        enemyBox = buildCharacterNode(
                loadImage(IMG_ENEMY, CHAR_WIDTH, CHAR_HEIGHT, true), "ENEMY", false);
        AnchorPane.setRightAnchor(enemyBox,   80.0);
        AnchorPane.setBottomAnchor(enemyBox,  40.0);
        battleArea.getChildren().add(enemyBox);

        buildInSceneHealthBar();
    }

    private void buildInSceneHealthBar() {
        battleHealthBar = new ProgressBar(playerMeter);
        battleHealthBar.setPrefWidth(340);
        battleHealthBar.setPrefHeight(14);
        applyHealthBarStyle();

        Label playerTag = new Label("PLAYER");
        playerTag.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        playerTag.setTextFill(Color.web(C_PLAYER_GREEN));

        Label enemyTag = new Label("ENEMY");
        enemyTag.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        enemyTag.setTextFill(Color.web(C_ENEMY_RED));

        Label vitality = new Label("VITALITY");
        vitality.setFont(Font.font("Courier New", FontWeight.BOLD, 10));
        vitality.setTextFill(Color.web(C_META_TEXT));
        vitality.setStyle("-fx-letter-spacing: 2;");

        HBox barRow = new HBox(10, playerTag, battleHealthBar, enemyTag);
        barRow.setAlignment(Pos.CENTER);

        healthBarContainer = new VBox(4, vitality, barRow);
        healthBarContainer.setAlignment(Pos.CENTER);
        healthBarContainer.setPadding(new Insets(10, 20, 10, 20));
        healthBarContainer.setStyle(
                "-fx-background-color: rgba(5,5,16,0.72);" +
                        "-fx-border-color: " + C_BAND_DIVIDER + ";" +
                        "-fx-border-width: 0 0 1 0;"
        );

        AnchorPane.setTopAnchor(healthBarContainer,   0.0);
        AnchorPane.setLeftAnchor(healthBarContainer,  0.0);
        AnchorPane.setRightAnchor(healthBarContainer, 0.0);
        battleArea.getChildren().add(healthBarContainer);
    }

    private void applyHealthBarStyle() {
        battleHealthBar.setStyle(
                "-fx-accent: "                   + C_PLAYER_GREEN + ";" +
                        "-fx-control-inner-background: " + C_ENEMY_RED    + ";" +
                        "-fx-background-radius: 2;" +
                        "-fx-border-radius: 2;"
        );
    }

    // =========================================================================
    // CHARACTER NODE
    // =========================================================================

    private VBox buildCharacterNode(ImageView sprite, String name, boolean isPlayer) {
        String color = isPlayer ? C_PLAYER_GREEN : C_ENEMY_RED;
        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        nameLabel.setTextFill(Color.web(color));
        nameLabel.setStyle(
                "-fx-background-color: rgba(7,7,14,0.75);" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 1;-fx-border-radius: 3;" +
                        "-fx-background-radius: 3;-fx-padding: 2 10 2 10;" +
                        "-fx-letter-spacing: 1;"
        );
        VBox box = new VBox(6);
        box.setAlignment(Pos.BOTTOM_CENTER);
        if (sprite != null) box.getChildren().add(sprite);
        box.getChildren().add(nameLabel);
        return box;
    }

    // =========================================================================
    // IMAGE LOADER
    // =========================================================================

    private ImageView loadImage(String filename, Double fitWidth, Double fitHeight, boolean preserve) {
        try {
            URL res = getClass().getResource(filename);
            if (res == null) return null;
            Image img = new Image(res.toExternalForm(), false);
            ImageView iv = new ImageView(img);
            if (fitWidth  != null) iv.setFitWidth(fitWidth);
            if (fitHeight != null) iv.setFitHeight(fitHeight);
            iv.setPreserveRatio(preserve);
            iv.setSmooth(true);
            return iv;
        } catch (Exception e) {
            System.err.println("[StageFive] Could not load: " + filename + " — " + e.getMessage());
            return null;
        }
    }

    // =========================================================================
    // GAME LOOP
    // =========================================================================

    private void startGameLoop() {
        if (gameLoop != null) gameLoop.stop();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOver) return;
                if (lastNanoTime < 0) { lastNanoTime = now; return; }

                double delta = (now - lastNanoTime) / 1_000_000_000.0;
                lastNanoTime = now;

                setMeter(playerMeter - DRAIN_PER_SECOND * delta);

                if (playerMeter <= 0.0) { triggerGameOver(false); return; }

                if (!animating && !dragging && isSorted()) {
                    arraysSolved++;
                    if (arraysSolved >= ARRAYS_TO_WIN) {
                        triggerGameOver(true);
                    } else {
                        generateRandomLevel();
                        recalcLayout();
                        drawArray();
                        drawProgressIndicator();
                    }
                }
            }
        };
        gameLoop.start();
    }

    // =========================================================================
    // HEALTH HELPERS
    // =========================================================================

    private void setMeter(double value) {
        playerMeter = Math.max(0.0, Math.min(1.0, value));
        if (battleHealthBar != null) battleHealthBar.setProgress(playerMeter);
    }

    private void gainHealth(int correctCount) {
        if (!gameOver) setMeter(playerMeter + GAIN_PER_CORRECT * correctCount);
    }

    // =========================================================================
    // PROGRESS INDICATOR
    // =========================================================================

    private void drawProgressIndicator() {
        drawArray();
    }

    // =========================================================================
    // SHAKE ANIMATIONS
    // =========================================================================

    /**
     * Plays a subtle horizontal shake on the given VBox.
     * @param node      the character VBox to shake
     * @param amplitude max pixel offset
     * @param cycles    number of back-and-forth oscillations
     * @param millis    total duration in milliseconds
     */
    private void shakeNode(VBox node, double amplitude, int cycles, int millis) {
        if (node == null) return;

        Timeline shake = new Timeline();
        int steps       = cycles * 4;
        double stepTime = millis / (double) steps;

        for (int i = 0; i <= steps; i++) {
            double progress = i / (double) steps;
            double offset   = Math.sin(progress * cycles * 2 * Math.PI) * amplitude * (1 - progress);
            final double tx = offset;
            shake.getKeyFrames().add(new KeyFrame(
                    Duration.millis(i * stepTime),
                    e -> node.setTranslateX(tx)
            ));
        }

        shake.getKeyFrames().add(new KeyFrame(
                Duration.millis(millis),
                e -> node.setTranslateX(0)
        ));

        shake.play();
    }

    /** Correct move → player action shake */
    private void shakePlayer() {
        shakeNode(playerBox, 5.0, 3, 320);
    }

    /** Wrong move → enemy reaction shake */
    private void shakeEnemy() {
        shakeNode(enemyBox, 5.0, 3, 320);
    }

    // =========================================================================
    // INVERSION COUNTING  (used to judge move quality)
    // =========================================================================

    /**
     * Counts inversions in the given data array using the current sort mode.
     * Locked tiles (whose value is unknown to the player) are included in the
     * count because their underlying value still participates in sorting.
     */
    private int countInversions(String[] data) {
        int inv = 0;
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = i + 1; j < data.length; j++) {
                int vi = currentValues.get(data[i]);
                int vj = currentValues.get(data[j]);
                if (currentMode == SortMode.ASCENDING  && vi > vj) inv++;
                if (currentMode == SortMode.DESCENDING && vi < vj) inv++;
            }
        }
        return inv;
    }

    /**
     * Returns the inversion count that would result from swapping indices a and b
     * WITHOUT modifying the real array.
     */
    private int inversionsAfterSwap(int a, int b) {
        String[] copy = Arrays.copyOf(levelData, levelData.length);
        String tmp = copy[a]; copy[a] = copy[b]; copy[b] = tmp;
        return countInversions(copy);
    }

    /**
     * A swap is a CORRECT (beneficial) move only if it strictly reduces
     * the inversion count — i.e. moves the array closer to fully sorted.
     */
    private boolean isGoodSwap(int a, int b) {
        return inversionsAfterSwap(a, b) < countInversions(levelData);
    }

    // =========================================================================
    // GAME OVER
    // =========================================================================

    private void triggerGameOver(boolean won) {
        gameOver = true;
        if (gameLoop != null) gameLoop.stop();
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);

        if (won) SaveManager.unlockNext("stage5");

        showEndOverlay(won);
    }

    private void showEndOverlay(boolean won) {
        String accentColor = won ? C_PLAYER_GREEN : C_ENEMY_RED;

        Rectangle backdrop = new Rectangle();
        backdrop.widthProperty().bind(battleArea.widthProperty());
        backdrop.heightProperty().bind(battleArea.heightProperty());
        backdrop.setFill(Color.web("#000000", 0.78));

        Label eyebrow = new Label(won ? "RESULT — VICTORY" : "RESULT — DEFEAT");
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 8));
        eyebrow.setTextFill(Color.web(C_META_TEXT));
        eyebrow.setStyle("-fx-letter-spacing: 2;");

        Label titleLabel = new Label(won ? "Victory" : "Defeat");
        titleLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.web(won ? C_COMPLETION : C_ENEMY_RED));
        titleLabel.setEffect(new DropShadow(14, Color.web(won ? C_COMPLETION : C_ENEMY_RED, 0.35)));

        Label subLabel = new Label(won ? "All secrets revealed." : "The enemy overcame you.");
        subLabel.setFont(Font.font("Georgia", FontPosture.ITALIC, 13));
        subLabel.setTextFill(Color.web(C_COMPLETE_TEXT));

        Rectangle divider = new Rectangle(260, 1);
        divider.setFill(Color.web(C_BAND_DIVIDER));

        Button playAgainBtn = makeOverlayButton("↺  Play Again", accentColor);
        playAgainBtn.setOnAction(e -> restartGame());

        Label hint = new Label("click backdrop to dismiss");
        hint.setFont(Font.font("Courier New", 9));
        hint.setTextFill(Color.web(C_META_TEXT));

        VBox popup = new VBox(16, eyebrow, titleLabel, subLabel, divider);

        if (won) {
            Button nextStageBtn = makeOverlayButton("▶  Next Stage", C_COMPLETION);
            nextStageBtn.setOnAction(e -> navigateToNextStage());
            popup.getChildren().add(nextStageBtn);
        }

        popup.getChildren().addAll(playAgainBtn, hint);
        popup.setAlignment(Pos.CENTER);
        popup.setMaxWidth(400);
        popup.setStyle(
                "-fx-background-color: " + C_CARD_BG + ";" +
                        "-fx-padding: 32 36 28 36;" +
                        "-fx-border-color: " + accentColor + ";" +
                        "-fx-border-width: 1;-fx-border-radius: 5;-fx-background-radius: 5;"
        );
        popup.setEffect(new DropShadow(24, Color.web(accentColor, 0.75)));

        StackPane centred = new StackPane(backdrop, popup);
        AnchorPane.setTopAnchor(centred,    0.0);
        AnchorPane.setBottomAnchor(centred, 0.0);
        AnchorPane.setLeftAnchor(centred,   0.0);
        AnchorPane.setRightAnchor(centred,  0.0);

        backdrop.setOnMouseClicked(e -> battleArea.getChildren().remove(centred));
        popup.setOnMouseClicked(e -> e.consume());
        battleArea.getChildren().add(centred);

        FadeTransition ft = new FadeTransition(Duration.millis(350), popup);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    private void navigateToNextStage() {
        Rectangle blackout = new Rectangle();
        blackout.widthProperty().bind(battleArea.widthProperty());
        blackout.heightProperty().bind(battleArea.heightProperty());
        blackout.setFill(Color.BLACK);
        blackout.setOpacity(0);
        AnchorPane.setTopAnchor(blackout,    0.0);
        AnchorPane.setBottomAnchor(blackout, 0.0);
        AnchorPane.setLeftAnchor(blackout,   0.0);
        AnchorPane.setRightAnchor(blackout,  0.0);
        battleArea.getChildren().add(blackout);

        FadeTransition toBlack = new FadeTransition(Duration.millis(600), blackout);
        toBlack.setFromValue(0.0);
        toBlack.setToValue(1.0);
        toBlack.setOnFinished(ev -> {
            try {
                URL resource = getClass().getResource(
                        "/com/example/kaiajourneythroughgrief/menu.fxml");
                if (resource == null) {
                    System.err.println("[StageFive] menu.fxml not found");
                    return;
                }
                FXMLLoader loader = new FXMLLoader(resource);
                Scene scene = new Scene(loader.load());
                ((Stage) battleArea.getScene().getWindow()).setScene(scene);
            } catch (Exception ex) {
                System.err.println("[StageFive] Failed to load menu: " + ex.getMessage());
            }
        });
        toBlack.play();
    }

    private Button makeOverlayButton(String label, String accentColor) {
        Button btn = new Button(label);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        btn.setTextFill(Color.web(C_ACTIVE_TITLE));
        String base  = "-fx-background-color: transparent;"
                + "-fx-border-color: " + C_BAND_DIVIDER + ";"
                + "-fx-border-width: 1;-fx-border-radius: 5;"
                + "-fx-padding: 8 28;-fx-cursor: hand;";
        String hover = "-fx-background-color: " + C_BAND_DIVIDER + ";"
                + "-fx-border-color: " + accentColor + ";"
                + "-fx-border-width: 1;-fx-border-radius: 5;"
                + "-fx-padding: 8 28;-fx-cursor: hand;";
        btn.setStyle(base);
        btn.setOnMouseEntered(e -> btn.setStyle(hover));
        btn.setOnMouseExited(e  -> btn.setStyle(base));
        return btn;
    }

    // =========================================================================
    // RESTART
    // =========================================================================

    private void restartGame() {
        battleArea.getChildren().removeIf(n -> n instanceof StackPane);
        gameOver        = false;
        lastNanoTime    = -1;
        arraysSolved    = 0;
        correctsPending = 0;
        setMeter(0.5);
        setupMouseHandlers();
        generateRandomLevel();
        drawArray();
        startGameLoop();
    }

    // =========================================================================
    // SORTING CANVAS
    // =========================================================================

    private void buildSortingCanvas() {
        canvas = new Canvas(800, 130);
        gc     = canvas.getGraphicsContext2D();

        generateRandomLevel();
        recalcLayout();
        drawArray();
        setupMouseHandlers();

        bottomPanel.getChildren().add(canvas);

        bottomPanel.widthProperty().addListener((obs, oldW, newW) -> {
            canvas.setWidth(newW.doubleValue());
            recalcLayout();
            drawArray();
        });
    }

    // =========================================================================
    // LEVEL GENERATION
    // =========================================================================

    private void generateRandomLevel() {
        levelData     = new String[NUMBER_COUNT];
        currentValues = new HashMap<>();
        for (int i = 0; i < NUMBER_COUNT; i++) {
            int num    = random.nextInt(90) + 10;
            String key = num + "_" + i;
            levelData[i] = key;
            currentValues.put(key, num);
        }
        currentMode = random.nextBoolean() ? SortMode.ASCENDING : SortMode.DESCENDING;

        tileIsLocked = new boolean[NUMBER_COUNT];
        int lockedCount = Math.min(LOCKED_TILES_INITIAL, NUMBER_COUNT);
        for (int i = 0; i < lockedCount; i++) {
            int idx = random.nextInt(NUMBER_COUNT);
            tileIsLocked[idx] = true;
        }
        correctsPending = 0;
    }

    // =========================================================================
    // SORT LOGIC
    // =========================================================================

    private int getValue(int index) { return currentValues.get(levelData[index]); }

    private void swapElements(int i, int j) {
        String t = levelData[i]; levelData[i] = levelData[j]; levelData[j] = t;
        boolean tmp = tileIsLocked[i]; tileIsLocked[i] = tileIsLocked[j]; tileIsLocked[j] = tmp;
    }

    private boolean isSorted() {
        revealCorrectlyPositionedLockedTiles();
        for (int i = 1; i < NUMBER_COUNT; i++) {
            if (currentMode == SortMode.ASCENDING  && getValue(i-1) > getValue(i)) return false;
            if (currentMode == SortMode.DESCENDING && getValue(i-1) < getValue(i)) return false;
        }
        return true;
    }

    private boolean isPositionCorrect(int pos) {
        int[] sorted = sortedSnapshot(currentMode == SortMode.ASCENDING);
        return getValue(pos) == sorted[pos];
    }

    private int[] sortedSnapshot(boolean ascending) {
        int[] vals = new int[NUMBER_COUNT];
        for (int i = 0; i < NUMBER_COUNT; i++) vals[i] = getValue(i);
        Arrays.sort(vals);
        if (!ascending) {
            for (int i = 0, j = vals.length - 1; i < j; i++, j--) {
                int tmp = vals[i]; vals[i] = vals[j]; vals[j] = tmp;
            }
        }
        return vals;
    }

    private int countCorrectAfterSwap(int a, int b) {
        int c = 0;
        if (isPositionCorrect(a)) c++;
        if (isPositionCorrect(b)) c++;
        return c;
    }

    private String themeColor() {
        return currentMode == SortMode.DESCENDING ? C_DESCENDING : C_ASCENDING;
    }

    // =========================================================================
    // LOCKED TILE REVEALS
    // =========================================================================

    private void processLockedTileReveals(int correctCount) {
        if (correctCount <= 0) return;

        correctsPending += correctCount;

        int autoRevealed = revealCorrectlyPositionedLockedTiles();
        correctsPending += autoRevealed;

        while (correctsPending >= CORRECT_COUNT_TO_UNLOCK) {
            correctsPending -= CORRECT_COUNT_TO_UNLOCK;
            unlockRandomTile();
        }
    }

    private void unlockRandomTile() {
        int lockedIndex = -1;
        for (int i = 0; i < NUMBER_COUNT; i++) {
            if (tileIsLocked[i]) { lockedIndex = i; break; }
        }
        if (lockedIndex == -1) return;
        shakeAndRevealTile(lockedIndex);
    }

    private int revealCorrectlyPositionedLockedTiles() {
        int revealedCount = 0;
        for (int i = 0; i < NUMBER_COUNT; i++) {
            if (tileIsLocked[i] && isPositionCorrect(i)) {
                tileIsLocked[i] = false;
                revealedCount++;
            }
        }
        return revealedCount;
    }

    private void shakeAndRevealTile(int index) {
        double shakeAmount = 4.0;
        int    shakeFrames = 10;
        Duration shakeDuration = Duration.millis(200);

        Timeline shakeTimeline = new Timeline();

        for (int i = 0; i <= shakeFrames; i++) {
            final int currentFrame = i;
            shakeTimeline.getKeyFrames().add(new KeyFrame(
                    shakeDuration.multiply(i / (double) shakeFrames),
                    e -> {
                        drawCanvasHeader();
                        for (int j = 0; j < NUMBER_COUNT; j++) {
                            if (j == index) continue;
                            double[] pos = boxPosition(j);
                            drawBox(pos[0], pos[1], getString(j), isPositionCorrect(j), false);
                        }
                        double[] shakePos   = boxPosition(index);
                        double   shakeOffset = (currentFrame % 2 == 0) ? shakeAmount : -shakeAmount;
                        drawBox(shakePos[0] + shakeOffset, shakePos[1], getString(index), false, false);
                    }
            ));
        }

        shakeTimeline.setOnFinished(e -> {
            tileIsLocked[index] = false;
            drawArray();
        });

        shakeTimeline.play();
    }

    private String getString(int index) {
        if (tileIsLocked[index]) return "?";
        return String.valueOf(getValue(index));
    }

    // =========================================================================
    // LAYOUT
    // =========================================================================

    private void recalcLayout() {
        double usable = canvas.getWidth() - MARGIN_X * 2;
        maxPerRow = Math.max(1, (int) ((usable + SPACING) / (80 + SPACING)));
        int itemsInRow = Math.min(maxPerRow, NUMBER_COUNT);
        boxWidth  = (usable - SPACING * (itemsInRow - 1)) / itemsInRow;
    }

    private double[] boxPosition(int index) {
        int row = index / maxPerRow;
        int col = index % maxPerRow;

        int    totalRows = (int) Math.ceil(NUMBER_COUNT / (double) maxPerRow);
        double totalH    = totalRows * BOX_HEIGHT + (totalRows - 1) * SPACING;
        double startY    = (canvas.getHeight() - totalH) / 2.0 + 16;

        int itemsInRow = Math.min(maxPerRow, NUMBER_COUNT - row * maxPerRow);
        double rowWidth = itemsInRow * boxWidth + (itemsInRow - 1) * SPACING;
        double startX   = (canvas.getWidth() - rowWidth) / 2.0;

        return new double[]{
                startX + col * (boxWidth + SPACING),
                startY + row * (BOX_HEIGHT + SPACING)
        };
    }

    private int hitTest(double x, double y) {
        for (int i = 0; i < NUMBER_COUNT; i++) {
            double[] pos = boxPosition(i);
            if (x >= pos[0] && x <= pos[0] + boxWidth &&
                    y >= pos[1] && y <= pos[1] + BOX_HEIGHT) return i;
        }
        return -1;
    }

    private int nearestIndex(double x, double y) {
        int    best  = -1;
        double bestD = Double.MAX_VALUE;
        for (int i = 0; i < NUMBER_COUNT; i++) {
            double[] pos = boxPosition(i);
            double   cx  = pos[0] + boxWidth   / 2;
            double   cy  = pos[1] + BOX_HEIGHT / 2;
            double   d2  = (x-cx)*(x-cx) + (y-cy)*(y-cy);
            if (d2 < bestD) { bestD = d2; best = i; }
        }
        return best;
    }

    // =========================================================================
    // MOUSE HANDLERS
    // =========================================================================

    private void setupMouseHandlers() {
        canvas.setOnMousePressed(e -> {
            if (e.getButton() != MouseButton.PRIMARY || animating || gameOver) return;
            int index = hitTest(e.getX(), e.getY());
            if (index == -1) return;
            dragging    = true;
            dragIndex   = index;
            double[] pos = boxPosition(index);
            dragOffsetX = e.getX() - pos[0];
            dragOffsetY = e.getY() - pos[1];
            dragX = pos[0];
            dragY = pos[1];
        });

        canvas.setOnMouseDragged(e -> {
            if (!dragging || animating || gameOver) return;
            dragX = e.getX() - dragOffsetX;
            dragY = e.getY() - dragOffsetY;
            drawArray();
        });

        canvas.setOnMouseReleased(e -> {
            if (!dragging || animating || gameOver) return;
            dragging = false;
            int target = nearestIndex(e.getX(), e.getY());
            if (target != -1 && target != dragIndex) {
                // ── Evaluate the move BEFORE the swap is performed ────────
                boolean goodMove = isGoodSwap(dragIndex, target);
                animateSwap(dragIndex, target, goodMove);
            } else {
                dragIndex = -1;
                drawArray();
            }
        });
    }

    // =========================================================================
    // RENDERING
    // =========================================================================

    private void drawCanvasHeader() {
        gc.setFill(Color.web(C_TITLE_BAR));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setStroke(Color.web(C_BAND_DIVIDER));
        gc.setLineWidth(1);
        gc.strokeLine(0, 0, canvas.getWidth(), 0);

        gc.setFill(Color.web(C_META_TEXT));
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 8));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("OBJECTIVE", 14, 14);

        String modeLabel = currentMode == SortMode.DESCENDING ? "Sort Descending" : "Sort Ascending";
        gc.setFill(Color.web(themeColor(), 0.85));
        gc.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
        gc.fillText(modeLabel, 14, 27);

        gc.setStroke(Color.web(themeColor(), 0.4));
        gc.setLineWidth(1);
        gc.strokeLine(14, 30, 110, 30);

        if (ARRAYS_TO_WIN > 1) {
            String progress = (arraysSolved + 1) + "  /  " + ARRAYS_TO_WIN;
            gc.setFill(Color.web(C_META_TEXT));
            gc.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText(progress, canvas.getWidth() - 14, 16);
        }
    }

    private void drawArray() {
        drawCanvasHeader();
        for (int i = 0; i < NUMBER_COUNT; i++) {
            if (dragging && i == dragIndex) continue;
            double[] pos    = boxPosition(i);
            String   display = getString(i);
            boolean  correct = !tileIsLocked[i] && isPositionCorrect(i);
            drawBox(pos[0], pos[1], display, correct, false);
        }
        if (dragging && dragIndex != -1) {
            String display = getString(dragIndex);
            drawBox(dragX, dragY, display, false, true);
        }
    }

    private void drawBox(double x, double y, String text, boolean correct, boolean isDragged) {
        boolean isLocked = text.equals("?");
        String theme = isLocked ? C_LOCKED : (correct ? C_COMPLETION : themeColor());

        gc.setFill(Color.web(C_CARD_BG));
        gc.fillRoundRect(x, y, boxWidth, BOX_HEIGHT, CARD_RADIUS, CARD_RADIUS);

        gc.setStroke(Color.web(theme, isDragged ? 1.0 : (correct ? 0.55 : 0.7)));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(x, y, boxWidth, BOX_HEIGHT, CARD_RADIUS, CARD_RADIUS);

        gc.setFill(Color.web(theme, isDragged ? 1.0 : (correct ? 0.4 : 0.85)));
        gc.fillRoundRect(x + 1, y + 8, ACCENT_W, BOX_HEIGHT - 16, 2, 2);

        gc.setFill(Color.web(correct ? C_COMPLETION : (isLocked ? C_LOCKED : C_ACTIVE_TITLE)));
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, x + boxWidth / 2.0, y + BOX_HEIGHT / 2.0 + 7);

        if (correct) {
            gc.setFill(Color.web(C_COMPLETION, 0.7));
            gc.setFont(Font.font("Georgia", 11));
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText("✓", x + boxWidth - 13, y + 13);
        }
    }

    private void drawBox(double x, double y, String text) {
        drawBox(x, y, text, false, false);
    }

    // =========================================================================
    // SWAP ANIMATION
    // =========================================================================

    /**
     * Animates a swap between indices {@code from} and {@code to}.
     *
     * @param goodMove true  → swap reduces inversions (correct move):
     *                           player shakes + health gained + locked tile check
     *                 false → swap increases/keeps inversions (wrong move):
     *                           enemy shakes + health lost
     */
    private void animateSwap(int from, int to, boolean goodMove) {
        animating = true;

        double[] fp = boxPosition(from);
        double[] tp = boxPosition(to);
        String valA = getString(from);
        String valB = getString(to);

        Duration total    = Duration.millis(SWAP_MILLIS);
        Timeline timeline = new Timeline();

        for (int i = 0; i <= SWAP_FRAMES; i++) {
            double t  = easeInOutQuad(i / (double) SWAP_FRAMES);
            double ax = fp[0] + (tp[0] - fp[0]) * t,  ay = fp[1] + (tp[1] - fp[1]) * t;
            double bx = tp[0] + (fp[0] - tp[0]) * t,  by = tp[1] + (fp[1] - tp[1]) * t;
            final double fax=ax, fay=ay, fbx=bx, fby=by;

            timeline.getKeyFrames().add(new KeyFrame(
                    total.multiply(i / (double) SWAP_FRAMES),
                    e -> {
                        drawCanvasHeader();
                        for (int j = 0; j < NUMBER_COUNT; j++) {
                            if (j == from || j == to) continue;
                            double[] p = boxPosition(j);
                            drawBox(p[0], p[1], getString(j));
                        }
                        drawBox(fax, fay, valA);
                        drawBox(fbx, fby, valB);
                    }
            ));
        }

        timeline.setOnFinished(e -> {
            swapElements(from, to);
            dragIndex = -1;
            animating = false;

            if (goodMove) {
                // ── Correct move: gain health + player action shake ───────
                int correctCount = countCorrectAfterSwap(from, to);
                gainHealth(correctCount);
                processLockedTileReveals(correctCount);
                shakePlayer();
            } else {
                // ── Wrong move: lose health + enemy reaction shake ────────
                setMeter(playerMeter - LOSS_PER_WRONG);
                shakeEnemy();
                if (playerMeter <= 0.0 && !gameOver) triggerGameOver(false);
            }

            drawArray();
        });

        timeline.play();
    }

    private double easeInOutQuad(double t) {
        return t < 0.5 ? 2*t*t : 1 - Math.pow(-2*t + 2, 2) / 2;
    }
}