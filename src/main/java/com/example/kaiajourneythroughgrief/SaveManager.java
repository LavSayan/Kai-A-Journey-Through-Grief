package com.example.kaiajourneythroughgrief;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.prefs.Preferences;


public class SaveManager {

    // ── Preferences node — uniquely identifies this game's data ───────────
    private static final Preferences PREFS =
            Preferences.userRoot().node("com/example/kaiajourneythroughgrief");

    // ── Stage order — used to determine which stage comes next ─────────────
    private static final String[] STAGE_ORDER = {
            "stage1", "stage2", "stage3", "stage4", "stage5"
    };

    // ── Default status for a fresh game ────────────────────────────────────
    private static final String DEFAULT_STAGE1 = "unlocked";
    private static final String DEFAULT_OTHER  = "locked";

    // =========================================================================
    // PUBLIC API
    // =========================================================================

    /**
     * Saves the status of a single stage.
     *
     * @param stageId e.g. "stage1"
     * @param status  "locked" | "unlocked" | "complete"
     */
    public static void save(String stageId, String status) {
        PREFS.put(stageId, status);
        flushQuietly();
    }

    /**
     * Marks the given stage as "complete" and unlocks the next one.
     * Call this when the player wins a stage battle.
     *
     * @param stageId the stage that was just completed
     */
    public static void unlockNext(String stageId) {
        save(stageId, "complete");

        for (int i = 0; i < STAGE_ORDER.length - 1; i++) {
            if (STAGE_ORDER[i].equals(stageId)) {
                String next = STAGE_ORDER[i + 1];
                // Only unlock if it's still locked — don't downgrade complete → unlocked
                if ("locked".equals(PREFS.get(next, DEFAULT_OTHER))) {
                    save(next, "unlocked");
                }
                break;
            }
        }
    }

    /**
     * Loads the saved status for every stage.
     * Returns a map ready to be used as LEVEL_STATUS in LevelScreen.
     * If no save data exists, returns the default new-game state.
     */
    public static Map<String, String> load() {
        Map<String, String> status = new LinkedHashMap<>();
        for (int i = 0; i < STAGE_ORDER.length; i++) {
            String id      = STAGE_ORDER[i];
            String def     = (i == 0) ? DEFAULT_STAGE1 : DEFAULT_OTHER;
            status.put(id, PREFS.get(id, def));
        }
        return status;
    }

    /**
     * Wipes all saved progress and resets to the default new-game state.
     */
    public static void reset() {
        try {
            PREFS.clear();
            flushQuietly();
        } catch (Exception e) {
            System.err.println("[SaveManager] Reset failed: " + e.getMessage());
        }
    }

    /**
     * Returns true if the player has any meaningful progress beyond the default.
     * "Progress" means at least one stage is complete, or stage2+ is unlocked.
     * Used by the menu to decide whether to show the new-game warning.
     */
    public static boolean hasProgress() {
        for (int i = 0; i < STAGE_ORDER.length; i++) {
            String def    = (i == 0) ? DEFAULT_STAGE1 : DEFAULT_OTHER;
            String status = PREFS.get(STAGE_ORDER[i], def);
            if ("complete".equals(status)) return true;
            if (i > 0 && "unlocked".equals(status)) return true;
        }
        return false;
    }

    // =========================================================================
    // PRIVATE
    // =========================================================================

    private static void flushQuietly() {
        try {
            PREFS.flush();
        } catch (Exception e) {
            System.err.println("[SaveManager] Could not flush preferences: " + e.getMessage());
        }
    }
}