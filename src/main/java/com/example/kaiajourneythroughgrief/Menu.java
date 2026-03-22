package com.example.kaiajourneythroughgrief;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
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
        double startY = H * 0.595;
        double gap    = 58;

        String[][] buttons = {
                { "BEGIN JOURNEY",  "▶",  C_COMPLETION,  "play"   },
                { "CONTINUE",       "↺",  C_ACTIVE,      "continue"},
                { "QUIT",           "✕",  "#AF0000",     "quit"   },
        };

        for (int i = 0; i < buttons.length; i++) {
            buildMenuButton(
                    buttons[i][0],
                    buttons[i][1],
                    buttons[i][2],
                    buttons[i][3],
                    W / 2.0,
                    startY + i * gap,
                    i * 120   // stagger delay ms
            );
        }
    }

    private void buildMenuButton(String label, String icon, String colorHex,
                                 String action, double cx, double cy, long delayMs) {
        double btnW = 280;
        double btnH = 42;
        double bx   = cx - btnW / 2.0;
        double by   = cy - btnH / 2.0;

        // Background rect
        Rectangle bg = new Rectangle(bx, by, btnW, btnH);
        bg.setArcWidth(4);
        bg.setArcHeight(4);
        bg.setFill(Color.web(C_TITLE_BAR, 0.85));
        bg.setStroke(Color.web(C_BAND_DIV, 0.8));
        bg.setStrokeWidth(1);

        // Left accent bar
        Rectangle accent = new Rectangle(bx, by, 3, btnH);
        accent.setArcWidth(3);
        accent.setArcHeight(3);
        accent.setFill(Color.web(colorHex, 0.7));

        // Icon label — Georgia
        Label iconLbl = new Label(icon);
        iconLbl.setFont(Font.font("Georgia", 14));
        iconLbl.setTextFill(Color.web(colorHex, 0.85));
        iconLbl.setLayoutX(bx + 18);
        iconLbl.setLayoutY(by + 11);

        // Text label — Courier New Bold
        Label textLbl = new Label(label);
        textLbl.setFont(Font.font("Courier New", FontWeight.BOLD, 12));
        textLbl.setTextFill(Color.web(C_ACTIVE, 0.85));
        textLbl.setLayoutX(bx + 44);
        textLbl.setLayoutY(by + 12);

        // Hit area
        Rectangle hit = new Rectangle(bx, by, btnW, btnH);
        hit.setFill(Color.TRANSPARENT);
        hit.setStyle("-fx-cursor: hand;");

        // Hover effects
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

        // Fade-in stagger animation
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
            case "play"     -> navigateTo("level_screen.fxml");
            case "continue" -> navigateTo("level_screen.fxml");
            case "quit"     -> {
                MusicPlayer.getInstance().fadeOutAndStop(600);
                javafx.animation.PauseTransition pause =
                        new javafx.animation.PauseTransition(javafx.util.Duration.millis(650));
                pause.setOnFinished(e -> javafx.application.Platform.exit());
                pause.play();
            }
        }
    }

    private void navigateTo(String fxmlFile) {
        try {
            URL resource = getClass().getResource(fxmlFile);
            if (resource == null) {
                System.err.println("[MenuController] FXML not found: " + fxmlFile);
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) canvas.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("[MenuController] Navigation failed: " + e.getMessage());
        }
    }

    // =========================================================================
    // VERSION TAG
    // =========================================================================

    private void drawVersionTag() {
        Label ver = new Label("v0.1  —  ARRAY COMBAT");
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