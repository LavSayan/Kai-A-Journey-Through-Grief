package com.example.kaiajourneythroughgrief;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;

public class LevelScreen implements Initializable {

    public static final double W = 1000;
    public static final double H = 740;

    @FXML private Pane canvas;

    private Map<String, String> LEVEL_STATUS;

    // ── Stage → FXML mapping ───────────────────────────────────────────────
    private static final Map<String, String> STAGE_FXML = new LinkedHashMap<>() {{
        put("stage1", "stage_one.fxml");
        put("stage2", "stage_two.fxml");
        put("stage3", "stage_three.fxml");
        put("stage4", "stage_four.fxml");
        put("stage5", "stage_five.fxml");
    }};

    private static final double BAND_H = H / 5.0;
    private static final double[] NODE_X = { 270, 730, 270, 730, 500 };
    private static final double[] NODE_Y = new double[5];
    static {
        for (int i = 0; i < 5; i++) NODE_Y[i] = H - BAND_H * (i + 0.5);
    }

    private static final Object[][] STAGES = {
            { "stage1", "I",   "The Forest Frozen in Fog",    "DENIAL",
                    "#ADD8E6", "#1A4A5A", "Preservers",
                    "Ice constructs that freeze memories in amber stillness." },
            { "stage2", "II",  "The City of Shattered Glass", "ANGER",
                    "#DC143C", "#5A0010", "Reflections",
                    "Mirror-clones that hurl your rage back at you." },
            { "stage3", "III", "The Temple of Endless Doors", "BARGAINING",
                    "#DAA520", "#5A4000", "The Tethered",
                    "Phantom chains from alternate realities left unchosen." },
            { "stage4", "IV",  "The Ocean of Still Water",    "DEPRESSION",
                    "#6A2FBF", "#18002E", "The Drowned",
                    "Waterlogged figures dragging you into silent depths." },
            { "stage5", "V",   "The Garden at Dawn",          "ACCEPTANCE",
                    "#FFD700", "#5A4400", "Thorned Guardians",
                    "Plant creatures that test if you are truly ready." },
    };

    // ── Guard: prevents double-clicking during a transition ────────────────
    private boolean transitioning = false;

    // =========================================================================
    // INIT
    // =========================================================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load saved progress first
        LEVEL_STATUS = SaveManager.load();

        // Music path matches Menu.java exactly
        MusicPlayer.getInstance().play("assets/bgmusic.mp3");

