package com.example.kaiajourneythroughgrief.battle;

import com.example.kaiajourneythroughgrief.MusicPlayer;
import com.example.kaiajourneythroughgrief.SaveManager;
import com.example.kaiajourneythroughgrief.battle.gen.Generators;
import com.example.kaiajourneythroughgrief.battle.gen.TileGenerator;
import com.example.kaiajourneythroughgrief.battle.model.Board;
import com.example.kaiajourneythroughgrief.battle.model.BoardLayout;
import com.example.kaiajourneythroughgrief.battle.model.HealthModel;
import com.example.kaiajourneythroughgrief.battle.model.LockedTiles;
import com.example.kaiajourneythroughgrief.battle.model.Tile;
import com.example.kaiajourneythroughgrief.battle.nav.SceneNavigator;
import com.example.kaiajourneythroughgrief.battle.view.BattleSceneView;
import com.example.kaiajourneythroughgrief.battle.view.BoardRenderer;
import com.example.kaiajourneythroughgrief.battle.view.Effects;
import com.example.kaiajourneythroughgrief.battle.view.EndOverlay;
import com.example.kaiajourneythroughgrief.battle.view.Painters;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

/**
 * The one battle controller, shared by all five stages.
 *
 * <p>Everything stage-specific arrives through {@link StageConfig}: the copy, the
 * assets, the tile style, the sort rule, the difficulty numbers and where "next"
 * leads. Everything here — the drain loop, drag handling, swap animation, the
 * locked-tile mechanic, the result card — is identical across stages.
 *
 * <p>The controller is never referenced from FXML; {@code BattleScene} installs it
 * programmatically so the stage can be passed to the constructor.
 */
public final class BattleController implements Initializable {

    // ── FXML injected nodes ────────────────────────────────────────────────
    @FXML private StackPane  topPanel;
    @FXML private AnchorPane battleArea;
    @FXML private StackPane  bottomPanel;

    private static final double INITIAL_CANVAS_WIDTH = 800;

    private final StageConfig config;
    private final TileGenerator generator;
    private final BoardLayout layout;
    private final BoardRenderer renderer;
    private final HealthModel health = new HealthModel(Theme.STARTING_HEALTH);
    private final LockedTiles locked;
    private final Random random = new Random();
    private final List<Timeline> running = new ArrayList<>();

    private BattleSceneView view;
    private SceneNavigator navigator;
    private Canvas canvas;
    private Board board;
    private AnimationTimer gameLoop;

    private int arraysSolved;
    private long lastNanoTime = -1;
    private boolean gameOver;
    private boolean dragging;
    private boolean animating;
    private int dragIndex = -1;
    private double dragOffsetX;
    private double dragOffsetY;
    private double dragX;
    private double dragY;

