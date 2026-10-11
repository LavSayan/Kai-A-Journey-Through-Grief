package com.example.kaiajourneythroughgrief.battle.model;

/**
 * Where every tile sits on the board canvas.
 *
 * <p>Positions are computed once per resize and cached, rather than recomputed
 * (and a {@code double[2]} allocated) on every lookup. Hit testing and
 * nearest-tile search call this in a loop, and the swap animation redraws the
 * whole board on every one of its 61 key frames.
 */
public final class BoardLayout {

    private final int tileCount;
    private final double minTileWidth;
    private final double tileHeight;
    private final double spacing;
    private final double marginX;
    private final double gridYOffset;

    private double canvasWidth;
    private double canvasHeight;
    private double boxWidth;
    private int maxPerRow;

    private final double[] xs;
    private final double[] ys;

    public BoardLayout(int tileCount, double minTileWidth, double tileHeight,
                       double spacing, double marginX, double gridYOffset) {
        this.tileCount = tileCount;
        this.minTileWidth = minTileWidth;
        this.tileHeight = tileHeight;
        this.spacing = spacing;
        this.marginX = marginX;
        this.gridYOffset = gridYOffset;

        this.xs = new double[tileCount];
        this.ys = new double[tileCount];
        update(0, 0);
    }

    /** Recomputes every tile position for the given canvas size. */
    public void update(double width, double height) {
        this.canvasWidth = width;
        this.canvasHeight = height;

        double usable = Math.max(minTileWidth, width - marginX * 2);
        maxPerRow = Math.max(1, (int) ((usable + spacing) / (minTileWidth + spacing)));

        int itemsInRow = Math.min(maxPerRow, tileCount);
        boxWidth = Math.max(1.0, (usable - spacing * (itemsInRow - 1)) / itemsInRow);

        int totalRows = (int) Math.ceil(tileCount / (double) maxPerRow);
        double totalH = totalRows * tileHeight + (totalRows - 1) * spacing;
        double startY = (height - totalH) / 2.0 + gridYOffset;

        for (int i = 0; i < tileCount; i++) {
            int row = i / maxPerRow;
            int col = i % maxPerRow;

            int inRow = Math.min(maxPerRow, tileCount - row * maxPerRow);
            double rowWidth = inRow * boxWidth + (inRow - 1) * spacing;
            double startX = (width - rowWidth) / 2.0;

            xs[i] = startX + col * (boxWidth + spacing);
            ys[i] = startY + row * (tileHeight + spacing);
        }
    }

    public double boxWidth()          { return boxWidth; }
    public double tileHeight()        { return tileHeight; }
    public double spacing()           { return spacing; }
    public double marginX()           { return marginX; }
    public double x(int index)        { return xs[index]; }
    public double y(int index)        { return ys[index]; }
    public int maxPerRow()            { return maxPerRow; }

    /** Index of the tile under the given point, or -1. */
    public int hitTest(double px, double py) {
        for (int i = 0; i < tileCount; i++) {
            if (px >= xs[i] && px <= xs[i] + boxWidth
                    && py >= ys[i] && py <= ys[i] + tileHeight) return i;
        }
        return -1;
    }

    /** Index of the tile whose centre is closest to the given point, or -1. */
    public int nearestIndex(double px, double py) {
        int best = -1;
        double bestD = Double.MAX_VALUE;
        for (int i = 0; i < tileCount; i++) {
            double cx = xs[i] + boxWidth / 2;
            double cy = ys[i] + tileHeight / 2;
            double d2 = (px - cx) * (px - cx) + (py - cy) * (py - cy);
            if (d2 < bestD) {
                bestD = d2;
                best = i;
            }
        }
        return best;
    }
}
