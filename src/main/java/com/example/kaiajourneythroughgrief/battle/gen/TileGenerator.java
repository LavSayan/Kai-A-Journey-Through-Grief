package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.model.Tile;

import java.util.Random;

/**
 * Produces the raw set of tiles for one level. Shuffling into puzzle order is
 * the generator's job too, so every generator is guaranteed to hand back a
 * board that still needs work.
 */
public interface TileGenerator {

    /** @return exactly {@code count} tiles in unsolved order */
    Tile[] generate(int count, Random random);
}
