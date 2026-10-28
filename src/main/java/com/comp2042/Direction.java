package com.comp2042;

/**
 * The four directions the snake can move in. Each direction knows how one step changes
 * the grid coordinates, where x grows to the right and y grows downwards.
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Returns how far one step in this direction moves along the x axis.
     *
     * @return -1, 0 or 1
     */
    public int getDx() {
        return dx;
    }

    /**
     * Returns how far one step in this direction moves along the y axis.
     *
     * @return -1, 0 or 1
     */
    public int getDy() {
        return dy;
    }

    /**
     * Returns the direction pointing the other way.
     *
     * @return DOWN for UP, LEFT for RIGHT, and so on
     */
    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
        };
    }
}
