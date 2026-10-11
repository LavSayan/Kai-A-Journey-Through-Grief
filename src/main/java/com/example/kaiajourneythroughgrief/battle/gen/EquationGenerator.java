package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.model.Tile;

import java.util.Random;

/**
 * Stage 4: tiles show an equation with an unknown x and the player sorts by the
 * hidden solution.
 *
 * <pre>
 *   addition:    x + b = c   with b in [1, 20], c = x + b
 *   subtraction: x − b = c   with b in [1, x−1], c = x − b   (always ≥ 1)
 * </pre>
 *
 * x itself is always a positive integer in [1, 99].
 */
public final class EquationGenerator implements TileGenerator {

    private static final String MINUS = "−";   // −

    @Override
    public Tile[] generate(int count, Random random) {
        Tile[] pool = new Tile[count];
        for (int i = 0; i < count; i++) pool[i] = makeEquation(random);
        return NumberGenerator.reorder(pool, random);
    }

    /** Builds one equation of the form {@code x + b = c} or {@code x − b = c}. */
    public static Tile makeEquation(Random random) {
        if (random.nextBoolean()) {
            int x = random.nextInt(99) + 1;      // x in [1, 99]
            int b = random.nextInt(20) + 1;      // b in [1, 20]
            int c = x + b;
            return new Tile(x, "x + " + b + " = " + c);
        }
        // x must be at least 2 so b can be >= 1 and c stays >= 1
        int x = random.nextInt(98) + 2;          // x in [2, 99]
        int b = random.nextInt(x - 1) + 1;       // b in [1, x−1]
        int c = x - b;
        return new Tile(x, "x " + MINUS + " " + b + " = " + c);
    }
}
