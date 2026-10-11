package com.example.kaiajourneythroughgrief.battle;

/**
 * How a stage picks the sort order for each freshly generated board.
 */
public enum SortModePolicy {

    /** Always ascending — the stage's theme colour is fixed. */
    FIXED_ASCENDING,

    /** Ascending or descending, re-rolled for every board. */
    RANDOM;

    public SortMode resolve(java.util.Random random) {
        return this == FIXED_ASCENDING
                ? SortMode.ASCENDING
                : (random.nextBoolean() ? SortMode.ASCENDING : SortMode.DESCENDING);
    }
}
