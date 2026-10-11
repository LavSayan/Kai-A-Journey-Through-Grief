package com.example.kaiajourneythroughgrief.battle.gen;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShufflerTest {

    @Test
    void unsortedPermutationIsAPermutation() {
        Random random = new Random(1);
        for (int n = 2; n <= 12; n++) {
            for (int trial = 0; trial < 200; trial++) {
                int[] p = Shuffler.unsortedPermutation(n, random);
                assertEquals(n, p.length);

                Set<Integer> seen = new HashSet<>();
                for (int v : p) {
                    assertTrue(v >= 0 && v < n, "value " + v + " out of range");
                    assertTrue(seen.add(v), "value " + v + " appeared twice");
                }
            }
        }
    }

    /** Guards the theoretical instant-win: a level must never arrive pre-solved. */
    @Test
    void unsortedPermutationIsNeverTheIdentity() {
        Random random = new Random(42);
        for (int n = 2; n <= 12; n++) {
            for (int trial = 0; trial < 500; trial++) {
                int[] p = Shuffler.unsortedPermutation(n, random);
                // A non-identity permutation may still leave some positions in
                // place, so the whole array is what must differ.
                boolean identity = true;
                for (int i = 0; i < n && identity; i++) identity = p[i] == i;
                assertFalse(identity, "n=" + n + " produced a solved board");
            }
        }
    }

    @Test
    void aSingleTilePermutationIsStillTheIdentity() {
        assertArrayEquals(new int[]{0}, Shuffler.unsortedPermutation(1, new Random(1)));
    }

    @Test
    void shuffleActuallyReorders() {
        int[] values = {1, 2, 3, 4, 5, 6, 7, 8};
        Random random = new Random(7);
        boolean sawDifferent = false;
        for (int trial = 0; trial < 50 && !sawDifferent; trial++) {
            int[] copy = values.clone();
            Shuffler.shuffle(copy, random);
            for (int i = 0; i < values.length; i++) {
                if (copy[i] != values[i]) { sawDifferent = true; break; }
            }
        }
        assertTrue(sawDifferent);
    }

    @Test
    void isSortedDetectsOrder() {
        assertTrue(Shuffler.isSorted(new int[]{1, 2, 2, 9}));
        assertFalse(Shuffler.isSorted(new int[]{1, 3, 2}));
    }
}
