package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.TileKind;

/**
 * Chooses the tile painter that matches a stage's tile style.
 */
public final class Painters {

    private Painters() {}

    public static TilePainter forKind(TileKind kind, StageConfig config) {
        return switch (kind) {
            case NUMBER -> new NumberTilePainter(config);
            case EXPRESSION -> new ExpressionTilePainter(config);
            case EQUATION -> new EquationTilePainter(config);
        };
    }
}
