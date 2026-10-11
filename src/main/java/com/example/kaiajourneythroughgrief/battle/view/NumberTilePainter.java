package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.Theme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Stages 1, 2 and 5: a plain number, or {@code ?} while the value is hidden.
 *
 * <p>The theme colour follows the board's sort direction on the stages that
 * randomise it, and a correctly placed tile gets the green tick in the corner.
 */
public final class NumberTilePainter implements TilePainter {

    private final StageConfig config;

    public NumberTilePainter(StageConfig config) {
        this.config = config;
    }

    @Override
    public void paint(TilePaintContext ctx) {
        GraphicsContext gc = ctx.gc();
        boolean locked = ctx.isLocked();
        Color theme = locked ? Theme.LOCKED
                : (ctx.isCorrect() ? Theme.COMPLETION : config.colorFor(ctx.board().mode()));

        TilePainter.paintCard(ctx, theme, 8);

        gc.setFill(ctx.isCorrect() ? Theme.COMPLETION
                : (locked ? Theme.LOCKED : Theme.ACTIVE_TITLE));
        gc.setFont(Theme.TILE_VALUE_20);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(ctx.label(), ctx.centerX(), ctx.y() + ctx.height() / 2 + 7);

        if (ctx.isCorrect()) {
            gc.setFill(Theme.fade(Theme.COMPLETION, 0.7));
            gc.setFont(Theme.TICK_11);
            gc.setTextAlign(TextAlignment.LEFT);
            gc.fillText("✓", ctx.x() + ctx.width() - 13, ctx.y() + 13);
        }
    }
}
