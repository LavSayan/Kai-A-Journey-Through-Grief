package com.example.kaiajourneythroughgrief.battle;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * The shared design system for every battle stage.
 *
 * <p>Colours and fonts are parsed exactly once here. The per-frame render loops
 * used to call {@code Color.web(...)} and {@code Font.font(...)} for every tile
 * on every animation frame, which meant re-parsing hex strings and re-resolving
 * font names 60+ times per swap. Use {@link #fade(Color, double)} for alpha
 * variants — it is a plain field copy, not a string parse.
 */
public final class Theme {

    private Theme() {}

    // ── Palette ────────────────────────────────────────────────────────────

    public static final Color CANVAS_BG     = Color.web("#07070F");
    public static final Color TITLE_BAR     = Color.web("#050510");
    public static final Color CARD_BG       = Color.web("#07070E");
    public static final Color BAND_DIVIDER  = Color.web("#1C1C30");
    public static final Color TITLE_TEXT    = Color.web("#C4C4DC");
    public static final Color ACTIVE_TITLE  = Color.web("#D8D8EC");
    public static final Color META_TEXT     = Color.web("#F9F6EE");
    public static final Color COMPLETE_TEXT = Color.web("#5E5E78");
    public static final Color COMPLETION    = Color.web("#00FF88");
    public static final Color ACCENT_LINE   = Color.web("#B8860B");
    public static final Color PLAYER_GREEN  = Color.web("#0a6f0b");
    public static final Color ENEMY_RED     = Color.web("#af0000");

    /** Theme colour for an ascending board on a mode-coloured stage. */
    public static final Color ASCENDING     = Color.web("#ADD8E6");

    /** Theme colour for a descending board on a mode-coloured stage. */
    public static final Color DESCENDING    = Color.web("#DC143C");

    /** Theme colour for a locked (unknown value) tile. */
    public static final Color LOCKED        = Color.web("#9370DB");

    /** Returns {@code color} at the given opacity without re-parsing a string. */
    public static Color fade(Color color, double opacity) {
        return Color.color(color.getRed(), color.getGreen(), color.getBlue(), opacity);
    }

    // ── Fonts ──────────────────────────────────────────────────────────────

    public static final Font META_8      = Font.font("Courier New", FontWeight.BOLD, 8);
    public static final Font META_9      = Font.font("Courier New", FontWeight.BOLD, 9);
    public static final Font META_12     = Font.font("Courier New", FontWeight.BOLD, 12);
    public static final Font META_9_PLAIN= Font.font("Courier New", 9);
    public static final Font META_10     = Font.font("Courier New", FontWeight.BOLD, 10);
    public static final Font META_10_PLAIN = Font.font("Courier New", 10);
    public static final Font PROGRESS_9  = Font.font("Courier New", FontWeight.BOLD, 9);
    public static final Font TITLE_15    = Font.font("Georgia", FontWeight.BOLD, 15);
    public static final Font HEADING_36  = Font.font("Georgia", FontWeight.BOLD, 36);
    public static final Font SUBTITLE_13 = Font.font("Georgia", FontPosture.ITALIC, 13);
    public static final Font OBJECTIVE_12= Font.font("Georgia", FontPosture.ITALIC, 12);
    public static final Font TICK_11     = Font.font("Georgia", 11);
    public static final Font TILE_VALUE_20 = Font.font("Georgia", FontWeight.BOLD, 20);
    public static final Font TILE_EXPR_18  = Font.font("Georgia", FontWeight.BOLD, 18);
    public static final Font TILE_EQ_15    = Font.font("Georgia", FontWeight.BOLD, 15);

    // ── Board geometry (identical across every stage) ──────────────────────

    public static final double SPACING     = 8;
    public static final double MARGIN_X    = 20;
    public static final double CARD_RADIUS = 5;
    public static final double ACCENT_W    = 3;

    /** Vertical offset of the tile grid from the vertically centred position. */
    public static final double GRID_Y_OFFSET = 16;

    public static final int SWAP_FRAMES = 60;
    public static final int SWAP_MILLIS = 300;

    // ── Battle balance (identical across every stage) ──────────────────────

    public static final double DRAIN_PER_SECOND = 0.04;
    public static final double GAIN_PER_CORRECT = 0.08;
    public static final double STARTING_HEALTH   = 0.5;

    // ── Character sprites (identical across every stage) ───────────────────

    public static final String PLAYER_NAME   = "KAI";
    public static final double CHAR_WIDTH   = 300;
    public static final double CHAR_HEIGHT  = 300;
    public static final double CHAR_LEFT    = 80;
    public static final double CHAR_BOTTOM  = 40;
}
