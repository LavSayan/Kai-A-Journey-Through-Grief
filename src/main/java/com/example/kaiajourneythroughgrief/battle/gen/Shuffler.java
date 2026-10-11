package com.example.kaiajourneythroughgrief.battle.gen;

import java.util.Random;

/**
 * Shuffling helpers used to turn freshly generated values into a puzzle.
 */
public final class Shuffler {

    private Shuffler() {}

    /** In-place Fisher–Yates. */
    public static void shuffle(int[] values, Random random) {
        for (int i = values.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = values[i];
            values[i] = values[j];
            values[j] = tmp;
        }
    }

    public static boolean isSorted(int[] values) {
        for (int i = 1; i < values.length; i++) {
            if (values[i - 1] > values[i]) return false;
        }
        return true;
    }

    /**
     * A shuffled permutation of {@code 0..count-1} that is guaranteed not to be
     * the identity, so the player never receives an already-solved board.
     */
    public static int[] unsortedPermutation(int count, Random random) {
        if (count <= 1) return new int[count];

        int[] order = identity(count);
        int attempts = 0;
        do {
            shuffle(order, random);
            attempts++;
        } while (isIdentity(order) && attempts < 50);
        return order;
    }

    private static int[] identity(int count) {
        int[] a = new int[count];
        for (int i = 0; i < count; i++) a[i] = i;
        return a;
    }

    private static boolean isIdentity(int[] a) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] != i) return false;
        }
        return true;
    }
}
