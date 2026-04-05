package com.example.kaiajourneythroughgrief;

import javafx.animation.*;
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
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

// Import your dialogue class
import com.example.kaiajourneythroughgrief.dialogues.Dialogue1;
import com.example.kaiajourneythroughgrief.dialogues.Dialogue2;


import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class Menu implements Initializable {

    // ── Canvas dimensions — matches the rest of the game ──────────────────
    public static final double W = 1000;
    public static final double H = 740;

    @FXML private Pane canvas;

    // ── Design system constants ────────────────────────────────────────────
    private static final String C_BG          = "#07070F";
    private static final String C_TITLE_BAR   = "#050510";
    private static final String C_BAND_DIV    = "#1C1C30";
    private static final String C_TITLE_TEXT  = "#C4C4DC";
    private static final String C_META        = "#3A3A56";
    private static final String C_ACTIVE      = "#D8D8EC";
    private static final String C_ACCENT_GOLD = "#B8860B";
    private static final String C_COMPLETION  = "#00FF88";

    // Stage theme colors (used for ambient accents)
    private static final String[] STAGE_COLORS = {
            "#ADD8E6", "#DC143C", "#DAA520", "#6A2FBF", "#FFD700"
    };

    // =========================================================================
    // INIT
    // =========================================================================

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Start background music — no-op if already playing from a previous screen
        MusicPlayer.getInstance().play("assets/bgmusic.mp3");

        drawBackground();
        drawAmbientOrbs();
        drawDividerLines();
        drawTitle();
        drawMenuButtons();
        drawVersionTag();
    }

    // =========================================================================
    // BACKGROUND
    // =========================================================================

    private void drawBackground() {
        // Base fill
        Rectangle bg = new Rectangle(W, H);
        bg.setFill(Color.web(C_BG));
        canvas.getChildren().add(bg);

        // Star field
        Random rng = new Random(42);
        for (int i = 0; i < 700; i++) {
            double size = rng.nextInt(2) + 1;
            Rectangle star = new Rectangle(size, size);
            star.setX(rng.nextDouble() * W);
            star.setY(rng.nextDouble() * H);
            star.setFill(Color.web("#FFFFFF", 0.006 + rng.nextDouble() * 0.018));
            canvas.getChildren().add(star);
        }

        // Top + bottom vignette
        Rectangle vig = new Rectangle(W, H);
        vig.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0.0,  Color.web("#000000", 0.7)),
                new Stop(0.18, Color.TRANSPARENT),
                new Stop(0.80, Color.TRANSPARENT),
                new Stop(1.0,  Color.web("#000000", 0.7))
        ));
        canvas.getChildren().add(vig);
    }

    // =========================================================================
    // AMBIENT ORBS  (soft glowing blobs from each stage's theme color)
    // =========================================================================

    private void drawAmbientOrbs() {
        double[][] orbPositions = {
                { 120,  620 },   // bottom-left  — Denial blue
                { 880,  580 },   // bottom-right — Anger crimson
                { W/2,  680 },   // bottom-center — Bargaining gold
                { 160,  200 },   // top-left     — Depression violet
                { 840,  160 },   // top-right    — Acceptance gold
        };

        for (int i = 0; i < STAGE_COLORS.length; i++) {
            Circle orb = new Circle(orbPositions[i][0], orbPositions[i][1], 180);
            orb.setFill(Color.web(STAGE_COLORS[i], 0.04));
            orb.setEffect(new GaussianBlur(90));
            canvas.getChildren().add(orb);
        }

        // Central glow behind the title
        Circle centerGlow = new Circle(W / 2, H * 0.38, 260);
        centerGlow.setFill(Color.web(C_ACCENT_GOLD, 0.028));
        centerGlow.setEffect(new GaussianBlur(120));
        canvas.getChildren().add(centerGlow);
    }

    // =========================================================================
    // DECORATIVE DIVIDER LINES
    // =========================================================================

    private void drawDividerLines() {
        // Top bar
        Rectangle topBar = new Rectangle(W, 32);
        topBar.setFill(Color.web(C_TITLE_BAR, 0.92));
        canvas.getChildren().add(topBar);

        Line topAccent = new Line(0, 32, W, 32);
        topAccent.setStroke(Color.web(C_ACCENT_GOLD, 0.25));
        topAccent.setStrokeWidth(1);
        canvas.getChildren().add(topAccent);

        // Bottom bar
        Rectangle botBar = new Rectangle(0, H - 28, W, 28);
        botBar.setFill(Color.web(C_TITLE_BAR, 0.92));
        canvas.getChildren().add(botBar);

        Line botAccent = new Line(0, H - 28, W, H - 28);
        botAccent.setStroke(Color.web(C_BAND_DIV, 0.8));
        botAccent.setStrokeWidth(1);
        canvas.getChildren().add(botAccent);

        // Horizontal rule above buttons
        double ruleY = H * 0.56;
        Line ruleLeft = new Line(W * 0.18, ruleY, W * 0.38, ruleY);
        ruleLeft.setStroke(Color.web(C_BAND_DIV, 0.7));
        ruleLeft.setStrokeWidth(1);

        Line ruleRight = new Line(W * 0.62, ruleY, W * 0.82, ruleY);
        ruleRight.setStroke(Color.web(C_BAND_DIV, 0.7));
        ruleRight.setStrokeWidth(1);

        // Diamond ornament at center of rule
        Polygon diamond = new Polygon(
                W / 2,      ruleY - 5,
                W / 2 + 5,  ruleY,
                W / 2,      ruleY + 5,
                W / 2 - 5,  ruleY
        );
        diamond.setFill(Color.web(C_ACCENT_GOLD, 0.5));

        canvas.getChildren().addAll(ruleLeft, ruleRight, diamond);
    }

    // =========================================================================
    // TITLE BLOCK
    // =========================================================================

    private void drawTitle() {
        // Eyebrow — Courier New
        Label eyebrow = new Label("A  J O U R N E Y  T H R O U G H");
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        eyebrow.setTextFill(Color.web(C_META));
        double eyebrowW = measureText(eyebrow.getText(), eyebrow.getFont());
        eyebrow.setLayoutX((W - eyebrowW) / 2.0);
        eyebrow.setLayoutY(H * 0.24);
        canvas.getChildren().add(eyebrow);

        // Main title — Georgia Bold, large
        Label title = new Label("G R I E F");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 72));
        title.setTextFill(Color.web(C_TITLE_TEXT, 0.92));
        title.setEffect(new DropShadow(24, Color.web("#000000", 0.9)));
        double titleW = measureText(title.getText(), title.getFont());
        title.setLayoutX((W - titleW) / 2.0);
        title.setLayoutY(H * 0.27);
        canvas.getChildren().add(title);

        // Animate title: gentle fade-in
        FadeTransition ft = new FadeTransition(Duration.millis(1200), title);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();

        // Gold accent underline beneath title
        double lineW = 220;
        Rectangle underline = new Rectangle((W - lineW) / 2.0, H * 0.42, lineW, 1);
        underline.setFill(Color.web(C_ACCENT_GOLD, 0.75));
        canvas.getChildren().add(underline);

        // Subtitle — Georgia Italic
        Label subtitle = new Label("Five stages. Five truths. One way through.");
        subtitle.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
        subtitle.setTextFill(Color.web(C_META, 0.9));
        double subW = measureText(subtitle.getText(), subtitle.getFont());
        subtitle.setLayoutX((W - subW) / 2.0);
        subtitle.setLayoutY(H * 0.435);
        canvas.getChildren().add(subtitle);

        // Animate subtitle after title
        subtitle.setOpacity(0);
        FadeTransition ft2 = new FadeTransition(Duration.millis(900), subtitle);
        ft2.setDelay(Duration.millis(600));
        ft2.setFromValue(0.0);
        ft2.setToValue(1.0);
        ft2.play();
    }

    // =========================================================================
    // MENU BUTTONS
    // =========================================================================

    private void drawMenuButtons() {
        double startY   = H * 0.595;
        double gap      = 58;
        boolean hasProgress = SaveManager.hasProgress();

        // BEGIN JOURNEY — always active
        buildMenuButton("BEGIN JOURNEY", "▶", C_COMPLETION, "play",
                W / 2.0, startY, 0, true);

        // CONTINUE — only active if progress exists, locked/dimmed otherwise
        buildMenuButton("CONTINUE", "↺", hasProgress ? C_ACTIVE : "#1C1C30",
                "continue", W / 2.0, startY + gap, 120, hasProgress);

        // QUIT — always active
        buildMenuButton("QUIT", "✕", "#AF0000", "quit",
                W / 2.0, startY + gap * 2, 240, true);
    }

    /**
     * @param active  true = clickable with hover; false = locked/dimmed, no interaction
     */
    private void buildMenuButton(String label, String icon, String colorHex,
                                 String action, double cx, double cy,
                                 long delayMs, boolean active) {
        double btnW = 280;
        double btnH = 42;
        double bx   = cx - btnW / 2.0;
        double by   = cy - btnH / 2.0;

        // Background rect
        Rectangle bg = new Rectangle(bx, by, btnW, btnH);
        bg.setArcWidth(4);
        bg.setArcHeight(4);
        bg.setFill(Color.web(C_TITLE_BAR, active ? 0.85 : 0.4));
        bg.setStroke(Color.web(active ? C_BAND_DIV : "#111120", 0.8));
        bg.setStrokeWidth(1);

        // Left accent bar
        Rectangle accent = new Rectangle(bx, by, 3, btnH);
        accent.setArcWidth(3);
        accent.setArcHeight(3);
        accent.setFill(Color.web(colorHex, active ? 0.7 : 0.2));

        // Icon label — Georgia
        Label iconLbl = new Label(active ? icon : "🔒");
        iconLbl.setFont(Font.font("Georgia", 14));
        iconLbl.setTextFill(Color.web(colorHex, active ? 0.85 : 0.25));
        iconLbl.setLayoutX(bx + 18);
        iconLbl.setLayoutY(by + 11);

        // Text label — Courier New Bold
        Label textLbl = new Label(label);
        textLbl.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        textLbl.setTextFill(Color.web(active ? C_ACTIVE : "#2A2A3A", active ? 0.85 : 1.0));
        textLbl.setLayoutX(bx + 44);
        textLbl.setLayoutY(by + 12);

        // Hit area
        Rectangle hit = new Rectangle(bx, by, btnW, btnH);
        hit.setFill(Color.TRANSPARENT);

        if (active) {
            hit.setStyle("-fx-cursor: hand;");
            hit.setOnMouseEntered(e -> {
                bg.setFill(Color.web(C_BAND_DIV, 0.95));
                bg.setStroke(Color.web(colorHex, 0.6));
                accent.setFill(Color.web(colorHex, 1.0));
                textLbl.setTextFill(Color.web(colorHex));
                iconLbl.setTextFill(Color.web(colorHex));
                bg.setEffect(new DropShadow(12, Color.web(colorHex, 0.35)));
            });
            hit.setOnMouseExited(e -> {
                bg.setFill(Color.web(C_TITLE_BAR, 0.85));
                bg.setStroke(Color.web(C_BAND_DIV, 0.8));
                accent.setFill(Color.web(colorHex, 0.7));
                textLbl.setTextFill(Color.web(C_ACTIVE, 0.85));
                iconLbl.setTextFill(Color.web(colorHex, 0.85));
                bg.setEffect(null);
            });
            hit.setOnMouseClicked(e -> handleMenuAction(action));
        } else {
            // Locked — no cursor change, no hover, no click
            hit.setStyle("-fx-cursor: default;");
        }

        javafx.scene.Group btnGroup = new javafx.scene.Group(bg, accent, iconLbl, textLbl, hit);
        btnGroup.setOpacity(0);
        canvas.getChildren().add(btnGroup);

        FadeTransition ft = new FadeTransition(Duration.millis(500), btnGroup);
        ft.setDelay(Duration.millis(900 + delayMs));
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    // =========================================================================
    // BUTTON ACTIONS
    // =========================================================================

    private void handleMenuAction(String action) {
        switch (action) {
            case "play" -> {
                if (SaveManager.hasProgress()) {
                    // Progress exists — warn before wiping
                    showNewGameWarning();
                } else {
                    // No progress — start fresh with intro dialogue
                    showIntroDialogue();
                }
            }
            case "continue" -> {
                // Go straight to level screen without dialogue
                navigateTo("level_screen.fxml");
            }
            case "quit" -> {
                MusicPlayer.getInstance().fadeOutAndStop(600);
                PauseTransition pause = new PauseTransition(Duration.millis(650));
                pause.setOnFinished(e -> javafx.application.Platform.exit());
                pause.play();
            }
        }
    }

    // =========================================================================
    // WARNING CARD
    // =========================================================================

    private void showNewGameWarning() {
        String warningColor = "#AF0000";   // dark red — signals danger

        // Full-screen backdrop
        Rectangle backdrop = new Rectangle(W, H);
        backdrop.setFill(Color.web("#000000", 0.82));

        // Ambient red orb
        Circle orb = new Circle(W / 2, H / 2, 280);
        orb.setFill(Color.web(warningColor, 0.06));
        orb.setEffect(new GaussianBlur(80));

        // Card
        double cardW = 440;
        VBox card = new VBox(16);
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(cardW);
        card.setPrefWidth(cardW);
        card.setStyle(
                "-fx-background-color: #07070E;" +
                        "-fx-border-color: " + warningColor + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;" +
                        "-fx-padding: 36 44 32 44;"
        );
        card.setEffect(new DropShadow(24, Color.web(warningColor, 0.4)));

        // Warning eyebrow — Courier New
        Label eyebrow = new Label("WARNING");
        eyebrow.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        eyebrow.setTextFill(Color.web(warningColor, 0.8));
        eyebrow.setStyle("-fx-letter-spacing: 3;");

        // Title — Georgia Bold
        Label titleLbl = new Label("Begin a New Journey?");
        titleLbl.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        titleLbl.setTextFill(Color.web("#D8D8EC"));
        titleLbl.setEffect(new DropShadow(10, Color.web("#000000", 0.9)));

        // Divider
        Rectangle divider = new Rectangle(cardW - 88, 1);
        divider.setFill(Color.web(warningColor, 0.3));

        // Warning message — Georgia Italic
        Label msgLbl = new Label("Your current progress will be erased.\nThis cannot be undone.");
        msgLbl.setFont(Font.font("Georgia", FontPosture.ITALIC, 13));
        msgLbl.setTextFill(Color.web("#5E5E78"));
        msgLbl.setWrapText(true);
        msgLbl.setMaxWidth(cardW - 88);
        msgLbl.setAlignment(Pos.CENTER);

        // Buttons
        Button startFreshBtn   = makeWarningButton("▶  START FRESH",   warningColor, true);
        Button keepProgressBtn = makeWarningButton("←  KEEP PROGRESS", "#3A3A56",    false);

        VBox btnRow = new VBox(10, startFreshBtn, keepProgressBtn);
        btnRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(eyebrow, titleLbl, divider, msgLbl, btnRow);
        card.setLayoutX(W / 2 - cardW / 2);

        Pane overlay = new Pane(backdrop, orb, card);
        card.layoutBoundsProperty().addListener((obs, oldB, newB) -> {
            if (newB.getHeight() > 0) card.setLayoutY((H - newB.getHeight()) / 2.0);
        });
        canvas.getChildren().add(overlay);

        // Fade in
        overlay.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(280), overlay);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // START FRESH — wipe save then show intro dialogue
        startFreshBtn.setOnAction(e -> {
            dismissOverlay(overlay);
            SaveManager.reset();
            showIntroDialogue();
        });

        // KEEP PROGRESS — just dismiss
        keepProgressBtn.setOnAction(e -> dismissOverlay(overlay));

        // Backdrop click also dismisses
        backdrop.setOnMouseClicked(e -> dismissOverlay(overlay));
        card.setOnMouseClicked(e -> e.consume());
    }

    // =========================================================================
    // INTRO DIALOGUE
    // =========================================================================

    /**
     * Shows Dialogue1 when starting a new game.
     * Dialogue1 is configured with all assets (background, characters, music, etc).
     * After dialogue completes, transitions to level screen.
     */
    private void showIntroDialogue() {
        // 1. Define the sequence of dialogues you want to play
        List<DialogueConfig> sequence = List.of(
                Dialogue1.getConfig(),
                Dialogue2.getConfig()
                // can add Dialogue3.getConfig() here later
        );

        // 2. Start the sequence at index 0
        playDialogueSequence(sequence, 0);
    }

    private void playDialogueSequence(List<DialogueConfig> sequence, int index) {
        // If we've played all dialogues, navigate to the game
        if (index >= sequence.size()) {
            navigateTo("level_screen.fxml");
            return;
        }

        try {
            // Create the current dialogue screen
            DialogueScreen currentDialogue = new DialogueScreen(
                    sequence.get(index),
                    () -> {
                        // ON FINISH: Remove current and play the next index
                        canvas.getChildren().removeIf(node -> node instanceof DialogueScreen);
                        playDialogueSequence(sequence, index + 1);
                    }
            );

            currentDialogue.setPrefSize(W, H);
            canvas.getChildren().add(currentDialogue);

        } catch (Exception ex) {
            System.err.println("[Menu] Dialogue sequence failed at index " + index + ": " + ex.getMessage());
            navigateTo("level_screen.fxml");
        }
    }

    private Button makeWarningButton(String label, String colorHex, boolean isPrimary) {
        Button btn = new Button(label);
        btn.setFont(Font.font("Courier New", FontWeight.BOLD, 11));
        btn.setTextFill(Color.web(isPrimary ? colorHex : "#3A3A56"));
        btn.setPrefWidth(240);

        String base  = "-fx-background-color: transparent;" +
                "-fx-border-color: " + colorHex + ";" +
                "-fx-border-width: 1;-fx-border-radius: 4;" +
                "-fx-padding: 10 0;-fx-cursor: hand;";
        String hover = "-fx-background-color: " + colorHex + "22;" +
                "-fx-border-color: " + colorHex + ";" +
                "-fx-border-width: 1;-fx-border-radius: 4;" +
                "-fx-padding: 10 0;-fx-cursor: hand;";

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

    private void dismissOverlay(Pane overlay) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), overlay);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(ev -> canvas.getChildren().remove(overlay));
        fadeOut.play();
    }

    private void navigateTo(String fxmlFile) {
        try {
            URL resource = getClass().getResource(fxmlFile);
            if (resource == null) {
                System.err.println("[Menu] FXML not found: " + fxmlFile);
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) canvas.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("[Menu] Navigation failed: " + e.getMessage());
        }
    }

    // =========================================================================
    // VERSION TAG
    // =========================================================================

    private void drawVersionTag() {
        Label ver = new Label("v0.1  —  Kai: A Journey Through Grief");
        ver.setFont(Font.font("Courier New", 8));
        ver.setTextFill(Color.web(C_META, 0.6));
        double vw = measureText(ver.getText(), ver.getFont());
        ver.setLayoutX((W - vw) / 2.0);
        ver.setLayoutY(H - 20);
        canvas.getChildren().add(ver);
    }

    // =========================================================================
    // UTIL
    // =========================================================================

    /** Measures rendered text width for centering calculations. */
    private double measureText(String text, Font font) {
        Text t = new Text(text);
        t.setFont(font);
        return t.getLayoutBounds().getWidth();
    }
}