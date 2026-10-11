package com.example.kaiajourneythroughgrief.battle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardLayoutTest {

    private static final double TILE_H = 56;
    private static final double SPACING = 8;
    private static final double MARGIN = 20;
    private static final double Y_OFFSET = 16;

    private static BoardLayout layout(int tileCount, double minWidth) {
        BoardLayout l = new BoardLayout(tileCount, minWidth, TILE_H, SPACING, MARGIN, Y_OFFSET);
        l.update(1000, 160);
        return l;
    }

    @Test
    void everyTileIsPlacedInsideTheCanvas() {
        for (int count : new int[]{8, 10}) {
            BoardLayout l = layout(count, 80);
            for (int i = 0; i < count; i++) {
                assertTrue(l.x(i) >= 0, "tile " + i + " starts left of the canvas");
                assertTrue(l.x(i) + l.boxWidth() <= 1000.0001,
                        "tile " + i + " overflows the right edge");
                assertTrue(l.y(i) >= 0, "tile " + i + " starts above the canvas");
                assertTrue(l.y(i) + TILE_H <= 160.0001,
                        "tile " + i + " overflows the bottom edge");
            }
        }
    }

    @Test
    void noTwoTilesOverlap() {
        for (int count : new int[]{8, 10}) {
            BoardLayout l = layout(count, 80);
            for (int i = 0; i < count; i++) {
                for (int j = i + 1; j < count; j++) {
                    boolean separatedX = l.x(i) + l.boxWidth() <= l.x(j)
                            || l.x(j) + l.boxWidth() <= l.x(i);
                    boolean separatedY = l.y(i) + TILE_H <= l.y(j)
                            || l.y(j) + TILE_H <= l.y(i);
                    assertTrue(separatedX || separatedY, "tiles " + i + " and " + j + " overlap");
                }
            }
        }
    }

    @Test
    void tilesThatFitOnOneRowShareAY() {
        BoardLayout l = layout(8, 80);
        assertEquals(1, (int) Math.ceil(8 / (double) l.maxPerRow()));
        for (int i = 1; i < 8; i++) assertEquals(l.y(0), l.y(i));
    }

    @Test
    void tenTilesWrapOntoASecondRowWhenTheBoardIsNarrow() {
        BoardLayout l = new BoardLayout(10, 80, TILE_H, SPACING, MARGIN, Y_OFFSET);
        l.update(800, 160);
        assertEquals(8, l.maxPerRow());
        assertTrue(l.y(8) > l.y(0), "nine tiles should overflow onto a second row");
        assertTrue(l.y(8) + TILE_H <= 160.0001, "the second row must still fit");
    }

    @Test
    void aFullWidthBoardKeepsTenTilesOnOneRow() {
        BoardLayout l = layout(10, 80);
        assertEquals(1, (int) Math.ceil(10 / (double) l.maxPerRow()));
    }

    @Test
    void hitTestFindsTheTileUnderAPointAndMissesOutside() {
        BoardLayout l = layout(8, 80);
        int index = l.hitTest(l.x(3) + l.boxWidth() / 2, l.y(3) + TILE_H / 2);
        assertEquals(3, index);
        assertEquals(-1, l.hitTest(-5, -5));
        assertEquals(-1, l.hitTest(5_000, 5_000));
    }

    @Test
    void nearestIndexPicksTheClosestTileCentre() {
        BoardLayout l = layout(8, 80);
        for (int i = 0; i < 8; i++) {
            double cx = l.x(i) + l.boxWidth() / 2;
            double cy = l.y(i) + TILE_H / 2;
            assertEquals(i, l.nearestIndex(cx, cy));
        }
    }

    @Test
    void positionsFollowAResize() {
        BoardLayout l = layout(8, 80);
        double before = l.boxWidth();
        l.update(600, 160);
        assertNotEquals(before, l.boxWidth());
        for (int i = 0; i < 8; i++) {
            assertTrue(l.x(i) >= 0);
            assertTrue(l.x(i) + l.boxWidth() <= 600.0001);
        }
    }

    @Test
    void aZeroWidthCanvasDoesNotProduceNegativeGeometry() {
        BoardLayout l = new BoardLayout(8, 80, TILE_H, SPACING, MARGIN, Y_OFFSET);
        l.update(0, 0);
        assertTrue(l.boxWidth() >= 1.0);
        assertTrue(l.maxPerRow() >= 1);
    }
}
