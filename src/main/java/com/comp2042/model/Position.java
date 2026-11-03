package com.comp2042.model;

/**
 * A cell on the board, given by its column {@code x} and row {@code y}; y grows downwards.
 * <p>
 * Positions are immutable, so the snake and the board can hand them out without anyone being
 * able to move a cell behind their back. This record replaces {@code java.awt.Point}, which is
 * mutable and tied the model to the AWT user-interface library.
 *
 * @param x the column, counted from 0 at the left edge
 * @param y the row, counted from 0 at the top edge
 */
public record Position(int x, int y) {

    /**
     * Returns the neighbouring cell one step away in the given direction.
     *
     * @param direction the direction to step in
     * @return the neighbouring position, which may lie outside the board
     */
    public Position step(Direction direction) {
        return new Position(x + direction.getDx(), y + direction.getDy());
    }

    /**
     * Tells whether this cell lies on a board of the given size.
     *
     * @param width  the board width in cells
     * @param height the board height in cells
     * @return {@code true} if both coordinates are on the board
     */
    public boolean isInside(int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    /**
     * Brings a cell that has left the board back in on the opposite side.
     *
     * @param width  the board width in cells
     * @param height the board height in cells
     * @return this cell if it is on the board, otherwise its wrapped-around twin
     */
    public Position wrap(int width, int height) {
        return new Position(Math.floorMod(x, width), Math.floorMod(y, height));
    }
}
