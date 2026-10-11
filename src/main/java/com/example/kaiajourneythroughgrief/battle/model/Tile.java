package com.example.kaiajourneythroughgrief.battle.model;

/**
 * One tile on the sorting board.
 *
 * @param sortValue the hidden value the player must order by
 * @param label     what is actually drawn on the tile
 */
public record Tile(int sortValue, String label) {

    public static Tile number(int value) {
        return new Tile(value, String.valueOf(value));
    }
}
