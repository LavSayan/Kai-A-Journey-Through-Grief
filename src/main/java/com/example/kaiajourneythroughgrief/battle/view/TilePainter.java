package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.Theme;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Draws one tile onto the board canvas. One implementation per {@code TileKind},
 * so a stage only chooses a painter rather than branching inside the draw loop.
 */
public interface TilePainter {

    void paint(TilePaintContext ctx);

    /**
     * The card body shared by every tile style: background, border, accent bar.
     *
     * @param accentInset how far the accent bar is inset from the top and
     *                    bottom edges — taller two-line tiles inset it further
     */
    static void paintCard(TilePaintContext ctx, Color theme, double accentInset) {
        GraphicsContext gc = ctx.gc();
        double w = ctx.width();
        double h = ctx.height();

        gc.setFill(Theme.CARD_BG);
        gc.fillRoundRect(ctx.x(), ctx.y(), w, h, Theme.CARD_RADIUS, Theme.CARD_RADIUS);

        double borderAlpha = ctx.isDragged() ? 1.0 : (ctx.isCorrect() ? 0.55 : 0.7);
        double accentAlpha = ctx.isDragged() ? 1.0 : (ctx.isCorrect() ? 0.4 : 0.85);

        gc.setStroke(Theme.fade(theme, borderAlpha));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(ctx.x(), ctx.y(), w, h, Theme.CARD_RADIUS, Theme.CARD_RADIUS);

        gc.setFill(Theme.fade(theme, accentAlpha));
        gc.fillRoundRect(ctx.x() + 1, ctx.y() + accentInset,
                Theme.ACCENT_W, h - accentInset * 2, 2, 2);
    }
}
