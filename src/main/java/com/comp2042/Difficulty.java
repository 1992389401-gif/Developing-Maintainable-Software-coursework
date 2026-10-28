package com.comp2042;

/**
 * How hard a game is: how fast the snake moves and whether the board edges are walls.
 */
public enum Difficulty {
    /** Normal speed, and the snake wraps around the board edges instead of crashing. */
    EASY(8.5, true),
    /** Normal speed; the board edges are walls. */
    NORMAL(8.5, false),
    /** A faster snake; the board edges are walls. */
    HARD(13.0, false);

    private final double speed;
    private final boolean wrapAround;

    Difficulty(double speed, boolean wrapAround) {
        this.speed = speed;
        this.wrapAround = wrapAround;
    }

    /**
     * Returns how many steps per second the snake makes.
     *
     * @return the rate of the game loop
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Tells whether the snake wraps to the opposite edge instead of crashing into the border.
     *
     * @return {@code true} if the board has no walls
     */
    public boolean isWrapAround() {
        return wrapAround;
    }
}
