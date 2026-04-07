package com.example.kaiajourneythroughgrief;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.LinkedList;
import java.util.List;
import java.net.URL;
import java.util.Queue;

public class DialogueScreen extends StackPane {

    private final StackPane leftVisual;
    private final StackPane rightVisual;
    private final Rectangle leftPlaceholder;
    private final Rectangle rightPlaceholder;
    private final ImageView leftCharacterImage;
    private final ImageView rightCharacterImage;
    private final Label leftNameLabel;
    private final Label rightNameLabel;

    private final Label dialogueText;
    private final StackPane dialogueBox;
    private final DropShadow activeGlow;
    private final ImageView backgroundImage;
    private final Rectangle backgroundPlaceholder;

    private final Queue<DialogueConfig.DialogueLine> dialogueQueue;
    private final Runnable onComplete;
    private Speaker currentSpeaker = Speaker.NONE;

    private MediaPlayer currentAudio;
    private MediaPlayer backgroundMusicPlayer;
    private double originalMusicVolume = 0.5;
    private double originalBackgroundVolume = 0.5;
    private double dialogueBackgroundVolume = 0.2;

    // Design system colors
    private static final Color CANVAS_BG = Color.web("#07070F");
    private static final Color CARD_BG = Color.web("#07070E");
    private static final Color LOCKED_BAND_FILL = Color.web("#0A0A15");
    private static final Color LOCKED_STROKE = Color.web("#161624");
    private static final Color ACTIVE_TEXT = Color.web("#D8D8EC");
    private static final Color ENEMY_TEXT = Color.web("#3A3A56");
    private static final Color COMPLETION_GREEN = Color.web("#00FF88");

    public enum Speaker {
        LEFT, RIGHT, NARRATOR, NONE
    }

    /**
     * Constructor accepting DialogueConfig for full scene setup
     */
    public DialogueScreen(DialogueConfig config) {
        this(config, null);
    }

