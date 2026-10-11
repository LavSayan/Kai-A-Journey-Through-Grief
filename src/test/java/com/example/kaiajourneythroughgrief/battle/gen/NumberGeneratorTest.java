package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.SortMode;
import com.example.kaiajourneythroughgrief.battle.model.Board;
import com.example.kaiajourneythroughgrief.battle.model.Tile;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class NumberGeneratorTest {

    private final TileGenerator generator = new NumberGenerator();

    @Test
    void producesTheRequestedNumberOfTiles() {
        for (int count : new int[]{8, 10}) {
            assertEquals(count, generator.generate(count, new Random(1)).length);
        }
    }

    @Test
    void everyValueIsInRangeAndMatchesItsLabel() {
        Random random = new Random(2);
        for (int trial = 0; trial < 2_000; trial++) {
            for (Tile t : generator.generate(10, random)) {
                assertTrue(t.sortValue() >= NumberGenerator.MIN_VALUE
                                && t.sortValue() <= NumberGenerator.MAX_VALUE,
                        "value " + t.sortValue() + " out of range");
                assertEquals(String.valueOf(t.sortValue()), t.label());
            }
        }
    }

    @Test
    void boardsArriveUnsolved() {
        Random random = new Random(3);
        for (int trial = 0; trial < 1_000; trial++) {
            Tile[] tiles = generator.generate(8, random);
            assertFalse(new Board(tiles, SortMode.ASCENDING).isSorted(),
                    "a level was generated already solved");
        }
    }
}
