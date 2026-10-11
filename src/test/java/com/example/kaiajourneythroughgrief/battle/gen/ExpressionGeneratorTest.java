package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.SortMode;
import com.example.kaiajourneythroughgrief.battle.model.Board;
import com.example.kaiajourneythroughgrief.battle.model.Tile;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionGeneratorTest {

    private final TileGenerator generator = new ExpressionGenerator();

    /** Evaluates the drawn label so the label and the hidden value can be cross-checked. */
    private static int evaluate(String label) {
        String s = label.replace("−", "-").replace("×", "*");

        int op = -1;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '+' || c == '-' || c == '*') { op = i; break; }
        }
        int a = Integer.parseInt(s.substring(0, op));
        int b = Integer.parseInt(s.substring(op + 1));
        return switch (s.charAt(op)) {
            case '+' -> a + b;
            case '-' -> a - b;
            default -> a * b;
        };
    }

    @Test
    void producesTheRequestedNumberOfTiles() {
        assertEquals(8, generator.generate(8, new Random(1)).length);
    }

    @Test
    void everyResultLandsInTheTargetRange() {
        Random random = new Random(2);
        for (int trial = 0; trial < 5_000; trial++) {
            Tile t = ExpressionGenerator.makeExpression(random);
            assertTrue(t.sortValue() >= ExpressionGenerator.MIN_RESULT
                            && t.sortValue() <= ExpressionGenerator.MAX_RESULT,
                    "result " + t.sortValue() + " (" + t.label() + ") out of range");
        }
    }

    @Test
    void theDrawnLabelEvaluatesToTheHiddenSortValue() {
        Random random = new Random(11);
        for (int trial = 0; trial < 5_000; trial++) {
            Tile t = ExpressionGenerator.makeExpression(random);
            assertEquals(t.sortValue(), evaluate(t.label()),
                    "label " + t.label() + " does not evaluate to " + t.sortValue());
        }
    }

    @Test
    void allThreeOperatorsShowUp() {
        Random random = new Random(5);
        boolean plus = false;
        boolean minus = false;
        boolean times = false;
        for (int trial = 0; trial < 2_000 && !(plus && minus && times); trial++) {
            String label = ExpressionGenerator.makeExpression(random).label();
            plus  |= label.contains("+");
            minus |= label.contains("−");
            times |= label.contains("×");
        }
        assertTrue(plus, "no addition was generated");
        assertTrue(minus, "no subtraction was generated");
        assertTrue(times, "no multiplication was generated");
    }

    @Test
    void boardsArriveUnsolved() {
        Random random = new Random(3);
        for (int trial = 0; trial < 1_000; trial++) {
            assertFalse(new Board(generator.generate(8, random), SortMode.ASCENDING).isSorted());
        }
    }
}
