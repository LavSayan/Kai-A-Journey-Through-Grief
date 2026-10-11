package com.example.kaiajourneythroughgrief.battle.view;

import com.example.kaiajourneythroughgrief.battle.model.Board;
import com.example.kaiajourneythroughgrief.battle.model.BoardLayout;
import com.example.kaiajourneythroughgrief.battle.model.Tile;

import javafx.scene.canvas.GraphicsContext;

/**
 * Everything a tile painter needs for one tile, bundled so the painters take a
 * single argument.
 */
public final class TilePaintContext {

    private final GraphicsContext gc;
    private final BoardLayout layout;
    private final Board board;
    private final int index;
    private final double x;
    private final double y;
    private final boolean correct;
    private final boolean dragged;

    public TilePaintContext(GraphicsContext gc, BoardLayout layout, Board board,
                            int index, double x, double y, boolean correct, boolean dragged) {
        this.gc = gc;
        this.layout = layout;
        this.board = board;
        this.index = index;
        this.x = x;
        this.y = y;
        this.correct = correct;
        this.dragged = dragged;
    }

    public GraphicsContext gc()         { return gc; }
    public BoardLayout layout()        { return layout; }
    public Board board()               { return board; }
    public int index()                 { return index; }
    public double x()                  { return x; }
    public double y()                  { return y; }
    public double width()              { return layout.boxWidth(); }
    public double height()             { return layout.tileHeight(); }
    public double centerX()            { return x + layout.boxWidth() / 2; }
    public boolean isCorrect()         { return correct; }
    public boolean isDragged()         { return dragged; }
    public boolean isLocked()          { return board.isLocked(index); }

    /** What the player sees — {@code "?"} while the value is hidden. */
    public String label()              { return board.display(index); }

    /** The hidden value this tile must be ordered by. */
    public int value()                 { return board.valueAt(index); }

    public Tile tile()                 { return board.tile(index); }
}
