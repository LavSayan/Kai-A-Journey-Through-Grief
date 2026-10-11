package com.example.kaiajourneythroughgrief.battle;

/**
 * What a tile on the board actually shows the player. The underlying game is
 * always "order these tiles by their hidden sort value" — only the label differs.
 */
public enum TileKind {

    /** A plain number, e.g. {@code 42}. */
    NUMBER,

    /** A maths expression whose result is hidden, e.g. {@code 5+3}. */
    EXPRESSION,

    /** An equation with an unknown x, e.g. {@code x + 6 = 11}. */
    EQUATION
}
