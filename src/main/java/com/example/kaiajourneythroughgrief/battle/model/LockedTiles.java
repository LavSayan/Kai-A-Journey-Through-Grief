package com.example.kaiajourneythroughgrief.battle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Stage 5's "secrets" mechanic: some tiles start with their value hidden behind
 * a {@code ?}, and are revealed either by landing in the right place or by
 * banking enough correct placements.
 *
 * <p>The original implementation locked tiles with {@code random.nextInt()} in a
 * loop, so repeated draws could collide and lock fewer tiles than intended, and
 * it revealed tiles from inside {@code isSorted()} — a predicate that was mutating
 * state on every frame of the game loop.
 */
public final class LockedTiles {

    private final int initialCount;
    private final int correctsPerUnlock;

    private int correctsPending;

    public LockedTiles(int initialCount, int correctsPerUnlock) {
        this.initialCount = initialCount;
        this.correctsPerUnlock = correctsPerUnlock;
    }

    /** Locks exactly {@code initialCount} distinct tiles for a fresh level. */
    public void reset(Board board, Random random) {
        board.clearLocks();
        correctsPending = 0;
        if (initialCount <= 0) return;

        int size = board.size();
        int toLock = Math.min(initialCount, size);

        List<Integer> indices = new ArrayList<>(size);
        for (int i = 0; i < size; i++) indices.add(i);
        Collections.shuffle(indices, random);

        for (int i = 0; i < toLock; i++) board.setLocked(indices.get(i), true);
    }

    public int correctsPending() {
        return correctsPending;
    }

    /**
     * Reveals every locked tile that already sits in its correct position.
     *
     * @return how many were revealed
     */
    public int revealCorrectlyPositioned(Board board) {
        int revealed = 0;
        for (int i = 0; i < board.size(); i++) {
            if (board.isLocked(i) && board.isPositionCorrect(i)) {
                board.setLocked(i, false);
                revealed++;
            }
        }
        return revealed;
    }

    /**
     * Banks a good move: correct placements feed the counter, and every
     * {@code correctsPerUnlock} earned unlocks another tile.
     *
     * @param correctCount how many of the two swapped tiles landed correctly
     * @return indices of tiles that should now be revealed (may be empty)
     */
    public List<Integer> bankCorrectMove(Board board, int correctCount) {
        if (correctCount <= 0) return List.of();

        correctsPending += correctCount;
        correctsPending += revealCorrectlyPositioned(board);

        List<Integer> toReveal = new ArrayList<>();
        while (correctsPending >= correctsPerUnlock) {
            correctsPending -= correctsPerUnlock;
            int index = firstLockedIndex(board);
            if (index < 0) break;
            board.setLocked(index, false);
            toReveal.add(index);
        }
        return toReveal;
    }

    private static int firstLockedIndex(Board board) {
        for (int i = 0; i < board.size(); i++) {
            if (board.isLocked(i)) return i;
        }
        return -1;
    }
}
