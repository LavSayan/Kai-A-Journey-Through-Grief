package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.SortMode;
import com.example.kaiajourneythroughgrief.battle.model.Board;
import com.example.kaiajourneythroughgrief.battle.model.Tile;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EquationGeneratorTest {

    private final TileGenerator generator = new EquationGenerator();

    /** Solves the drawn equation so the label and the hidden value can be cross-checked. */
    private static int solve(String label) {
        String s = label.replace("−", "-");          // "x + 6 = 11" or "x - 6 = 11"
        int equals = s.indexOf('=');
        String rhs = s.substring(equals + 1).trim();
        int c = Integer.parseInt(rhs);

        int op = s.indexOf('+') > 0 ? s.indexOf('+') : s.indexOf('-');
        int b = Integer.parseInt(s.substring(op + 1, equals).trim());

        return s.charAt(op) == '+' ? c - b : c + b;
    }

    @Test
    void producesTheRequestedNumberOfTiles() {
        assertEquals(8, generator.generate(8, new Random(1)).length);
    }

    @Test
    void everySolutionIsAPositiveIntegerInRange() {
        Random random = new Random(2);
        for (int trial = 0; trial < 5_000; trial++) {
            Tile t = EquationGenerator.makeEquation(random);
            assertTrue(t.sortValue() >= 1 && t.sortValue() <= 99,
                    "x = " + t.sortValue() + " (" + t.label() + ") out of range");
        }
    }

    @Test
    void theDrawnEquationSolvesToTheHiddenSortValue() {
        Random random = new Random(11);
        for (int trial = 0; trial < 5_000; trial++) {
            Tile t = EquationGenerator.makeEquation(random);
            assertEquals(t.sortValue(), solve(t.label()),
                    "label " + t.label() + " does not solve to " + t.sortValue());
        }
    }

    @Test
    void bothFormsShowUp() {
        Random random = new Random(5);
        boolean addition = false;
        boolean subtraction = false;
        for (int trial = 0; trial < 2_000 && !(addition && subtraction); trial++) {
            String label = EquationGenerator.makeEquation(random).label();
            addition |= label.contains("+");
            subtraction |= label.contains("−");
        }
        assertTrue(addition, "no addition was generated");
        assertTrue(subtraction, "no subtraction was generated");
    }

    @Test
    void boardsArriveUnsolved() {
        Random random = new Random(3);
        for (int trial = 0; trial < 1_000; trial++) {
            assertFalse(new Board(generator.generate(8, random), SortMode.ASCENDING).isSorted());
        }
    }
}
