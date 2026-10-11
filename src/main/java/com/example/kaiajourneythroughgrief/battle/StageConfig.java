package com.example.kaiajourneythroughgrief.battle;

import com.example.kaiajourneythroughgrief.DialogueConfig;

import javafx.scene.paint.Color;

import java.util.function.Supplier;

/**
 * Everything that distinguishes one battle stage from another.
 *
 * <p>A stage that needs a new mechanic adds fields here plus a painter or
 * generator; the battle loop, board layout and rendering pipeline are shared.
 */
public record StageConfig(
        String id,
        int number,
        String title,
        Color themeColor,
        String enemyName,
        String assetDir,
        TileKind tileKind,
        SortModePolicy sortModePolicy,
        int tileCount,
        double minTileWidth,
        double tileHeight,
        double canvasHeight,
        double lossPerWrong,
        int arraysToWin,
        int lockedTilesInitial,
        int correctCountToUnlock,
        String staticObjectiveLabel,
        double objectiveRuleWidth,
        String victorySubtitle,
        Supplier<DialogueConfig> postVictoryDialogue,
        String nextStageId
) {

    public String eyebrowLabel() {
        return "STAGE " + number;
    }

    public String musicPath() {
        return assetDir + "/bgmusic.mp3";
    }

    public String backgroundPath() {
        return assetDir + "/background.png";
    }

    public String playerPath() {
        return assetDir + "/player.gif";
    }

    public String enemyPath() {
        return assetDir + "/enemy.gif";
    }

    /** True when this stage hides some tiles behind a {@code ?}. */
    public boolean hasLockedTiles() {
        return lockedTilesInitial > 0;
    }

    /** True when the stage re-rolls ascending/descending for every board. */
    public boolean hasRandomSortMode() {
        return sortModePolicy == SortModePolicy.RANDOM;
    }

    /** The colour a board is drawn in — follows the mode on randomised stages. */
    public Color colorFor(SortMode mode) {
        if (!hasRandomSortMode()) return themeColor;
        return mode == SortMode.DESCENDING ? Theme.DESCENDING : Theme.ASCENDING;
    }

    /** The objective line: dynamic on randomised stages, fixed otherwise. */
    public String objectiveLabel(SortMode mode) {
        return hasRandomSortMode() ? mode.objectiveLabel() : staticObjectiveLabel;
    }

    public static Builder builder(String id, int number, String title) {
        return new Builder(id, number, title);
    }

    /**
     * Fluent builder — every stage only spells out what makes it different.
     */
    public static final class Builder {

        private final String id;
        private final int number;
        private final String title;

        private Color themeColor = Theme.ASCENDING;
        private String enemyName = "ENEMY";
        private String assetDir;
        private TileKind tileKind = TileKind.NUMBER;
        private SortModePolicy sortModePolicy = SortModePolicy.FIXED_ASCENDING;
        private int tileCount = 8;
        private double minTileWidth = 80;
        private double tileHeight = 56;
        private double canvasHeight = 130;
        private double lossPerWrong = 0.05;
        private int arraysToWin = 2;
        private int lockedTilesInitial = 0;
        private int correctCountToUnlock = 0;
        private String staticObjectiveLabel = SortMode.ASCENDING.objectiveLabel();
        private double objectiveRuleWidth = 110;
        private String victorySubtitle = "The array has been sorted.";
        private Supplier<DialogueConfig> postVictoryDialogue;
        private String nextStageId = null;

        private Builder(String id, int number, String title) {
            this.id = id;
            this.number = number;
            this.title = title;
            this.assetDir = "assets/stage" + number;
        }

        public Builder theme(Color color)          { this.themeColor = color; return this; }
        public Builder enemy(String name)           { this.enemyName = name; return this; }
        public Builder assets(String dir)           { this.assetDir = dir; return this; }
        public Builder tiles(TileKind kind)         { this.tileKind = kind; return this; }
        public Builder sortMode(SortModePolicy p)   { this.sortModePolicy = p; return this; }
        public Builder tileCount(int n)             { this.tileCount = n; return this; }
        public Builder minTileWidth(double w)       { this.minTileWidth = w; return this; }
        public Builder tileHeight(double h)         { this.tileHeight = h; return this; }
        public Builder canvasHeight(double h)       { this.canvasHeight = h; return this; }
        public Builder lossPerWrong(double v)       { this.lossPerWrong = v; return this; }
        public Builder arraysToWin(int n)           { this.arraysToWin = n; return this; }
        public Builder lockedTiles(int initial, int correctsToUnlock) {
            this.lockedTilesInitial = initial;
            this.correctCountToUnlock = correctsToUnlock;
            return this;
        }
        public Builder objective(String label, double ruleWidth) {
            this.staticObjectiveLabel = label;
            this.objectiveRuleWidth = ruleWidth;
            return this;
        }
        public Builder victorySubtitle(String s)    { this.victorySubtitle = s; return this; }
        public Builder then(Supplier<DialogueConfig> dialogue, String nextStageId) {
            this.postVictoryDialogue = dialogue;
            this.nextStageId = nextStageId;
            return this;
        }

        public StageConfig build() {
            return new StageConfig(
                    id, number, title, themeColor, enemyName, assetDir,
                    tileKind, sortModePolicy, tileCount, minTileWidth, tileHeight,
                    canvasHeight, lossPerWrong, arraysToWin, lockedTilesInitial,
                    correctCountToUnlock, staticObjectiveLabel, objectiveRuleWidth,
                    victorySubtitle, postVictoryDialogue, nextStageId);
        }
    }
}
