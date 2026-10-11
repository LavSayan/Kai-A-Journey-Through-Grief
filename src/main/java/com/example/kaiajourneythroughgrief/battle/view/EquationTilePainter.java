package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.Theme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Stage 4: a two-line equation tile with an unknown x.
 *
 * <pre>
 *   ┌──────────────────────────┐
 *   │    x + 6 = 11            │  ← equation, centred, bold
 *   │    x = ?                 │  ← unsolved hint (dim)
 *   └──────────────────────────┘
 *
 * Once the tile is in the correct sorted position:
 *   │    x = 5  ✓              │  ← green, reveals the answer
 * </pre>
 */
public final class EquationTilePainter implements TilePainter {

    private final StageConfig config;

    public EquationTilePainter(StageConfig config) {
        this.config = config;
    }

    @Override
    public void paint(TilePaintContext ctx) {
        GraphicsContext gc = ctx.gc();
        Color theme = ctx.isCorrect() ? Theme.COMPLETION : config.themeColor();

        TilePainter.paintCard(ctx, theme, 10);

        gc.setFill(ctx.isCorrect() ? Theme.COMPLETION : Theme.ACTIVE_TITLE);
        gc.setFont(Theme.TILE_EQ_15);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(ctx.label(), ctx.centerX(), ctx.y() + ctx.height() / 2 - 4);

        if (ctx.isCorrect()) {
            gc.setFill(Theme.fade(Theme.COMPLETION, 0.85));
            gc.setFont(Theme.META_10);
            gc.fillText("x = " + ctx.value() + "  ✓", ctx.centerX(), ctx.y() + ctx.height() / 2 + 14);
        } else {
            gc.setFill(Theme.fade(theme, 0.40));
            gc.setFont(Theme.META_10_PLAIN);
            gc.fillText("x = ?", ctx.centerX(), ctx.y() + ctx.height() / 2 + 14);
        }
    }
}