        drawBackground();
        drawBiomeBands();
        drawBandDividers();
        drawConnectorPath();
        drawStageNodes();
        drawTitleBar();
        drawLegend();
    }

    // =========================================================================
    // CLICK HANDLER
    // =========================================================================

    private void handleClick(String stageId, int stageNum,
                             String title, String stageName,
                             String colorHex, String darkHex,
                             String enemy, String desc,
                             String status) {
        if (transitioning) return;

        switch (status) {
            case "locked"   -> showLockedCard(stageNum, title, stageName, colorHex, enemy);
            case "unlocked" -> showIntroCard(stageId, stageNum, title, stageName,
                    colorHex, darkHex, enemy, desc);
            case "complete" -> showCompleteCard(stageId, stageNum, title, stageName,
                    colorHex, darkHex, enemy, desc);
        }
    }

    // =========================================================================
    // LOCKED CARD
    // =========================================================================

    /**
     * Shown when the player clicks a sealed stage.
     * Displays a muted, locked-style card — no enter button, only dismiss.
     */
    private void showLockedCard(int stageNum, String title, String stageName,
                                String colorHex, String enemy) {
        // Use a heavily dimmed version of the theme color for locked state
        String lockedColor = "#2A2A44";

        Rectangle backdrop = new Rectangle(W, H);
        backdrop.setFill(Color.web("#000000", 0.82));

        Circle orb = new Circle(W / 2, H / 2, 280);
        orb.setFill(Color.web(lockedColor, 0.12));
        orb.setEffect(new GaussianBlur(80));

        double cardW = 420;
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(cardW);
        card.setPrefWidth(cardW);
        card.setStyle(
                "-fx-background-color: #07070E;" +
                        "-fx-border-color: " + lockedColor + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 36 44 32 44;"
        );
        card.setEffect(new DropShadow(20, Color.web(lockedColor, 0.3)));

        // Lock icon
        Label lockIcon = new Label("🔒");
        lockIcon.setFont(Font.font("Georgia", 32));

        // Eyebrow
        Label eyebrow = new Label("STAGE  " + toRoman(stageNum) + "  —  " + stageName);
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        eyebrow.setTextFill(Color.web(lockedColor));
        eyebrow.setStyle("-fx-letter-spacing: 3;");

        // Title
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Georgia", FontPosture.ITALIC, 22));
        titleLbl.setTextFill(Color.web("#2A2A44"));
        titleLbl.setWrapText(true);
        titleLbl.setMaxWidth(cardW - 88);
        titleLbl.setAlignment(Pos.CENTER);

        // Divider
        Rectangle divider = new Rectangle(cardW - 88, 1);
        divider.setFill(Color.web(lockedColor, 0.4));

        // Sealed message
        String prevTitle = stageNum > 1 ? (String) STAGES[stageNum - 2][2] : null;
        String sealMsg = prevTitle != null
                ? "Complete  \"" + prevTitle + "\"\nto unseal this region."
                : "This is the beginning of the journey.";
        Label sealLbl = new Label(sealMsg);
        sealLbl.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
        sealLbl.setTextFill(Color.web("#3A3A56"));
        sealLbl.setWrapText(true);
        sealLbl.setMaxWidth(cardW - 88);
        sealLbl.setAlignment(Pos.CENTER);

        // Enemy hint (dimmed)
        Label enemyLbl = new Label("⚔  " + enemy);
        enemyLbl.setFont(Font.font("Courier New", 9));
        enemyLbl.setTextFill(Color.web("#1E1E30"));

        // Dismiss button
        Button dismissBtn = makeIntroButton("✕  RETURN", lockedColor, false);

        card.getChildren().addAll(lockIcon, eyebrow, titleLbl, divider, sealLbl, enemyLbl, dismissBtn);
        card.setLayoutX(W / 2 - cardW / 2);

        Pane overlay = new Pane(backdrop, orb, card);
        card.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            if (newB.getHeight() > 0) card.setLayoutY((H - newB.getHeight()) / 2.0);
        });
        canvas.getChildren().add(overlay);

        overlay.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(280), overlay);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        dismissBtn.setOnAction(e -> dismissOverlay(overlay));
        backdrop.setOnMouseClicked(e -> dismissOverlay(overlay));
    }

    // =========================================================================
    // COMPLETE CARD
    // =========================================================================

    /**
     * Shown when the player clicks a completed stage.
     * Asks whether to replay, with a gold checkmark aesthetic.
     * Pressing REPLAY opens the intro card; CANCEL dismisses.
     */
    private void showCompleteCard(String stageId, int stageNum,
                                  String title, String stageName,
                                  String colorHex, String darkHex,
                                  String enemy, String desc) {
        String completeColor = "#00FF88";   // completion green

        Rectangle backdrop = new Rectangle(W, H);
        backdrop.setFill(Color.web("#000000", 0.82));

        Circle orb = new Circle(W / 2, H / 2, 300);
        orb.setFill(Color.web(completeColor, 0.05));
        orb.setEffect(new GaussianBlur(80));

        double cardW = 460;
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(cardW);
        card.setPrefWidth(cardW);
        card.setStyle(
                "-fx-background-color: #07070E;" +
                        "-fx-border-color: " + completeColor + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 36 44 32 44;"
        );
        card.setEffect(new DropShadow(28, Color.web(completeColor, 0.35)));

        // Check icon
        Label checkIcon = new Label("✓");
        checkIcon.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        checkIcon.setTextFill(Color.web(completeColor, 0.9));
        checkIcon.setEffect(new DropShadow(10, Color.web(completeColor, 0.4)));

        // Eyebrow
        Label eyebrow = new Label("STAGE  " + toRoman(stageNum) + "  —  " + stageName);
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        eyebrow.setTextFill(Color.web(completeColor, 0.55));
        eyebrow.setStyle("-fx-letter-spacing: 3;");

        // Title — muted since it's complete
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Georgia", FontPosture.ITALIC, 22));
        titleLbl.setTextFill(Color.web("#5E5E78"));
        titleLbl.setWrapText(true);
        titleLbl.setMaxWidth(cardW - 88);
        titleLbl.setAlignment(Pos.CENTER);

        // Divider
        Rectangle divider = new Rectangle(cardW - 88, 1);
        divider.setFill(Color.web(completeColor, 0.2));

        // Replay question
        Label replayLbl = new Label("You have already walked this path.\nWould you like to return?");
        replayLbl.setFont(Font.font("Georgia", FontPosture.ITALIC, 13));
        replayLbl.setTextFill(Color.web("#5E5E78"));
        replayLbl.setWrapText(true);
        replayLbl.setMaxWidth(cardW - 88);
        replayLbl.setAlignment(Pos.CENTER);

        // Enemy line
        Label enemyLbl = new Label("⚔  " + enemy);
        enemyLbl.setFont(Font.font("Courier New", 9));
        enemyLbl.setTextFill(Color.web("#3A3A56"));

        // Buttons
        Button replayBtn  = makeIntroButton("↺  REPLAY STAGE", completeColor, true);
        Button cancelBtn  = makeIntroButton("✕  CANCEL",       "#3A3A56",     false);

        VBox btnRow = new VBox(10, replayBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(checkIcon, eyebrow, titleLbl, divider,
                replayLbl, enemyLbl, btnRow);
        card.setLayoutX(W / 2 - cardW / 2);

        Pane overlay = new Pane(backdrop, orb, card);
        card.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            if (newB.getHeight() > 0) card.setLayoutY((H - newB.getHeight()) / 2.0);
        });
        canvas.getChildren().add(overlay);

        overlay.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(280), overlay);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        replayBtn.setOnAction(e -> {
            // Dismiss this card first, then open the intro card
            dismissOverlay(overlay);
            showIntroCard(stageId, stageNum, title, stageName,
                    colorHex, darkHex, enemy, desc);
        });
        cancelBtn.setOnAction(e -> dismissOverlay(overlay));
        backdrop.setOnMouseClicked(e -> dismissOverlay(overlay));
    }

    // =========================================================================
    // STAGE INTRO CARD
    // =========================================================================

    private void showIntroCard(String stageId, int stageNum,
                               String title, String stageName,
                               String colorHex, String darkHex,
                               String enemy, String desc) {

        // ── Full-screen dark backdrop ──────────────────────────────────────
        Rectangle backdrop = new Rectangle(W, H);
        backdrop.setFill(Color.web("#000000", 0.82));
        backdrop.setEffect(new GaussianBlur(0)); // no blur on backdrop itself

        // ── Ambient color orb behind the card ─────────────────────────────
        Circle orb = new Circle(W / 2, H / 2, 320);
        orb.setFill(Color.web(colorHex, 0.07));
        orb.setEffect(new GaussianBlur(80));

        // ── Card container ─────────────────────────────────────────────────
        double cardW = 480;
        VBox card = new VBox(18);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(cardW);
        card.setPrefWidth(cardW);
        card.setStyle(
                "-fx-background-color: #07070E;" +
                        "-fx-border-color: " + colorHex + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 40 44 36 44;"
        );
        card.setEffect(new DropShadow(40, Color.web(colorHex, 0.45)));

        // Stage number eyebrow — Courier New
        Label eyebrow = new Label("STAGE  " + toRoman(stageNum) + "  —  " + stageName);
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        eyebrow.setTextFill(Color.web(colorHex, 0.7));
        eyebrow.setStyle("-fx-letter-spacing: 3;");

        // Stage title — Georgia Bold
        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        titleLbl.setTextFill(Color.web("#D8D8EC"));
        titleLbl.setWrapText(true);
        titleLbl.setMaxWidth(cardW - 88);
        titleLbl.setAlignment(Pos.CENTER);
        titleLbl.setEffect(new DropShadow(12, Color.web("#000000", 0.9)));

        // Divider
        Rectangle divider = new Rectangle(cardW - 88, 1);
        divider.setFill(Color.web(colorHex, 0.3));

        // Enemy eyebrow — Courier New
        Label enemyEyebrow = new Label("ENEMIES");
        enemyEyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 8));
        enemyEyebrow.setTextFill(Color.web("#3A3A56"));
        enemyEyebrow.setStyle("-fx-letter-spacing: 2;");

        // Enemy name — Georgia
        Label enemyName = new Label("⚔  " + enemy);
        enemyName.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        enemyName.setTextFill(Color.web(colorHex, 0.85));

        // Enemy description — Georgia Italic
        Label enemyDesc = new Label(desc);
        enemyDesc.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
        enemyDesc.setTextFill(Color.web("#5E5E78"));
        enemyDesc.setWrapText(true);
        enemyDesc.setMaxWidth(cardW - 88);
        enemyDesc.setAlignment(Pos.CENTER);

        // ── Buttons ────────────────────────────────────────────────────────
        Button enterBtn  = makeIntroButton("▶  ENTER STAGE", colorHex, true);
        Button cancelBtn = makeIntroButton("✕  CANCEL",      "#3A3A56", false);

        VBox btnRow = new VBox(10, enterBtn, cancelBtn);
        btnRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(
                eyebrow, titleLbl, divider,
                enemyEyebrow, enemyName, enemyDesc,
                btnRow
        );

        // ── Position card centered on screen ──────────────────────────────
        // We add to canvas as a Pane overlay using layout coordinates.
        card.setLayoutX(W / 2 - cardW / 2);
        card.setLayoutY(0); // will shift down after layout pass via listener

        // Wrap everything in a container pane
        Pane overlay = new Pane(backdrop, orb, card);

        // Center card vertically after its height is known
        card.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            if (newB.getHeight() > 0)
                card.setLayoutY((H - newB.getHeight()) / 2.0);
        });

        canvas.getChildren().add(overlay);

        // ── Fade the overlay IN ────────────────────────────────────────────
        overlay.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(280), overlay);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // ── ENTER button action ────────────────────────────────────────────
        enterBtn.setOnAction(e -> {
            if (transitioning) return;
            transitioning = true;

            // 1. Fade card out quickly
            FadeTransition cardOut = new FadeTransition(Duration.millis(200), overlay);
            cardOut.setToValue(0.0);

            // 2. Fade entire canvas to black
            Rectangle blackout = new Rectangle(W, H, Color.BLACK);
            blackout.setOpacity(0);
            canvas.getChildren().add(blackout);

            FadeTransition toBlack = new FadeTransition(Duration.millis(600), blackout);
            toBlack.setFromValue(0.0);
            toBlack.setToValue(1.0);

            // 3. Load the stage after blackout completes
            SequentialTransition seq = new SequentialTransition(
                    cardOut,
                    toBlack,
                    new PauseTransition(Duration.millis(120))
            );
            seq.setOnFinished(ev -> loadStage(stageId));
            seq.play();
        });

        // ── CANCEL button action ───────────────────────────────────────────
        cancelBtn.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), overlay);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> canvas.getChildren().remove(overlay));
            fadeOut.play();
        });

        // Clicking the backdrop also cancels
        backdrop.setOnMouseClicked(e -> cancelBtn.fire());
    }

    // ── Intro card button factory ──────────────────────────────────────────

    private Button makeIntroButton(String label, String colorHex, boolean isPrimary) {
        Button btn = new Button(label);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        btn.setTextFill(Color.web(isPrimary ? colorHex : "#3A3A56"));
        btn.setPrefWidth(220);

        String base = "-fx-background-color: transparent;" +
                "-fx-border-color: " + colorHex + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 4;" +
                "-fx-padding: 10 0;" +
                "-fx-cursor: hand;";
        String hover = "-fx-background-color: " + colorHex + "22;" +
                "-fx-border-color: " + colorHex + ";" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 4;" +
                "-fx-padding: 10 0;" +
                "-fx-cursor: hand;";

        btn.setStyle(base);
        btn.setOnMouseEntered(e -> {
            btn.setStyle(hover);
            btn.setTextFill(Color.web(isPrimary ? "#FFFFFF" : colorHex));
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(base);
            btn.setTextFill(Color.web(isPrimary ? colorHex : "#3A3A56"));
        });
        return btn;
    }

    // =========================================================================
    // FADE-TO-BLACK + LOAD STAGE
    // =========================================================================

    /**
     * Loads the FXML for the given stage and replaces the scene.
     * Only called after the fade-to-black completes.
     */
    private void loadStage(String stageId) {
        String fxmlFile = STAGE_FXML.get(stageId);
        if (fxmlFile == null) {
            System.err.println("[LevelScreen] No FXML mapped for: " + stageId);
            transitioning = false;
            return;
        }
        try {
            URL resource = getClass().getResource(fxmlFile);
            if (resource == null) {
                System.err.println("[LevelScreen] FXML not found: " + fxmlFile);
                transitioning = false;
                return;
            }
            // Fade music out as we enter the battle stage
            MusicPlayer.getInstance().fadeOutAndStop(500);

            FXMLLoader loader = new FXMLLoader(resource);
            Scene scene = new Scene(loader.load());
            ((Stage) canvas.getScene().getWindow()).setScene(scene);
        } catch (Exception e) {
            System.err.println("[LevelScreen] Failed to load " + fxmlFile + ": " + e.getMessage());
            transitioning = false;
        }
    }

    // =========================================================================
    // NOTICE POPUP  (locked stages)
    // =========================================================================

    // =========================================================================
    // SHARED OVERLAY HELPERS
    // =========================================================================

    /** Fades out and removes any overlay pane from the canvas. */
    private void dismissOverlay(Pane overlay) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), overlay);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(ev -> canvas.getChildren().remove(overlay));
        fadeOut.play();
    }

    // =========================================================================
    // UTIL
    // =========================================================================

    private String toRoman(int n) {
        return switch (n) {
            case 1 -> "I"; case 2 -> "II"; case 3 -> "III";
            case 4 -> "IV"; case 5 -> "V"; default -> String.valueOf(n);
        };
    }

    // =========================================================================
    // DRAWING
    // =========================================================================

    private void drawBackground() {
        Rectangle bg = new Rectangle(W, H);
        bg.setFill(Color.web("#07070F"));
        canvas.getChildren().add(bg);

        Random rng = new Random(7);
        for (int i = 0; i < 900; i++) {
            Rectangle g = new Rectangle(rng.nextInt(2) + 1, rng.nextInt(2) + 1);
            g.setX(rng.nextDouble() * W);
            g.setY(rng.nextDouble() * H);
            g.setFill(Color.web("#FFFFFF", 0.008 + rng.nextDouble() * 0.016));
            canvas.getChildren().add(g);
        }

        Rectangle vig = new Rectangle(W, H);
        vig.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0,  Color.web("#000000", 0.6)),
                new Stop(0.12, Color.TRANSPARENT),
                new Stop(0.88, Color.TRANSPARENT),
                new Stop(1.0,  Color.web("#000000", 0.6))
        ));
        canvas.getChildren().add(vig);
    }

    private void drawBiomeBands() {
        for (int i = 0; i < STAGES.length; i++) {
            String stageId   = (String) STAGES[i][0];
            String colorHex  = (String) STAGES[i][4];
            String stageName = (String) STAGES[i][3];
            String status    = LEVEL_STATUS.getOrDefault(stageId, "locked");
            double bandY     = H - (i + 1) * BAND_H;

            Rectangle band = new Rectangle(W, BAND_H);
            band.setX(0); band.setY(bandY);
            band.setFill("locked".equals(status)
                    ? Color.web("#0A0A15", 0.97)
                    : Color.web(colorHex, "complete".equals(status) ? 0.08 : 0.14));
            canvas.getChildren().add(band);

            if (!"locked".equals(status)) {
                Rectangle sideDark = new Rectangle(W, BAND_H);
                sideDark.setX(0); sideDark.setY(bandY);
                sideDark.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0.0, Color.web("#000000", 0.5)),
                        new Stop(0.2, Color.TRANSPARENT),
                        new Stop(0.8, Color.TRANSPARENT),
                        new Stop(1.0, Color.web("#000000", 0.5))
                ));
                canvas.getChildren().add(sideDark);
            }

            if ("unlocked".equals(status)) {
                for (double ry : new double[]{ bandY, bandY + BAND_H - 2 }) {
                    Rectangle rim = new Rectangle(W, 2);
                    rim.setX(0); rim.setY(ry);
                    rim.setFill(Color.web(colorHex, 0.6));
                    rim.setEffect(new DropShadow(10, Color.web(colorHex, 0.5)));
                    canvas.getChildren().add(rim);
                }
            }

            double wmOpacity = "locked".equals(status) ? 0.022
                    : ("complete".equals(status) ? 0.055 : 0.09);
            Label wm = new Label(stageName);
            wm.setFont(Font.font("Georgia", FontWeight.BOLD, 54));
            wm.setTextFill(Color.web("locked".equals(status) ? "#FFFFFF" : colorHex, wmOpacity));
            wm.setLayoutX(W / 2.0 - 120);
            wm.setLayoutY(bandY + BAND_H / 2.0 - 34);
            canvas.getChildren().add(wm);
        }
    }

    private void drawBandDividers() {
        for (int i = 1; i < STAGES.length; i++) {
            double y = H - i * BAND_H;
            Line div = new Line(0, y, W, y);
            div.setStroke(Color.web("#1C1C30", 0.95));
            div.setStrokeWidth(1.5);
            canvas.getChildren().add(div);

            Line shimmer = new Line(0, y, W, y);
            shimmer.setStroke(Color.web("#FFFFFF", 0.035));
            shimmer.setStrokeWidth(1.0);
            canvas.getChildren().add(shimmer);
        }
    }

    private void drawConnectorPath() {
        for (int i = 0; i < STAGES.length - 1; i++) {
            double x1 = NODE_X[i], y1 = NODE_Y[i];
            double x2 = NODE_X[i+1], y2 = NODE_Y[i+1];
            String fromStatus = LEVEL_STATUS.getOrDefault((String) STAGES[i][0], "locked");
            String fromColor  = (String) STAGES[i][4];
            boolean revealed  = !"locked".equals(fromStatus);

            double cx1 = x1 + (500 - x1) * 0.30, cy1 = y1 - BAND_H * 0.55;
            double cx2 = x2 + (500 - x2) * 0.30, cy2 = y2 + BAND_H * 0.55;

            CubicCurve ghost = makeCurve(x1,y1,cx1,cy1,cx2,cy2,x2,y2);
            ghost.setStroke(Color.web("#181828", 0.95));
            ghost.setStrokeWidth(4.0);
            ghost.getStrokeDashArray().addAll(3.0, 12.0);
            canvas.getChildren().add(ghost);

            if (revealed) {
                CubicCurve halo = makeCurve(x1,y1,cx1,cy1,cx2,cy2,x2,y2);
                halo.setStroke(Color.web(fromColor, 0.22));
                halo.setStrokeWidth(10.0);
                halo.setEffect(new DropShadow(14, Color.web(fromColor, 0.3)));
                canvas.getChildren().add(halo);

                CubicCurve road = makeCurve(x1,y1,cx1,cy1,cx2,cy2,x2,y2);
                road.setStroke(Color.web(fromColor, 0.85));
                road.setStrokeWidth(2.5);
                road.getStrokeDashArray().addAll(7.0, 5.0);
                canvas.getChildren().add(road);
            }
        }
    }

    private CubicCurve makeCurve(double x1, double y1, double cx1, double cy1,
                                 double cx2, double cy2, double x2, double y2) {
        CubicCurve c = new CubicCurve(x1,y1,cx1,cy1,cx2,cy2,x2,y2);
        c.setFill(Color.TRANSPARENT);
        return c;
    }

    private void drawStageNodes() {
        for (int i = 0; i < STAGES.length; i++) {
            buildNode(
                    (String) STAGES[i][0], i + 1,
                    (String) STAGES[i][1], (String) STAGES[i][2],
                    (String) STAGES[i][3], (String) STAGES[i][4],
                    (String) STAGES[i][5], (String) STAGES[i][6],
                    (String) STAGES[i][7],
                    LEVEL_STATUS.getOrDefault((String) STAGES[i][0], "locked"),
                    NODE_X[i], NODE_Y[i]
            );
        }
    }

    private void buildNode(String stageId, int stageNum, String roman,
                           String title, String stageName,
                           String colorHex, String darkHex,
                           String enemy, String desc,
                           String status, double cx, double cy) {
        boolean isLocked   = "locked".equals(status);
        boolean isComplete = "complete".equals(status);
        boolean isUnlocked = "unlocked".equals(status);

        if (isUnlocked) {
            Circle pulse = new Circle(cx, cy, 32);
            pulse.setFill(Color.TRANSPARENT);
            pulse.setStroke(Color.web(colorHex, 0.5));
            pulse.setStrokeWidth(1.5);
            canvas.getChildren().add(pulse);

            ScaleTransition st = new ScaleTransition(Duration.seconds(2.1), pulse);
            st.setFromX(0.85); st.setFromY(0.85);
            st.setToX(1.9);    st.setToY(1.9);
            st.setCycleCount(Animation.INDEFINITE);

            FadeTransition ft = new FadeTransition(Duration.seconds(2.1), pulse);
            ft.setFromValue(0.65); ft.setToValue(0.0);
            ft.setCycleCount(Animation.INDEFINITE);
            st.play(); ft.play();
        }

        if (isComplete) {
            Circle cring = new Circle(cx, cy, 26);
            cring.setFill(Color.TRANSPARENT);
            cring.setStroke(Color.web("#00FF88", 0.55));
            cring.setStrokeWidth(1.5);
            cring.getStrokeDashArray().addAll(4.5, 4.5);
            canvas.getChildren().add(cring);
        }

        double radius = isLocked ? 14 : (isUnlocked ? 20 : 17);
        Circle node = new Circle(cx, cy, radius);
        if (isLocked) {
            node.setFill(Color.web("#0C0C1A"));
            node.setStroke(Color.web("#242436", 0.9));
            node.setStrokeWidth(1.5);
        } else {
            node.setFill(Color.web(darkHex, 0.92));
            node.setStroke(Color.web(colorHex, isComplete ? 0.55 : 1.0));
            node.setStrokeWidth(isUnlocked ? 2.5 : 1.8);
            node.setEffect(new DropShadow(isUnlocked ? 24 : 14,
                    Color.web(colorHex, isUnlocked ? 0.75 : 0.35)));
        }
        canvas.getChildren().add(node);

        Label sym = new Label();
        if (isLocked) {
            sym.setText("🔒");
            sym.setFont(Font.font("Georgia", 10));
            sym.setLayoutX(cx - 7); sym.setLayoutY(cy - 9);
        } else if (isComplete) {
            sym.setText("✓");
            sym.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
            sym.setTextFill(Color.web("#00FF88", 0.95));
            sym.setLayoutX(cx - 7); sym.setLayoutY(cy - 11);
        } else {
            sym.setText(roman);
            sym.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
            sym.setTextFill(Color.web(colorHex));
            sym.setLayoutX(cx - roman.length() * 4.2);
            sym.setLayoutY(cy - 10);
        }
        canvas.getChildren().add(sym);

        buildInfoCard(stageNum, title, stageName, colorHex, enemy,
                status, isLocked, isComplete, isUnlocked, cx, cy);

        Circle hit = new Circle(cx, cy, radius + 14);
        hit.setFill(Color.TRANSPARENT);
        hit.setStyle(isLocked ? "-fx-cursor: default;" : "-fx-cursor: hand;");
        if (!isLocked) {
            hit.setOnMouseEntered(e -> {
                ScaleTransition s = new ScaleTransition(Duration.millis(100), node);
                s.setToX(1.25); s.setToY(1.25); s.play();
            });
            hit.setOnMouseExited(e -> {
                ScaleTransition s = new ScaleTransition(Duration.millis(100), node);
                s.setToX(1.0); s.setToY(1.0); s.play();
            });
        }
        // Pass all stage data through to the click handler
        hit.setOnMouseClicked(e ->
                handleClick(stageId, stageNum, title, stageName,
                        colorHex, darkHex, enemy, desc, status));
        canvas.getChildren().add(hit);
    }

    private void buildInfoCard(int stageNum, String title, String stageName,
                               String colorHex, String enemy,
                               String status, boolean isLocked,
                               boolean isComplete, boolean isUnlocked,
                               double cx, double cy) {
        double cardW = 215, cardH = 70;
        boolean cardRight = (NODE_X[stageNum - 1] < W / 2.0);
        double cardX = cardRight ? cx + 30 : cx - cardW - 30;
        double cardY = cy - cardH / 2.0;

        double tickX     = cardRight ? cx + 20 : cx - 20;
        double cardEdgeX = cardRight ? cardX : cardX + cardW;
        Line tick = new Line(tickX, cy, cardEdgeX, cy);
        tick.setStroke(Color.web(isLocked ? "#181828" : colorHex, isLocked ? 0.4 : 0.28));
        tick.setStrokeWidth(1.0);
        canvas.getChildren().add(tick);

        Rectangle card = new Rectangle(cardW, cardH);
        card.setX(cardX); card.setY(cardY);
        card.setArcWidth(5); card.setArcHeight(5);
        card.setFill(Color.web("#07070E", 0.92));
        card.setStroke(Color.web(isLocked ? "#161624" : colorHex,
                isLocked ? 0.7 : (isUnlocked ? 0.7 : 0.3)));
        card.setStrokeWidth(1.0);
        canvas.getChildren().add(card);

        if (!isLocked) {
            double accentX = cardRight ? cardX : cardX + cardW - 3;
            Rectangle accent = new Rectangle(3, cardH);
            accent.setX(accentX); accent.setY(cardY);
            accent.setArcWidth(3); accent.setArcHeight(3);
            accent.setFill(Color.web(colorHex, isComplete ? 0.4 : 0.85));
            canvas.getChildren().add(accent);
        }

        double textX = cardX + 12;

        Label eyebrow = new Label(stageName);
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 8));
        eyebrow.setStyle("-fx-letter-spacing: 2;");
        eyebrow.setLayoutX(textX); eyebrow.setLayoutY(cardY + 7);
        eyebrow.setTextFill(isLocked ? Color.web("#1E1E30")
                : Color.web(colorHex, isComplete ? 0.5 : 0.95));

        Label titleLbl = new Label(title);
        titleLbl.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
        titleLbl.setLayoutX(textX); titleLbl.setLayoutY(cardY + 20);
        titleLbl.setMaxWidth(cardW - 20);
        titleLbl.setWrapText(false);
        titleLbl.setTextFill(isLocked ? Color.web("#1C1C2C")
                : (isComplete ? Color.web("#5E5E78") : Color.web("#D8D8EC")));

        Label enemyLbl = new Label("⚔  " + enemy);
        enemyLbl.setFont(Font.font("Courier New", 8));
        enemyLbl.setLayoutX(textX); enemyLbl.setLayoutY(cardY + 51);
        enemyLbl.setTextFill(isLocked ? Color.web("#131320") : Color.web("#3A3A56"));

        canvas.getChildren().addAll(eyebrow, titleLbl, enemyLbl);
    }

    private void drawTitleBar() {
        // ── Background bar ────────────────────────────────────────────────
        Rectangle bar = new Rectangle(W, 36);
        bar.setFill(Color.web("#050510", 0.94));
        canvas.getChildren().add(bar);

        Line underline = new Line(0, 36, W, 36);
        underline.setStroke(Color.web("#B8860B", 0.28));
        underline.setStrokeWidth(1);
        canvas.getChildren().add(underline);

        // ── Centred title ─────────────────────────────────────────────────
        String titleText = "K A I:  A   J O U R N E Y  T H R O U G H  G R I E F";
        Font titleFont = Font.font("Georgia", FontWeight.BOLD, 15);
        Text measure = new Text(titleText);
        measure.setFont(titleFont);
        double textWidth = measure.getLayoutBounds().getWidth();

        Label title = new Label(titleText);
        title.setFont(titleFont);
        title.setTextFill(Color.web("#C4C4DC", 0.88));
        title.setLayoutX((W - textWidth) / 2.0);
        title.setLayoutY(9);
        canvas.getChildren().add(title);

        // ── Right side: Back + Exit buttons ──────────────────────────────
        // Styled exactly like the intro card buttons — same factory method,
        // same Courier New Bold, same transparent/hover pattern.
        Button backBtn = makeTitleBarButton("←  BACK",  "#3A3A56");
        Button exitBtn = makeTitleBarButton("✕  EXIT",  "#AF0000");

        backBtn.setOnAction(e -> handleTitleBarBack());
        exitBtn.setOnAction(e -> handleTitleBarExit());

        // Measure button width to right-align them inside the 36px bar
        double btnW  = 90;
        double btnH  = 22;
        double btnY  = (36 - btnH) / 2.0;
        double gap   = 8;
        double rightMargin = 14;

        backBtn.setPrefWidth(btnW);
        backBtn.setPrefHeight(btnH);
        backBtn.setLayoutX(W - rightMargin - btnW * 2 - gap);
        backBtn.setLayoutY(btnY);

        exitBtn.setPrefWidth(btnW);
        exitBtn.setPrefHeight(btnH);
        exitBtn.setLayoutX(W - rightMargin - btnW);
        exitBtn.setLayoutY(btnY);

        canvas.getChildren().addAll(backBtn, exitBtn);
    }

    /** Compact title-bar button — same design language as intro card buttons. */
    private Button makeTitleBarButton(String label, String colorHex) {
        Button btn = new Button(label);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 8));
        btn.setTextFill(Color.web(colorHex));

        String base  = "-fx-background-color: transparent;"
                + "-fx-border-color: " + colorHex + ";"
                + "-fx-border-width: 1;-fx-border-radius: 3;"
                + "-fx-padding: 3 8;-fx-cursor: hand;";
        String hover = "-fx-background-color: " + colorHex + "22;"
                + "-fx-border-color: " + colorHex + ";"
                + "-fx-border-width: 1;-fx-border-radius: 3;"
                + "-fx-padding: 3 8;-fx-cursor: hand;";

        btn.setStyle(base);
        btn.setOnMouseEntered(e -> {
            btn.setStyle(hover);
            btn.setTextFill(Color.web("#FFFFFF"));
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle(base);
            btn.setTextFill(Color.web(colorHex));
        });
        return btn;
    }

    private void handleTitleBarBack() {
        if (transitioning) return;
        transitioning = true;
        // Fade to black then navigate to menu
        Rectangle blackout = new Rectangle(W, H, Color.BLACK);
        blackout.setOpacity(0);
        canvas.getChildren().add(blackout);

        FadeTransition toBlack = new FadeTransition(Duration.millis(500), blackout);
        toBlack.setFromValue(0.0);
        toBlack.setToValue(1.0);
        toBlack.setOnFinished(e -> {
            try {
                URL resource = getClass().getResource("menu.fxml");
                if (resource == null) {
                    System.err.println("[LevelScreen] menu.fxml not found");
                    transitioning = false;
                    return;
                }
                FXMLLoader loader = new FXMLLoader(resource);
                javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
                ((Stage) canvas.getScene().getWindow()).setScene(scene);
            } catch (Exception ex) {
                System.err.println("[LevelScreen] Back failed: " + ex.getMessage());
                transitioning = false;
            }
        });
        toBlack.play();
    }

    private void handleTitleBarExit() {
        if (transitioning) return;
        transitioning = true;
        // Fade music and screen to black simultaneously, then exit
        MusicPlayer.getInstance().fadeOutAndStop(500);

        Rectangle blackout = new Rectangle(W, H, Color.BLACK);
        blackout.setOpacity(0);
        canvas.getChildren().add(blackout);

        FadeTransition toBlack = new FadeTransition(Duration.millis(500), blackout);
        toBlack.setFromValue(0.0);
        toBlack.setToValue(1.0);
        toBlack.setOnFinished(e -> javafx.application.Platform.exit());
        toBlack.play();
    }

    private void drawLegend() {
        double lx = W - 162, ly = H - 60;
        Rectangle bg = new Rectangle(150, 52);
        bg.setX(lx - 4); bg.setY(ly - 4);
        bg.setArcWidth(4); bg.setArcHeight(4);
        bg.setFill(Color.web("#050510", 0.88));
        bg.setStroke(Color.web("#181828", 0.9));
        bg.setStrokeWidth(1);
        canvas.getChildren().add(bg);

        String[][] items = {
                {"🔒", "#2A2A44", "Sealed"},
                {"▶",  "#ADD8E6", "Current stage"},
                {"✓",  "#00CC77", "Completed"},
        };
        for (int i = 0; i < items.length; i++) {
            Label icon = new Label(items[i][0]);
            icon.setFont(Font.font("Georgia", 11));
            icon.setTextFill(Color.web(items[i][1]));
            icon.setLayoutX(lx); icon.setLayoutY(ly + i * 16);

            Label txt = new Label(items[i][2]);
            txt.setFont(Font.font("Georgia", FontPosture.ITALIC, 10));
            txt.setTextFill(Color.web("#383856"));
            txt.setLayoutX(lx + 20); txt.setLayoutY(ly + i * 16 + 1);

            canvas.getChildren().addAll(icon, txt);
        }
    }
}