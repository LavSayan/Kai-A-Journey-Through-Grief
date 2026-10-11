package com.example.kaiajourneythroughgrief.battle.gen;

import com.example.kaiajourneythroughgrief.battle.model.Tile;

import java.util.Random;

/**
 * Stages 1, 2 and 5: plain random numbers between 1 and 100.
 */
public final class NumberGenerator implements TileGenerator {

    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 100;

    @Override
    public Tile[] generate(int count, Random random) {
        Tile[] pool = new Tile[count];
        for (int i = 0; i < count; i++) {
            pool[i] = Tile.number(random.nextInt(MAX_VALUE) + MIN_VALUE);
        }
        return reorder(pool, random);
    }

    static Tile[] reorder(Tile[] pool, Random random) {
        int[] order = Shuffler.unsortedPermutation(pool.length, random);
        Tile[] result = new Tile[pool.length];
        for (int i = 0; i < pool.length; i++) result[i] = pool[order[i]];
        return result;
    }
}