    /**
     * Constructor accepting DialogueConfig and completion callback
     */
    public DialogueScreen(DialogueConfig config, Runnable onComplete) {
        this.dialogueQueue = new LinkedList<>(config.getDialogueLines());
        this.onComplete = onComplete;

        // Background placeholder - subtle gradient in design system palette
        backgroundPlaceholder = new Rectangle();
        backgroundPlaceholder.widthProperty().bind(this.widthProperty());
        backgroundPlaceholder.heightProperty().bind(this.heightProperty());

        LinearGradient backgroundGradient = new LinearGradient(
                0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#07070F")),
                new Stop(0.5, Color.web("#0A0A15")),
                new Stop(1, Color.web("#07070F"))
        );
        backgroundPlaceholder.setFill(backgroundGradient);

        // Background image
        backgroundImage = new ImageView();
        backgroundImage.setPreserveRatio(false);
        backgroundImage.fitWidthProperty().bind(this.widthProperty());
        backgroundImage.fitHeightProperty().bind(this.heightProperty());
        backgroundImage.setSmooth(true);
        backgroundImage.setCache(true);

        // Load background image from config
        if (config.getBackgroundImagePath() != null && !config.getBackgroundImagePath().isBlank()) {
            setBackgroundImage(config.getBackgroundImagePath());
        }

        // Character placeholders - matching locked state aesthetic
        leftPlaceholder = createCharacterPlaceholder();
        rightPlaceholder = createCharacterPlaceholder();

        leftCharacterImage = new ImageView();
        rightCharacterImage = new ImageView();

        configureCharacterImage(leftCharacterImage);
        configureCharacterImage(rightCharacterImage);

        // Character name labels - Courier New for labels (technical/metadata)
        leftNameLabel = new Label(config.getLeftCharacterName());
        rightNameLabel = new Label(config.getRightCharacterName());
        styleCharacterName(leftNameLabel);
        styleCharacterName(rightNameLabel);

        // Load character images from config
        setImageForView(leftCharacterImage, leftPlaceholder, config.getLeftCharacterImagePath());
        setImageForView(rightCharacterImage, rightPlaceholder, config.getRightCharacterImagePath());

        StackPane leftImagePane = new StackPane(leftPlaceholder, leftCharacterImage);
        StackPane rightImagePane = new StackPane(rightPlaceholder, rightCharacterImage);

        leftVisual = new StackPane(new VBox(8, leftImagePane, leftNameLabel));
        rightVisual = new StackPane(new VBox(8, rightImagePane, rightNameLabel));

        // Initial opacity for inactive speakers
        leftVisual.setOpacity(0.35);
        rightVisual.setOpacity(0.35);

        // Glow effect for active speaker (unlocked node glow equivalent)
        activeGlow = new DropShadow(24, Color.rgb(255, 255, 255, 0.75));

        HBox characterContainer = new HBox(80, leftVisual, rightVisual);
        characterContainer.setAlignment(Pos.CENTER);
        characterContainer.setPadding(new Insets(20, 0, 20, 0));
        characterContainer.setMaxWidth(Double.MAX_VALUE);
        characterContainer.setPrefWidth(Double.MAX_VALUE);

        // Dialogue box background - card background color, expanded to fill bottom
        Rectangle dialogueBackground = new Rectangle();
        dialogueBackground.widthProperty().bind(this.widthProperty());
        dialogueBackground.heightProperty().bind(this.heightProperty().multiply(0.35)); // Takes 35% of screen
        dialogueBackground.setFill(CARD_BG);
        dialogueBackground.setArcWidth(0);
        dialogueBackground.setArcHeight(0);

        // Subtle stroke like card stroke (unlocked)
        dialogueBackground.setStroke(Color.rgb(22, 22, 36, 0.7));
        dialogueBackground.setStrokeWidth(1);

        // Title text shadow from design system
        DropShadow dialogueShadow = new DropShadow(8, Color.rgb(0, 0, 0, 0.9));
        dialogueBackground.setEffect(dialogueShadow);

        // Dialogue text - Georgia for narrative content
        dialogueText = new Label("Click to begin...");
        dialogueText.setTextFill(ACTIVE_TEXT);
        dialogueText.setWrapText(true);
        dialogueText.setMaxWidth(900);
        dialogueText.setFont(Font.font("Georgia", FontPosture.ITALIC, 18));
        dialogueText.setLineSpacing(3);

        // Skip button
        Button skipButton = createSkipButton();
        skipButton.setMinWidth(60);
        skipButton.setPrefWidth(60);

        HBox captionContent = new HBox(12, skipButton, dialogueText);
        captionContent.setAlignment(Pos.CENTER_LEFT);
        captionContent.setPadding(new Insets(24));
        captionContent.setStyle("-fx-background-color: transparent;");
        captionContent.maxWidthProperty().bind(this.widthProperty());
        captionContent.minWidthProperty().bind(this.widthProperty());
        HBox.setHgrow(dialogueText, Priority.ALWAYS);

        dialogueBox = new StackPane(dialogueBackground, captionContent);
        dialogueBox.setAlignment(Pos.CENTER_LEFT); // FIX: Changed from CENTER to CENTER_LEFT
        dialogueBox.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(dialogueBox, Priority.ALWAYS);

        // Main content layout: Characters at top, dialogue box expands below
        VBox contentLayout = new VBox(0, characterContainer, dialogueBox);
        contentLayout.setStyle("-fx-background-color: transparent;");
        contentLayout.maxWidthProperty().bind(this.widthProperty());
        contentLayout.minWidthProperty().bind(this.widthProperty());
        VBox.setVgrow(dialogueBox, Priority.ALWAYS);

        // Root StackPane: backgrounds behind, content on top
        StackPane root = new StackPane(backgroundPlaceholder, backgroundImage, contentLayout);
        root.setStyle("-fx-background-color: #07070F;");
        root.maxWidthProperty().bind(this.widthProperty());
        root.maxHeightProperty().bind(this.heightProperty());
        this.getChildren().add(root);

        dialogueBox.setOnMouseClicked(event -> advanceDialogue());
        dialogueBox.setCursor(javafx.scene.Cursor.HAND);

        // Fade-in transition
        this.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), this);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();

        // Setup background music from config
        setupBackgroundMusic(config.getBackgroundMusicPath(), config.getBackgroundMusicVolume());

        if (!dialogueQueue.isEmpty()) {
            shiftFocus(convertSpeaker(dialogueQueue.peek().getSpeaker()));
        }
    }

    /**
     * Setup background music for the dialogue scene
     */
    private void setupBackgroundMusic(String musicPath, double volume) {
        if (musicPath == null || musicPath.isBlank()) {
            return;
        }

        try {
            // Store original music player volume
            originalBackgroundVolume = MusicPlayer.getInstance().getVolume();
            this.dialogueBackgroundVolume = volume;

            // Lower global background music during dialogue
            MusicPlayer.getInstance().setVolume(volume);
        } catch (Exception e) {
            System.err.println("[DialogueScreen] Could not setup background music: " + e.getMessage());
        }
    }

    /**
     * Convert DialogueConfig.Speaker to DialogueScreen.Speaker
     */
    private Speaker convertSpeaker(DialogueConfig.DialogueLine.Speaker configSpeaker) {
        return switch (configSpeaker) {
            case LEFT -> Speaker.LEFT;
            case RIGHT -> Speaker.RIGHT;
            case NARRATOR -> Speaker.NARRATOR;
            case NONE -> Speaker.NONE;
        };
    }

    private Rectangle createCharacterPlaceholder() {
        Rectangle rect = new Rectangle(220, 280);
        rect.setArcWidth(5);
        rect.setArcHeight(5);

        // Locked state aesthetic - very dark with subtle stroke
        rect.setFill(LOCKED_BAND_FILL);
        rect.setStroke(LOCKED_STROKE);
        rect.setStrokeWidth(1);
        rect.setOpacity(0.97); // Locked band fill opacity

        return rect;
    }

    private void advanceDialogue() {
        if (dialogueQueue.isEmpty()) {
            dialogueText.setText("End of conversation.");
            clearFocus();
            dialogueBox.setOnMouseClicked(null);
            stopCurrentAudio();

            // Fade out transition
            FadeTransition fadeOut = new FadeTransition(Duration.millis(600), this);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                // Restore background music volume
                MusicPlayer.getInstance().setVolume(originalBackgroundVolume);
                if (onComplete != null) {
                    onComplete.run();
                }
            });
            fadeOut.play();
            return;
        }

        DialogueConfig.DialogueLine nextLine = dialogueQueue.poll();
        stopCurrentAudio();

        // Text fade transition
        FadeTransition textFadeOut = new FadeTransition(Duration.millis(120), dialogueText);
        textFadeOut.setFromValue(1.0);
        textFadeOut.setToValue(0.0);

        FadeTransition textFadeIn = new FadeTransition(Duration.millis(240), dialogueText);
        textFadeIn.setFromValue(0.0);
        textFadeIn.setToValue(1.0);

        textFadeOut.setOnFinished(e -> {
            dialogueText.setText(nextLine.getText());
            if (nextLine.getAudioPath() != null && !nextLine.getAudioPath().isBlank()) {
                playAudio(nextLine.getAudioPath());
            }
            textFadeIn.play();
        });

        textFadeOut.play();
        shiftFocus(convertSpeaker(nextLine.getSpeaker()));
    }

    private void shiftFocus(Speaker newSpeaker) {
        if (currentSpeaker == newSpeaker) return;

        StackPane becomingSilent = getCharacterNode(currentSpeaker);
        StackPane becomingActive = getCharacterNode(newSpeaker);

        ParallelTransition parallelTransition = new ParallelTransition();

        // Fade out previous speaker (to inactive state)
        if (becomingSilent != null) {
            becomingSilent.setEffect(null);
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), becomingSilent);
            fadeOut.setToValue(0.35); // Inactive opacity

            ScaleTransition scaleDown = new ScaleTransition(Duration.millis(300), becomingSilent);
            scaleDown.setToX(0.95);
            scaleDown.setToY(0.95);

            parallelTransition.getChildren().addAll(fadeOut, scaleDown);
        }

        // Fade in new speaker (to active state)
        if (becomingActive != null) {
            FadeTransition fadeIn = new FadeTransition(Duration.millis(300), becomingActive);
            fadeIn.setToValue(1.0); // Full opacity when active

            ScaleTransition scaleUp = new ScaleTransition(Duration.millis(300), becomingActive);
            scaleUp.setToX(1.0);
            scaleUp.setToY(1.0);

            fadeIn.setOnFinished(e -> becomingActive.setEffect(activeGlow));
            parallelTransition.getChildren().addAll(fadeIn, scaleUp);
        }

        parallelTransition.play();
        currentSpeaker = newSpeaker;
    }

    private void clearFocus() {
        shiftFocus(Speaker.NONE);
    }

    private void stopCurrentAudio() {
        if (currentAudio != null) {
            currentAudio.stop();
            currentAudio.dispose();
            currentAudio = null;
        }
    }

    private void playAudio(String audioPath) {
        try {
            URL audioUrl = resolveAudioResource(audioPath);
            if (audioUrl != null) {
                Media media = new Media(audioUrl.toString());
                currentAudio = new MediaPlayer(media);
                currentAudio.setVolume(originalMusicVolume);
                currentAudio.play();
            } else {
                System.err.println("[DialogueScreen] Audio file not found: " + audioPath);
            }
        } catch (Exception e) {
            System.err.println("[DialogueScreen] Could not play audio: " + audioPath + " - " + e.getMessage());
        }
    }

    private URL resolveAudioResource(String audioPath) {
        if (audioPath == null || audioPath.isBlank()) {
            return null;
        }

        // Try as-given first
        URL url = getClass().getResource(audioPath);
        if (url != null) {
            return url;
        }

        // Normalize missing leading slash
        String normalized = audioPath.startsWith("/") ? audioPath : "/" + audioPath;
        url = getClass().getResource(normalized);
        if (url != null) {
            return url;
        }

        // Fallback to package root path (this project structure)
        String packageRoot = "/com/example/kaiajourneythroughgrief" + normalized;
        return getClass().getResource(packageRoot);
    }

    private StackPane getCharacterNode(Speaker speaker) {
        return switch (speaker) {
            case LEFT -> leftVisual;
            case RIGHT -> rightVisual;
            default -> null;
        };
    }

    private void configureCharacterImage(ImageView imageView) {
        imageView.setFitWidth(220);
        imageView.setFitHeight(280);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }

    private void styleCharacterName(Label label) {
        // Courier New for labels/metadata
        label.setFont(Font.font("Courier New", FontWeight.BOLD, 10));
        label.setTextFill(ENEMY_TEXT); // Metadata color
        label.setStyle("-fx-letter-spacing: 2; " +
                "-fx-background-color: rgba(7, 7, 14, 0.85); " +
                "-fx-padding: 5 10; " +
                "-fx-background-radius: 3;");
        label.setAlignment(Pos.CENTER);
    }

    private void setImageForView(ImageView view, Rectangle placeholder, String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            view.setImage(null);
            placeholder.setVisible(true);
            return;
        }

        try {
            URL resource = getClass().getResource(imagePath);
            if (resource != null) {
                Image img = new Image(resource.toExternalForm(), false);
                view.setImage(img);
                placeholder.setVisible(false);

                // Smooth fade-in
                view.setOpacity(0);
                FadeTransition imageFade = new FadeTransition(Duration.millis(500), view);
                imageFade.setFromValue(0.0);
                imageFade.setToValue(1.0);
                imageFade.play();
                return;
            }
            System.err.println("[DialogueScreen] Image not found: " + imagePath);
        } catch (Exception e) {
            System.err.println("[DialogueScreen] Could not load image: " + imagePath + " — " + e.getMessage());
        }

        view.setImage(null);
        placeholder.setVisible(true);
    }

    public void setBackgroundImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            backgroundImage.setImage(null);
            backgroundPlaceholder.setVisible(true);
            return;
        }

        try {
            URL resource = getClass().getResource(imagePath);
            if (resource != null) {
                Image img = new Image(resource.toExternalForm(), false);
                backgroundImage.setImage(img);
                backgroundPlaceholder.setVisible(false);

                // Smooth fade-in
                backgroundImage.setOpacity(0);
                FadeTransition bgFade = new FadeTransition(Duration.millis(800), backgroundImage);
                bgFade.setFromValue(0.0);
                bgFade.setToValue(1.0);
                bgFade.play();
                return;
            }
            System.err.println("[DialogueScreen] Background image not found: " + imagePath);
        } catch (Exception e) {
            System.err.println("[DialogueScreen] Could not load background: " + imagePath + " — " + e.getMessage());
        }

        backgroundPlaceholder.setVisible(true);
    }

    public void setOriginalMusicVolume(double volume) {
        this.originalMusicVolume = volume;
    }

    private Button createSkipButton() {
        Button skipBtn = new Button("SKIP");
        // Courier New for technical/label text
        skipBtn.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        skipBtn.setTextFill(ENEMY_TEXT);
        skipBtn.setStyle(
                "-fx-letter-spacing: 2;" +
                        "-fx-background-color: rgba(10, 10, 21, 0.7);" +
                        "-fx-border-color: rgba(22, 22, 36, 0.7);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 3;" +
                        "-fx-background-radius: 3;" +
                        "-fx-padding: 6 12;" +
                        "-fx-cursor: hand;"
        );

        String hoverStyle =
                "-fx-letter-spacing: 2;" +
                        "-fx-background-color: rgba(22, 22, 36, 0.85);" +
                        "-fx-border-color: rgba(88, 88, 120, 0.7);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 3;" +
                        "-fx-background-radius: 3;" +
                        "-fx-padding: 6 12;" +
                        "-fx-cursor: hand;";

        skipBtn.setOnMouseEntered(e -> {
            skipBtn.setStyle(hoverStyle);
            skipBtn.setTextFill(ACTIVE_TEXT);
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), skipBtn);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        skipBtn.setOnMouseExited(e -> {
            skipBtn.setStyle(
                    "-fx-letter-spacing: 2;" +
                            "-fx-background-color: rgba(10, 10, 21, 0.7);" +
                            "-fx-border-color: rgba(22, 22, 36, 0.7);" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;" +
                            "-fx-padding: 6 12;" +
                            "-fx-cursor: hand;");
            skipBtn.setTextFill(ENEMY_TEXT);
        });

        skipBtn.setOnAction(e -> showSkipConfirmation());

        return skipBtn;
    }

    private void showSkipConfirmation() {
        // Backdrop
        Rectangle backdrop = new Rectangle();
        backdrop.widthProperty().bind(this.widthProperty());
        backdrop.heightProperty().bind(this.heightProperty());
        backdrop.setFill(Color.rgb(7, 7, 15, 0.85)); // Canvas background with high opacity

        // Warning symbol - Georgia for display/UI text
        Label iconLabel = new Label("⚠");
        iconLabel.setFont(Font.font("Georgia", 28));
        iconLabel.setTextFill(Color.web("#B8860B")); // Title bar underline accent color

        // Title - Georgia Bold for titles
        Label warningLabel = new Label("Skip Dialogue?");
        warningLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 15)); // Main title bar size
        warningLabel.setTextFill(ACTIVE_TEXT);

        // Subtitle - Georgia Italic for subtitles
        Label confirmLabel = new Label("This will end the conversation");
        confirmLabel.setFont(Font.font("Georgia", FontPosture.ITALIC, 11));
        confirmLabel.setTextFill(ENEMY_TEXT);

        // Yes button - Courier New for labels
        Button yesBtn = new Button("YES");
        yesBtn.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        yesBtn.setTextFill(Color.web("#DC143C")); // Anger stage color for destructive action
        yesBtn.setStyle(
                "-fx-letter-spacing: 2;" +
                        "-fx-background-color: rgba(92, 0, 16, 0.3);" + // Anger dark hex with low opacity
                        "-fx-border-color: rgba(220, 20, 60, 0.5);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 3;" +
                        "-fx-background-radius: 3;" +
                        "-fx-padding: 7 18;" +
                        "-fx-cursor: hand;");
        yesBtn.setPrefWidth(85);

        // Cancel button - Courier New for labels
        Button noBtn = new Button("CANCEL");
        noBtn.setFont(Font.font("Courier New", FontWeight.BOLD, 9));
        noBtn.setTextFill(ENEMY_TEXT);
        noBtn.setStyle(
                "-fx-letter-spacing: 2;" +
                        "-fx-background-color: rgba(10, 10, 21, 0.7);" +
                        "-fx-border-color: rgba(22, 22, 36, 0.7);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 3;" +
                        "-fx-background-radius: 3;" +
                        "-fx-padding: 7 18;" +
                        "-fx-cursor: hand;");
        noBtn.setPrefWidth(85);

        HBox buttonBox = new HBox(10, noBtn, yesBtn);
        buttonBox.setAlignment(Pos.CENTER);

        VBox confirmBox = new VBox(10, iconLabel, warningLabel, confirmLabel, buttonBox);
        confirmBox.setAlignment(Pos.CENTER);
        confirmBox.setStyle(
                "-fx-background-color: #0D0D1A;" + // Dialog pane background
                        "-fx-border-color: #B8860B;" + // Title bar underline accent
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 20 28;");
        confirmBox.setMaxWidth(260);

        // Title text shadow
        DropShadow confirmShadow = new DropShadow(8, Color.rgb(0, 0, 0, 0.9));
        confirmBox.setEffect(confirmShadow);

        StackPane confirmOverlay = new StackPane(backdrop, confirmBox);
        StackPane.setAlignment(confirmBox, Pos.CENTER);

        // Animate entrance
        confirmBox.setScaleX(0.85);
        confirmBox.setScaleY(0.85);
        confirmBox.setOpacity(0);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), confirmBox);
        scaleIn.setToX(1.0);
        scaleIn.setToY(1.0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), confirmBox);
        fadeIn.setToValue(1.0);

        ParallelTransition showAnim = new ParallelTransition(scaleIn, fadeIn);

        this.getChildren().add(confirmOverlay);
        showAnim.play();

        // Button hover effects
        yesBtn.setOnMouseEntered(e -> {
            yesBtn.setStyle(
                    "-fx-letter-spacing: 2;" +
                            "-fx-background-color: rgba(92, 0, 16, 0.6);" +
                            "-fx-border-color: rgba(220, 20, 60, 0.8);" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;" +
                            "-fx-padding: 7 18;" +
                            "-fx-cursor: hand;");
            yesBtn.setTextFill(Color.web("#FF6B6B"));
        });
        yesBtn.setOnMouseExited(e -> {
            yesBtn.setStyle(
                    "-fx-letter-spacing: 2;" +
                            "-fx-background-color: rgba(92, 0, 16, 0.3);" +
                            "-fx-border-color: rgba(220, 20, 60, 0.5);" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;" +
                            "-fx-padding: 7 18;" +
                            "-fx-cursor: hand;");
            yesBtn.setTextFill(Color.web("#DC143C"));
        });

        noBtn.setOnMouseEntered(e -> {
            noBtn.setStyle(
                    "-fx-letter-spacing: 2;" +
                            "-fx-background-color: rgba(22, 22, 36, 0.85);" +
                            "-fx-border-color: rgba(88, 88, 120, 0.7);" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;" +
                            "-fx-padding: 7 18;" +
                            "-fx-cursor: hand;");
            noBtn.setTextFill(ACTIVE_TEXT);
        });
        noBtn.setOnMouseExited(e -> {
            noBtn.setStyle(
                    "-fx-letter-spacing: 2;" +
                            "-fx-background-color: rgba(10, 10, 21, 0.7);" +
                            "-fx-border-color: rgba(22, 22, 36, 0.7);" +
                            "-fx-border-width: 1;" +
                            "-fx-border-radius: 3;" +
                            "-fx-background-radius: 3;" +
                            "-fx-padding: 7 18;" +
                            "-fx-cursor: hand;");
            noBtn.setTextFill(ENEMY_TEXT);
        });

        // Button actions with animation
        yesBtn.setOnAction(e -> {
            ScaleTransition scaleOut = new ScaleTransition(Duration.millis(150), confirmBox);
            scaleOut.setToX(0.85);
            scaleOut.setToY(0.85);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), confirmOverlay);
            fadeOut.setToValue(0.0);

            ParallelTransition hideAnim = new ParallelTransition(scaleOut, fadeOut);
            hideAnim.setOnFinished(ev -> {
                this.getChildren().remove(confirmOverlay);
                skipDialogue();
            });
            hideAnim.play();
        });

        noBtn.setOnAction(e -> {
            ScaleTransition scaleOut = new ScaleTransition(Duration.millis(150), confirmBox);
            scaleOut.setToX(0.85);
            scaleOut.setToY(0.85);

            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), confirmOverlay);
            fadeOut.setToValue(0.0);

            ParallelTransition hideAnim = new ParallelTransition(scaleOut, fadeOut);
            hideAnim.setOnFinished(ev -> this.getChildren().remove(confirmOverlay));
            hideAnim.play();
        });

        // Click backdrop to cancel
        backdrop.setOnMouseClicked(e -> noBtn.fire());
    }

    private void skipDialogue() {
        stopCurrentAudio();
        dialogueQueue.clear();
        advanceDialogue();
    }
}