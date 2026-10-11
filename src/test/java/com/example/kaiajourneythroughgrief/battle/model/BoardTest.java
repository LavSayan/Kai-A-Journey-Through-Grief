package com.example.kaiajourneythroughgrief.battle.model;

import com.example.kaiajourneythroughgrief.battle.SortMode;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private static Tile[] tiles(int... values) {
        Tile[] t = new Tile[values.length];
        for (int i = 0; i < values.length; i++) t[i] = Tile.number(values[i]);
        return t;
    }

    // ── Win condition ──────────────────────────────────────────────────────

    @Test
    void ascendingBoardIsNotSortedUntilAscending() {
        Board board = new Board(tiles(3, 1, 2), SortMode.ASCENDING);
        assertFalse(board.isSorted());
        board.swap(1, 0);
        board.swap(2, 1);
        assertTrue(board.isSorted());
    }

    @Test
    void descendingBoardIsNotSortedUntilDescending() {
        Board board = new Board(tiles(3, 2, 1), SortMode.DESCENDING);
        assertTrue(board.isSorted());
        board.swap(0, 2);
        assertFalse(board.isSorted());
    }

    @Test
    void changingModeFlipsTheWinCondition() {
        Board board = new Board(tiles(1, 2, 3), SortMode.ASCENDING);
        assertTrue(board.isSorted());
        board.setMode(SortMode.DESCENDING);
        assertFalse(board.isSorted());
    }

    // ── Target order ───────────────────────────────────────────────────────

    @Test
    void targetOrderMatchesSortDirection() {
        assertArrayEquals(new int[]{1, 2, 3, 4}, new Board(tiles(3, 1, 4, 2), SortMode.ASCENDING).targetOrder());
        assertArrayEquals(new int[]{4, 3, 2, 1}, new Board(tiles(3, 1, 4, 2), SortMode.DESCENDING).targetOrder());
    }

    @Test
    void targetOrderIsInvariantUnderSwaps() {
        Board board = new Board(tiles(5, 3, 9, 1, 7), SortMode.ASCENDING);
        int[] before = board.targetOrder();
        Random random = new Random(7);
        for (int i = 0; i < 50; i++) board.swap(random.nextInt(5), random.nextInt(5));
        assertArrayEquals(before, board.targetOrder());
    }

    @Test
    void positionCorrectIsFalseForEveryPositionOfACompletelyReversedBoard() {
        Board board = new Board(tiles(9, 8, 7, 6), SortMode.ASCENDING);
        for (int i = 0; i < 4; i++) assertFalse(board.isPositionCorrect(i));
    }

    // ── Move quality ───────────────────────────────────────────────────────

    @Test
    void goodSwapReducesInversionsAndBadSwapIncreasesThem() {
        // Swapping the outermost pair of a reversed board fixes five inversions.
        Board reversed = new Board(tiles(4, 3, 2, 1), SortMode.ASCENDING);
        assertTrue(reversed.isGoodSwap(0, 3));

        // Breaking up an ordered pair makes it worse.
        Board ordered = new Board(tiles(1, 2, 3, 4), SortMode.ASCENDING);
        assertFalse(ordered.isGoodSwap(0, 1));
    }

    @Test
    void goodSwapIsSymmetricInItsArguments() {
        Board board = new Board(tiles(4, 3, 2, 1), SortMode.ASCENDING);
        assertEquals(board.inversionDeltaForSwap(0, 3), board.inversionDeltaForSwap(3, 0));
        assertEquals(board.isGoodSwap(0, 3), board.isGoodSwap(3, 0));
    }

    @Test
    void swappingAMiddlePairWithTheEndsIsJudgedCorrectly() {
        // [4,3,2,1] -> swap 1 and 2 (adjacent) only resolves their own pair.
        Board board = new Board(tiles(4, 3, 2, 1), SortMode.ASCENDING);
        assertEquals(1, Math.abs(board.inversionDeltaForSwap(1, 2)));
        assertTrue(board.isGoodSwap(1, 2));
    }

    @Test
    void goodSwapFollowsTheSortDirection() {
        Board ascending = new Board(tiles(1, 2, 3, 4), SortMode.ASCENDING);
        assertFalse(ascending.isGoodSwap(0, 1));

        Board descending = new Board(tiles(1, 2, 3, 4), SortMode.DESCENDING);
        assertTrue(descending.isGoodSwap(0, 1));
    }

    /**
     * The O(1) delta is the load-bearing piece of move scoring, so check it
     * against a brute-force recount over many random boards.
     */
    @Test
    void inversionDeltaMatchesBruteForceRecount() {
        Random random = new Random(20260311);
        for (SortMode mode : SortMode.values()) {
            for (int trial = 0; trial < 2_000; trial++) {
                int n = 2 + random.nextInt(8);
                Tile[] pool = new Tile[n];
                for (int i = 0; i < n; i++) {
                    // Small value range so duplicate values are common.
                    pool[i] = Tile.number(random.nextInt(6) + 1);
                }
                Board board = new Board(pool, mode);

                int a = random.nextInt(n);
                int b = random.nextInt(n);

                int expected = board.inversionDeltaForSwap(a, b);
                int before = board.countInversions();
                board.swap(a, b);
                int after = board.countInversions();

                assertEquals(after - before, expected,
                        "mode=" + mode + " board=" + java.util.Arrays.toString(pool));
            }
        }
    }

    @Test
    void goodSwapIsExactlyADeltaBelowZero() {
        Random random = new Random(99);
        Board board = new Board(tiles(1, 9, 2, 8, 3, 7, 4), SortMode.ASCENDING);
        for (int i = 0; i < 500; i++) {
            int a = random.nextInt(7);
            int b = random.nextInt(7);
            assertEquals(board.inversionDeltaForSwap(a, b) < 0, board.isGoodSwap(a, b));
        }
    }

    @Test
    void swappingEqualValuesIsNotAGoodMove() {
        Board board = new Board(tiles(5, 5, 5), SortMode.ASCENDING);
        assertEquals(0, board.inversionDeltaForSwap(0, 2));
        assertFalse(board.isGoodSwap(0, 2));
    }

    // ── Correctness after a move ───────────────────────────────────────────

    @Test
    void correctAfterSwapCountsLandedTiles() {
        Board board = new Board(tiles(3, 2, 1), SortMode.ASCENDING);
        assertEquals(0, board.correctAfterSwap(0, 2));   // swap not applied yet
        board.swap(0, 2);
        assertEquals(2, board.correctAfterSwap(0, 2));
    }

    @Test
    void correctingOneTileOnlyCountsOne() {
        Board board = new Board(tiles(2, 1, 3), SortMode.ASCENDING);
        board.swap(0, 1);
        assertEquals(2, board.correctAfterSwap(0, 1));
    }

    // ── Tile movement ──────────────────────────────────────────────────────

    @Test
    void swapMovesTilesAndCarriesLockedStateWithThem() {
        Board board = new Board(tiles(10, 20, 30), SortMode.ASCENDING);
        board.setLocked(0, true);

        board.swap(0, 2);

        assertEquals(30, board.valueAt(0));
        assertEquals(10, board.valueAt(2));
        assertFalse(board.isLocked(0));
        assertTrue(board.isLocked(2));
    }

    @Test
    void displayHidesLockedTileValues() {
        Board board = new Board(tiles(10, 20, 30), SortMode.ASCENDING);
        assertEquals("20", board.display(1));
        board.setLocked(1, true);
        assertEquals("?", board.display(1));
    }

    @Test
    void setTilesReplacesTheWholeBoardAndClearsLocks() {
        Board board = new Board(tiles(1, 2), SortMode.ASCENDING);
        board.setLocked(0, true);

        board.setTiles(tiles(9, 8));

        assertEquals(2, board.size());
        assertFalse(board.isLocked(0));
        assertEquals(9, board.valueAt(0));
    }
}
