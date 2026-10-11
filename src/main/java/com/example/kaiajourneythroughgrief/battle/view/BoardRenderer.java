package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.SortMode;
import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.Theme;
import com.example.kaiajourneythroughgrief.battle.model.Board;
import com.example.kaiajourneythroughgrief.battle.model.BoardLayout;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Draws the board canvas: the objective header strip plus one tile per slot.
 *
 * <p>The header and the tiles both read their colours from the shared
 * {@link Theme} palette rather than re-parsing hex strings on every frame.
 */
public final class BoardRenderer {

    private final StageConfig config;
    private final BoardLayout layout;
    private final TilePainter painter;

    private final Canvas canvas;
    private final GraphicsContext gc;

    public BoardRenderer(StageConfig config, BoardLayout layout, TilePainter painter,
                         double initialWidth) {
        this.config = config;
        this.layout = layout;
        this.painter = painter;
        this.canvas = new Canvas(initialWidth, config.canvasHeight());
        this.gc = canvas.getGraphicsContext2D();
        layout.update(initialWidth, config.canvasHeight());
    }

    public Canvas canvas() {
        return canvas;
    }

    /** Resizes the backing canvas and re-flows the tile grid. */
    public void resize(double width) {
        canvas.setWidth(width);
        layout.update(width, config.canvasHeight());
    }

    public void render(Board board, int arraysSolved) {
        drawHeader(board, arraysSolved);
        for (int i = 0; i < board.size(); i++) {
            paint(board, i, layout.x(i), layout.y(i), isCorrect(board, i), false);
        }
    }

    /**
     * A locked tile is never "correct" — the player has not earned the reveal.
     */
    public static boolean isCorrect(Board board, int index) {
        return !board.isLocked(index) && board.isPositionCorrect(index);
    }

    public void paint(Board board, int index, double x, double y,
                      boolean correct, boolean dragged) {
        painter.paint(new TilePaintContext(gc, layout, board, index, x, y, correct, dragged));
    }

    /** Draws the header strip and the background behind the tiles. */
    public void drawHeader(Board board, int arraysSolved) {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        SortMode mode = board.mode();
        Color theme = config.colorFor(mode);

        gc.setFill(Theme.TITLE_BAR);
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Theme.BAND_DIVIDER);
        gc.setLineWidth(1);
        gc.strokeLine(0, 0, w, 0);

        gc.setFill(Theme.META_TEXT);
        gc.setFont(Theme.META_8);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("OBJECTIVE", 14, 14);

        gc.setFill(Theme.fade(theme, 0.85));
        gc.setFont(Theme.OBJECTIVE_12);
        gc.fillText(config.objectiveLabel(mode), 14, 27);

        gc.setStroke(Theme.fade(theme, 0.4));
        gc.setLineWidth(1);
        gc.strokeLine(14, 30, 14 + config.objectiveRuleWidth(), 30);

        if (config.arraysToWin() > 1) {
            gc.setFill(Theme.META_TEXT);
            gc.setFont(Theme.PROGRESS_9);
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.fillText((arraysSolved + 1) + "  /  " + config.arraysToWin(), w - 14, 16);
        }
    }
}
