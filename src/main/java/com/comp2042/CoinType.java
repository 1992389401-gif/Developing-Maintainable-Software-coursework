package com.comp2042;

/**
 * The kinds of coin that can appear on the board.
 * <p>
 * The view uses a coin's type to choose its sprite, so the coin classes do not need to
 * load JavaFX images and can be created in plain unit tests.
 */
public enum CoinType {
    /** The ordinary coin, with no special effect. */
    RED,
    /** Worth more points than a red coin. */
    GOLD,
    /** Adds obstacles to the board when eaten. */
    SILVER,
    /** Lets the snake wrap around the board edges until the next coin is eaten. */
    BLUE,
    /** Speeds the snake up until the next coin is eaten. */
    RAINBOW
}
