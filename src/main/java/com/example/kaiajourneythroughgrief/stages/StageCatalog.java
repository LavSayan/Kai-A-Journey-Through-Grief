package com.example.kaiajourneythroughgrief.stages;

import com.example.kaiajourneythroughgrief.battle.SortModePolicy;
import com.example.kaiajourneythroughgrief.battle.StageConfig;
import com.example.kaiajourneythroughgrief.battle.TileKind;
import com.example.kaiajourneythroughgrief.battle.Theme;
import com.example.kaiajourneythroughgrief.dialogues.Dialogue4;
import com.example.kaiajourneythroughgrief.dialogues.Dialogue5;
import com.example.kaiajourneythroughgrief.dialogues.Dialogue6;
import com.example.kaiajourneythroughgrief.dialogues.Dialogue7;
import com.example.kaiajourneythroughgrief.dialogues.Dialogue8;

import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The single source of truth for all five battle stages.
 *
 * <p>Values here are transcribed verbatim from the original per-stage
 * controllers, including the places where the stages disagree with each other
 * (Stage 2 is the only stage that punishes a wrong swap twice as hard).
 */
public final class StageCatalog {

    private StageCatalog() {}

    private static final Map<String, StageConfig> BY_ID = build();

    private static Map<String, StageConfig> build() {
        Map<String, StageConfig> m = new LinkedHashMap<>();

        // ── Stage 1 ────────────────────────────────────────────────────────
        // The tutorial. Plain numbers, always ascending, gentlest punishment.
        m.put("stage1", StageConfig.builder("stage1", 1, "The Forest Frozen in Fog")
                .theme(Theme.ASCENDING)
                .enemy("OUROBORO")
                .tiles(TileKind.NUMBER)
                .sortMode(SortModePolicy.FIXED_ASCENDING)
                .tileCount(8)
                .minTileWidth(80)
                .tileHeight(56)
                .canvasHeight(130)
                .lossPerWrong(0.05)
                .objective("Sort Ascending", 110)
                .victorySubtitle("The array has been sorted.")
                .then(Dialogue4::getConfig, "stage2")
                .build());

        // ── Stage 2 ────────────────────────────────────────────────────────
        // Sort direction is now random, and a wrong swap costs double.
        m.put("stage2", StageConfig.builder("stage2", 2, "A City of Shattered Glass")
                .theme(Theme.ASCENDING)
                .enemy("SHARD")
                .tiles(TileKind.NUMBER)
                .sortMode(SortModePolicy.RANDOM)
                .tileCount(8)
                .minTileWidth(80)
                .tileHeight(56)
                .canvasHeight(130)
                .lossPerWrong(0.10)
                .objective("Sort Ascending", 110)
                .victorySubtitle("The array has been sorted.")
                .then(Dialogue5::getConfig, "stage3")
                .build());

        // ── Stage 3 ────────────────────────────────────────────────────────
        // Tiles are expressions; the player sorts by the hidden result.
        m.put("stage3", StageConfig.builder("stage3", 3, "A Temple of Endless Doors")
                .theme(Color.rgb(0xFF, 0xD5, 0x80))
                .enemy("THE DOOR")
                .tiles(TileKind.EXPRESSION)
                .sortMode(SortModePolicy.FIXED_ASCENDING)
                .tileCount(8)
                .minTileWidth(90)
                .tileHeight(64)
                .canvasHeight(140)
                .lossPerWrong(0.05)
                .objective("Solve & Sort Ascending", 150)
                .victorySubtitle("The equations are solved.")
                .then(Dialogue6::getConfig, "stage4")
                .build());

        // ── Stage 4 ────────────────────────────────────────────────────────
        // Tiles are equations with an unknown x; sort by the hidden solution.
        m.put("stage4", StageConfig.builder("stage4", 4, "An Ocean of Still Water")
                .theme(Color.rgb(0xB3, 0x9D, 0xDB))
                .enemy("THE MERMAID")
                .tiles(TileKind.EQUATION)
                .sortMode(SortModePolicy.FIXED_ASCENDING)
                .tileCount(8)
                .minTileWidth(100)
                .tileHeight(68)
                .canvasHeight(145)
                .lossPerWrong(0.05)
                .objective("Solve for x, Sort Ascending", 175)
                .victorySubtitle("All equations solved.")
                .then(Dialogue7::getConfig, "stage5")
                .build());

        // ── Stage 5 ────────────────────────────────────────────────────────
        // Ten tiles, random sort direction, and some tiles start hidden.
        m.put("stage5", StageConfig.builder("stage5", 5, "A Garden at Dawn")
                .theme(Theme.ASCENDING)
                .enemy("THE ECLIPSE")
                .tiles(TileKind.NUMBER)
                .sortMode(SortModePolicy.RANDOM)
                .tileCount(10)
                .minTileWidth(80)
                .tileHeight(56)
                .canvasHeight(130)
                .lossPerWrong(0.05)
                .lockedTiles(4, 3)
                .objective("Sort Ascending", 110)
                .victorySubtitle("All secrets revealed.")
                .then(Dialogue8::getConfig, null)
                .build());

        return Collections.unmodifiableMap(m);    }

    /** Returns the config for a stage id such as {@code "stage3"}, or null. */
    public static StageConfig get(String stageId) {
        return BY_ID.get(stageId);
    }

    /** Every stage, in play order. */
    public static List<StageConfig> all() {
        return List.copyOf(BY_ID.values());
    }

    /** Every stage id, in play order. */
    public static List<String> ids() {
        return List.copyOf(BY_ID.keySet());
    }
}