    public BattleController(StageConfig config) {
        this.config = config;
        this.generator = Generators.forKind(config.tileKind());
        this.locked = new LockedTiles(config.lockedTilesInitial(), config.correctCountToUnlock());
        this.layout = new BoardLayout(config.tileCount(), config.minTileWidth(),
                config.tileHeight(), Theme.SPACING, Theme.MARGIN_X, Theme.GRID_Y_OFFSET);
        this.renderer = new BoardRenderer(config, layout,
                Painters.forKind(config.tileKind(), config), INITIAL_CANVAS_WIDTH);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MusicPlayer.getInstance().play(config.musicPath());

        BattleSceneView.styleScene(topPanel, bottomPanel, battleArea);
        view = new BattleSceneView(config, topPanel, battleArea, new BattleSceneView.Actions(
                this::restart, this::back, this::leave));
        navigator = new SceneNavigator(battleArea);

        canvas = renderer.canvas();
        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);
        bottomPanel.getChildren().add(canvas);
        bottomPanel.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            renderer.resize(newWidth.doubleValue());
            draw();
        });

        newLevel();
        draw();
        startLoop();
    }

    // =========================================================================
    // LEVEL LIFECYCLE
    // =========================================================================

    /** Generates a fresh board and re-applies the stage's opening rules. */
    private void newLevel() {
        Tile[] tiles = generator.generate(config.tileCount(), random);
        board = new Board(tiles, config.sortModePolicy().resolve(random));
        if (config.hasLockedTiles()) locked.reset(board, random);
    }

    private void draw() {
        renderer.render(board, arraysSolved);
    }

    // =========================================================================
    // GAME LOOP
    // =========================================================================

    private void startLoop() {
        stopLoop();
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOver) return;
                if (lastNanoTime < 0) {
                    lastNanoTime = now;
                    return;
                }
                double delta = (now - lastNanoTime) / 1_000_000_000.0;
                lastNanoTime = now;

                health.drain(Theme.DRAIN_PER_SECOND, delta);
                view.setHealth(health.value());
                if (health.isDepleted()) {
                    finish(false);
                    return;
                }

                if (!animating && !dragging && checkForWin()) {
                    arraysSolved++;
                    if (arraysSolved >= config.arraysToWin()) {
                        finish(true);
                    } else {
                        newLevel();
                        draw();
                    }
                }
            }
        };
        gameLoop.start();
    }

    private void stopLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
    }

    /**
     * Evaluates the board for a win.
     *
     * <p>Locked tiles that have landed in the right place reveal themselves here,
     * as an explicit step. The original did this from inside {@code isSorted()},
     * which meant a query method was quietly mutating the board on every frame.
     */
    private boolean checkForWin() {
        if (config.hasLockedTiles() && locked.revealCorrectlyPositioned(board) > 0) {
            draw();
        }
        return board.isSorted();
    }

    // =========================================================================
    // INPUT
    // =========================================================================

    private void onMousePressed(javafx.scene.input.MouseEvent e) {
        if (e.getButton() != MouseButton.PRIMARY || animating || gameOver) return;
        int index = layout.hitTest(e.getX(), e.getY());
        if (index == -1) return;

        dragging = true;
        dragIndex = index;
        dragOffsetX = e.getX() - layout.x(index);
        dragOffsetY = e.getY() - layout.y(index);
        dragX = layout.x(index);
        dragY = layout.y(index);
    }

    private void onMouseDragged(javafx.scene.input.MouseEvent e) {
        if (!dragging || animating || gameOver) return;
        dragX = e.getX() - dragOffsetX;
        dragY = e.getY() - dragOffsetY;
        draw();
    }

    private void onMouseReleased(javafx.scene.input.MouseEvent e) {
        if (!dragging || animating || gameOver) return;
        dragging = false;

        int target = layout.nearestIndex(e.getX(), e.getY());
        if (target != -1 && target != dragIndex) {
            // Judge the move before it happens, then animate it.
            animateSwap(dragIndex, target, board.isGoodSwap(dragIndex, target));
        } else {
            dragIndex = -1;
            draw();
        }
    }

    private void detachInput() {
        canvas.setOnMousePressed(null);
        canvas.setOnMouseDragged(null);
        canvas.setOnMouseReleased(null);
    }

    // =========================================================================
    // SWAP ANIMATION
    // =========================================================================

    private void animateSwap(int from, int to, boolean goodMove) {
        animating = true;

        double fromX = layout.x(from);
        double fromY = layout.y(from);
        double toX = layout.x(to);
        double toY = layout.y(to);

        Duration total = Duration.millis(Theme.SWAP_MILLIS);
        Timeline timeline = new Timeline();
        Board snapshot = board;
        int progress = arraysSolved;

        for (int i = 0; i <= Theme.SWAP_FRAMES; i++) {
            double t = Effects.easeInOutQuad(i / (double) Theme.SWAP_FRAMES);
            double ax = fromX + (toX - fromX) * t;
            double ay = fromY + (toY - fromY) * t;
            double bx = toX + (fromX - toX) * t;
            double by = toY + (fromY - toY) * t;

            timeline.getKeyFrames().add(new KeyFrame(
                    total.multiply(i / (double) Theme.SWAP_FRAMES), e -> {
                        renderer.drawHeader(snapshot, progress);
                        for (int j = 0; j < snapshot.size(); j++) {
                            if (j == from || j == to) continue;
                            renderer.paint(snapshot, j, layout.x(j), layout.y(j), false, false);
                        }
                        renderer.paint(snapshot, from, ax, ay, false, false);
                        renderer.paint(snapshot, to, bx, by, false, false);
                    }));
        }

        play(timeline, () -> {
            board.swap(from, to);
            dragIndex = -1;
            animating = false;
            if (goodMove) {
                rewardGoodMove(from, to);
            } else {
                punishBadMove();
            }
            draw();
        });
    }

    private void rewardGoodMove(int from, int to) {
        int correctCount = board.correctAfterSwap(from, to);

        health.gain(Theme.GAIN_PER_CORRECT, correctCount);
        view.setHealth(health.value());

        if (config.hasLockedTiles()) {
            List<Integer> revealed = locked.bankCorrectMove(board, correctCount);
            for (int index : revealed) animateReveal(index);
        }

        Effects.shake(view.playerBox(), 5.0, 3, 320);
    }

    private void punishBadMove() {
        health.set(health.value() - config.lossPerWrong());
        view.setHealth(health.value());

        Effects.shake(view.enemyBox(), 5.0, 3, 320);
        if (health.isDepleted() && !gameOver) finish(false);
    }

    /** A short shudder to draw the eye to a tile that just came out of hiding. */
    private void animateReveal(int index) {
        Board snapshot = board;
        int progress = arraysSolved;
        double baseX = layout.x(index);
        double y = layout.y(index);

        Timeline timeline = new Timeline();
        for (int frame = 0; frame <= 10; frame++) {
            double offset = (frame % 2 == 0) ? 4.0 : -4.0;
            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.millis(200.0 * frame / 10.0), e -> {
                        renderer.drawHeader(snapshot, progress);
                        for (int j = 0; j < snapshot.size(); j++) {
                            if (j == index) continue;
                            renderer.paint(snapshot, j, layout.x(j), layout.y(j),
                                    BoardRenderer.isCorrect(snapshot, j), false);
                        }
                        renderer.paint(snapshot, index, baseX + offset, y,
                                BoardRenderer.isCorrect(snapshot, index), false);
                    }));
        }
        play(timeline, this::draw);
    }

    // =========================================================================
    // ANIMATION BOOKKEEPING
    // =========================================================================

    /**
     * Starts a timeline and remembers it so {@link #stopAnimations()} can cancel
     * anything still in flight when the player leaves or the battle ends.
     */
    private void play(Timeline timeline, Runnable onDone) {
        running.add(timeline);
        timeline.setOnFinished(e -> {
            running.remove(timeline);
            if (onDone != null) onDone.run();
        });
        timeline.play();
    }

    /** Cancels every in-flight timeline so nothing keeps drawing after a scene change. */
    private void stopAnimations() {
        for (Timeline timeline : running) {
            timeline.stop();
        }
        running.clear();
        animating = false;
        dragging = false;
        dragIndex = -1;
    }

    // =========================================================================
    // END OF BATTLE
    // =========================================================================

    private void finish(boolean won) {
        gameOver = true;
        stopLoop();
        stopAnimations();
        detachInput();

        if (won) SaveManager.unlockNext(config.id());

        EndOverlay.show(battleArea, config, won,
                this::restart,
                won ? () -> navigator.playPostVictoryDialogue(config, battleArea) : null);
    }

    private void restart() {
        view.clearBattlefield();
        stopAnimations();
        gameOver = false;
        lastNanoTime = -1;
        arraysSolved = 0;

        health.reset();
        view.setHealth(health.value());

        canvas.setOnMousePressed(this::onMousePressed);
        canvas.setOnMouseDragged(this::onMouseDragged);
        canvas.setOnMouseReleased(this::onMouseReleased);

        newLevel();
        draw();
        startLoop();
    }

    private void back() {
        stopLoop();
        stopAnimations();
        navigator.toLevelScreen();
    }

    private void leave() {
        stopLoop();
        stopAnimations();
        Platform.exit();
    }
}
