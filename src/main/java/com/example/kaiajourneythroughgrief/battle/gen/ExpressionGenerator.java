package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.model.Tile;

import java.util.Random;

/**
 * Stage 3: tiles show a maths expression and the player sorts by the hidden
 * result. Uses +, − and ×, with results guaranteed to land in 10–99.
 */
public final class ExpressionGenerator implements TileGenerator {

    public static final int MIN_RESULT = 10;
    public static final int MAX_RESULT = 99;

    private static final String MINUS = "−";   // −
    private static final String TIMES = "×";   // ×
    private static final int MAX_ATTEMPTS = 200;

    @Override
    public Tile[] generate(int count, Random random) {
        Tile[] pool = new Tile[count];
        for (int i = 0; i < count; i++) pool[i] = makeExpression(random);
        return NumberGenerator.reorder(pool, random);
    }

    /** Builds one expression whose evaluated result is in [10, 99]. */
    public static Tile makeExpression(Random random) {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int op = random.nextInt(3);          // 0 = +, 1 = −, 2 = ×
            int a;
            int b;
            int result;
            String label;

            switch (op) {
                case 0 -> {                        // a + b
                    a = random.nextInt(89) + 1;   // 1–89
                    b = random.nextInt(89) + 1;
                    result = a + b;
                    label = a + "+" + b;
                }
                case 1 -> {                        // a − b, result picked first
                    result = random.nextInt(90) + MIN_RESULT;
                    b = random.nextInt(20) + 1;  // 1–20
                    a = result + b;
                    label = a + MINUS + b;
                }
                default -> {                       // a × b
                    a = random.nextInt(9) + 2;    // 2–10
                    b = random.nextInt(9) + 2;
                    result = a * b;
                    label = a + TIMES + b;
                }
            }

            if (result >= MIN_RESULT && result <= MAX_RESULT) {
                return new Tile(result, label);
            }
        }

        // Unreachable in practice, but a fallback keeps generation total.
        int a = random.nextInt(40) + 10;
        int b = random.nextInt(40) + 10;
        return new Tile(a + b, a + "+" + b);
    }
}
