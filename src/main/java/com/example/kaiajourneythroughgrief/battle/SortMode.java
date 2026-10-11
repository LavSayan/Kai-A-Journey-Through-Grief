package com.example.kaiajourneythroughgrief.battle;

/**
 * The order the player must arrange the board into.
 */
public enum SortMode {

    ASCENDING,
    DESCENDING;

    /** True when {@code left} belongs before {@code right} in this ordering. */
    public boolean precedes(int left, int right) {
        return this == ASCENDING ? left < right : left > right;
    }

    /** True when the pair (left before right) counts as an inversion. */
    public boolean isInverted(int left, int right) {
        return this == ASCENDING ? left > right : left < right;
    }

    /** Label shown in the objective line of the board canvas. */
    public String objectiveLabel() {
        return this == ASCENDING ? "Sort Ascending" : "Sort Descending";
    }
}
