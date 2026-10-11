package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.TileKind;

/**
 * Maps a stage's {@link TileKind} onto the generator that builds its tiles.
 * Generators are stateless, so one instance of each is enough.
 */
public final class Generators {

    private Generators() {}

    private static final TileGenerator NUMBERS = new NumberGenerator();
    private static final TileGenerator EXPRESSIONS = new ExpressionGenerator();
    private static final TileGenerator EQUATIONS = new EquationGenerator();

    public static TileGenerator forKind(TileKind kind) {
        return switch (kind) {
            case NUMBER -> NUMBERS;
            case EXPRESSION -> EXPRESSIONS;
            case EQUATION -> EQUATIONS;
        };
    }
}
