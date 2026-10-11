package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.Theme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

/**
 * Stage 3: a maths expression with its result hidden underneath.
 *
 * <pre>
 *   ┌──────────────────┐
 *   │      5+3         │   the expression, large
 *   │      = ?         │   the prompt to evaluate
 *   └──────────────────┘
 *
 * Once it lands in the right place the answer is revealed:
 *   │      = 8 ✓       │
 * </pre>
 */
public final class ExpressionTilePainter implements TilePainter {

    private final StageConfig config;

    public ExpressionTilePainter(StageConfig config) {
        this.config = config;
    }

    @Override
    public void paint(TilePaintContext ctx) {
        GraphicsContext gc = ctx.gc();
        Color theme = ctx.isCorrect() ? Theme.COMPLETION : config.themeColor();

        TilePainter.paintCard(ctx, theme, 8);

        gc.setFill(ctx.isCorrect() ? Theme.COMPLETION : Theme.ACTIVE_TITLE);
        gc.setFont(Theme.TILE_EXPR_18);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(ctx.label(), ctx.centerX(), ctx.y() + ctx.height() / 2 + 2);

        if (ctx.isCorrect()) {
            gc.setFill(Theme.fade(Theme.COMPLETION, 0.75));
            gc.setFont(Theme.META_9);
            gc.fillText("= " + ctx.value() + " ✓", ctx.centerX(), ctx.y() + ctx.height() - 8);
        } else {
            gc.setFill(Theme.fade(theme, 0.45));
            gc.setFont(Theme.META_9_PLAIN);
            gc.fillText("= ?", ctx.centerX(), ctx.y() + ctx.height() - 8);
        }
    }
}
