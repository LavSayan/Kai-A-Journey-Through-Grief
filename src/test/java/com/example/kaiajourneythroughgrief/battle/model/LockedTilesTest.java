package com.example.kaiajourneythroughgrief.battle.model;

import com.example.kaiajourneythroughgrief.battle.SortMode;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LockedTilesTest {

    private static Board board(int... values) {
        Tile[] t = new Tile[values.length];
        for (int i = 0; i < values.length; i++) t[i] = Tile.number(values[i]);
        return new Board(t, SortMode.ASCENDING);
    }

    private static int countLocked(Board b) {
        int n = 0;
        for (int i = 0; i < b.size(); i++) if (b.isLocked(i)) n++;
        return n;
    }

    /**
     * The original picked lock targets with repeated random draws, which could
     * collide and leave fewer tiles hidden than intended.
     */
    @Test
    void resetLocksExactlyTheRequestedNumberOfDistinctTiles() {
        LockedTiles mechanic = new LockedTiles(4, 3);
        Random random = new Random(4);

        for (int trial = 0; trial < 500; trial++) {
            Board b = board(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
            mechanic.reset(b, random);
            assertEquals(4, countLocked(b), "trial " + trial);
        }
    }

    @Test
    void resetNeverAsksForMoreTilesThanExist() {
        LockedTiles mechanic = new LockedTiles(10, 3);
        Board b = board(1, 2, 3, 4);
        mechanic.reset(b, new Random(1));
        assertEquals(4, countLocked(b));
    }

    @Test
    void resetClearsPreviousLocksAndPendingProgress() {
        LockedTiles mechanic = new LockedTiles(4, 3);
        // No tile is in its final position, so nothing reveals itself.
        Board b = board(2, 1, 4, 3, 6, 5, 8, 7, 10, 9);
        mechanic.reset(b, new Random(1));
        mechanic.bankCorrectMove(b, 3);
        assertEquals(3, countLocked(b), "the first unlock should have fired");

        mechanic.reset(b, new Random(2));
        assertEquals(0, mechanic.correctsPending());
        assertEquals(4, countLocked(b));
    }

    @Test
    void correctlyPlacedLockedTilesRevealThemselves() {
        LockedTiles mechanic = new LockedTiles(1, 99);
        Board b = board(1, 2, 3, 4);
        b.setLocked(0, true);      // already the smallest, so it is in place

        assertEquals(1, mechanic.revealCorrectlyPositioned(b));
        assertFalse(b.isLocked(0));
    }

    @Test
    void misplacedLockedTilesStayHidden() {
        LockedTiles mechanic = new LockedTiles(1, 99);
        Board b = board(4, 3, 2, 1);
        b.setLocked(0, true);

        assertEquals(0, mechanic.revealCorrectlyPositioned(b));
        assertTrue(b.isLocked(0));
    }

    @Test
    void threeCorrectPlacementsUnlockOneMoreTile() {
        LockedTiles mechanic = new LockedTiles(2, 3);
        // Nothing is already in place, so progress only comes from the player.
        Board b = board(2, 1, 4, 3, 6, 5);
        b.setLocked(0, true);
        b.setLocked(1, true);

        assertTrue(mechanic.bankCorrectMove(b, 2).isEmpty(), "two is not enough");
        assertEquals(2, mechanic.correctsPending());
        assertEquals(2, countLocked(b));

        List<Integer> unlocked = mechanic.bankCorrectMove(b, 2);
        assertEquals(List.of(0), unlocked);
        assertEquals(1, mechanic.correctsPending());
        assertEquals(1, countLocked(b));
    }

    @Test
    void aBadMoveBanksNothing() {
        LockedTiles mechanic = new LockedTiles(4, 3);
        Board b = board(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        mechanic.reset(b, new Random(5));
        int lockedBefore = countLocked(b);

        assertTrue(mechanic.bankCorrectMove(b, 0).isEmpty());
        assertEquals(0, mechanic.correctsPending());
        assertEquals(lockedBefore, countLocked(b));
    }

    @Test
    void unlockingStopsCleanlyWhenNothingIsLeftHidden() {
        LockedTiles mechanic = new LockedTiles(1, 1);
        Board b = board(2, 1, 4, 3);
        mechanic.reset(b, new Random(6));
        assertEquals(1, countLocked(b));

        mechanic.bankCorrectMove(b, 2);

        assertEquals(0, countLocked(b));
        assertEquals(0, mechanic.correctsPending());
    }

    @Test
    void correctPlacementsThatLandTilesAutomaticallyAlsoBankProgress() {
        LockedTiles mechanic = new LockedTiles(2, 5);
        Board b = board(1, 2, 3, 4, 5);
        b.setLocked(4, true);   // already in place, unlocks itself
        b.setLocked(0, true);   // also already in place

        assertEquals(2, mechanic.revealCorrectlyPositioned(b));
        assertEquals(0, countLocked(b));
    }
}
