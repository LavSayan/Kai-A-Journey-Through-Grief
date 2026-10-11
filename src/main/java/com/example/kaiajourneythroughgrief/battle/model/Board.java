package com.example.kaiajourneythroughgrief.battle.model;

import com.example.kaiajourneythroughgrief.battle.SortMode;

import java.util.Arrays;

/**
 * The sorting board: an ordered list of tiles plus the hidden-value target
 * order the player is trying to reach.
 *
 * <p>This class is deliberately free of any JavaFX dependency so the sorting
 * rules can be unit tested without starting a UI toolkit.
 */
public final class Board {

    private Tile[] tiles;
    private boolean[] locked;
    private SortMode mode;

    /**
     * The values in their finished order. Swapping never changes the multiset of
     * values, so this only has to be rebuilt when a new level is generated —
     * not on every swap. The original code re-sorted an {@code int[]} once per
     * tile per frame to answer this same question.
     */
    private int[] target;

    public Board(Tile[] tiles, SortMode mode) {
        setTiles(tiles);
        setMode(mode);
    }

    // ── State ──────────────────────────────────────────────────────────────

    public int size() {
        return tiles.length;
    }

    public Tile tile(int index) {
        return tiles[index];
    }

    public int valueAt(int index) {
        return tiles[index].sortValue();
    }

    public SortMode mode() {
        return mode;
    }

    public void setMode(SortMode mode) {
        this.mode = mode;
        rebuildTarget();
    }

    /** Replaces the board with a freshly generated level. */
    public void setTiles(Tile[] tiles) {
        this.tiles = tiles.clone();
        this.locked = new boolean[tiles.length];
        rebuildTarget();
    }

    // ── Locked tiles ───────────────────────────────────────────────────────

    public boolean isLocked(int index) {
        return locked[index];
    }

    public void setLocked(int index, boolean value) {
        locked[index] = value;
    }

    public void clearLocks() {
        Arrays.fill(locked, false);
    }

    public void swapLocks(int a, int b) {
        boolean tmp = locked[a];
        locked[a] = locked[b];
        locked[b] = tmp;
    }

    /** What the player sees on a tile — {@code "?"} while its value is hidden. */
    public String display(int index) {
        return locked[index] ? "?" : tiles[index].label();
    }

    // ── Sorting rules ──────────────────────────────────────────────────────

    /**
     * Swaps two tiles, carrying their locked state along with them.
     */
    public void swap(int a, int b) {
        Tile tmp = tiles[a];
        tiles[a] = tiles[b];
        tiles[b] = tmp;
        swapLocks(a, b);
    }

    /** True when every adjacent pair is already in the right order. */
    public boolean isSorted() {
        for (int i = 1; i < tiles.length; i++) {
            if (mode.isInverted(valueAt(i - 1), valueAt(i))) return false;
        }
        return true;
    }

    /** True when the tile at {@code index} holds the value it should end up with. */
    public boolean isPositionCorrect(int index) {
        return valueAt(index) == target[index];
    }

    /** How many of the two given positions land correctly. Call <em>after</em> {@link #swap}. */
    public int correctAfterSwap(int a, int b) {
        int count = 0;
        if (isPositionCorrect(a)) count++;
        if (isPositionCorrect(b)) count++;
        return count;
    }

    // ── Move quality ───────────────────────────────────────────────────────

    /**
     * Change in inversion count caused by swapping {@code a} and {@code b},
     * without performing the swap.
     *
     * <p>Only pairs involving {@code a} or {@code b} can change. For positions
     * outside the span between them the two values simply trade places, so their
     * contributions cancel; only the elements strictly between {@code a} and
     * {@code b}, plus the pair itself, actually move. That makes this O(|a-b|)
     * instead of the O(n²) "count inversions twice and compare" the original
     * code did on every mouse release — and it needs no copy of the board.
     */
    public int inversionDeltaForSwap(int a, int b) {
        if (a == b) return 0;
        int lo = Math.min(a, b);
        int hi = Math.max(a, b);
        int loValue = valueAt(lo);
        int hiValue = valueAt(hi);

        int delta = 0;
        for (int k = lo + 1; k < hi; k++) {
            int v = valueAt(k);
            // Pair (lo, k) now compares hiValue instead of loValue...
            delta += booleanToInt(mode.isInverted(hiValue, v)) - booleanToInt(mode.isInverted(loValue, v));
            // ...and pair (k, hi) now compares loValue instead of hiValue.
            delta += booleanToInt(mode.isInverted(v, loValue)) - booleanToInt(mode.isInverted(v, hiValue));
        }

        // The pair (lo, hi) itself now holds (hiValue, loValue).
        return delta
                + booleanToInt(mode.isInverted(hiValue, loValue))
                - booleanToInt(mode.isInverted(loValue, hiValue));
    }

    private static int booleanToInt(boolean b) {
        return b ? 1 : 0;
    }

    /** A swap is a good move when it strictly reduces the inversion count. */
    public boolean isGoodSwap(int a, int b) {
        return inversionDeltaForSwap(a, b) < 0;
    }

    /**
     * Brute-force inversion count. Kept for tests, which use it to verify the
     * O(1) delta above.
     */
    public int countInversions() {
        int inv = 0;
        for (int i = 0; i < tiles.length - 1; i++) {
            for (int j = i + 1; j < tiles.length; j++) {
                if (mode.isInverted(valueAt(i), valueAt(j))) inv++;
            }
        }
        return inv;
    }

    /** The values in their finished order. Exposed for tests. */
    public int[] targetOrder() {
        return target.clone();
    }

    private void rebuildTarget() {
        target = new int[tiles.length];
        for (int i = 0; i < tiles.length; i++) target[i] = tiles[i].sortValue();
        Arrays.sort(target);
        if (mode == SortMode.DESCENDING) {
            for (int i = 0, j = target.length - 1; i < j; i++, j--) {
                int tmp = target[i];
                target[i] = target[j];
                target[j] = tmp;
            }
        }
    }
}
