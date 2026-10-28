package com.comp2042;

import javafx.scene.input.KeyCode;

/**
 * Maps keyboard keys to what they do in the game, so the controller does not have to
 * compare key codes itself.
 */
public final class KeyBindings {

    private KeyBindings() {
    }

    /**
     * Returns the direction an arrow key stands for.
     *
     * @param code the key that was pressed
     * @return the matching direction, or {@code null} if the key is not an arrow key
     */
    public static Direction directionFor(KeyCode code) {
        return switch (code) {
            case UP -> Direction.UP;
            case DOWN -> Direction.DOWN;
            case LEFT -> Direction.LEFT;
            case RIGHT -> Direction.RIGHT;
            default -> null;
        };
    }

    /**
     * Tells whether a key switches the game to full screen. Keyboards report the "+" key in
     * different ways: PLUS on layouts with a separate "+" key, EQUALS on US-style layouts
     * (where "+" is Shift and "="), and ADD on the numeric keypad, so all three count.
     *
     * @param code the key that was pressed
     * @return {@code true} for a "+" key
     */
    public static boolean entersFullScreen(KeyCode code) {
        return code == KeyCode.PLUS || code == KeyCode.EQUALS || code == KeyCode.ADD;
    }

    /**
     * Tells whether a key switches the game back to a window: "-" on the main keyboard
     * (MINUS) or on the numeric keypad (SUBTRACT).
     *
     * @param code the key that was pressed
     * @return {@code true} for a "-" key
     */
    public static boolean leavesFullScreen(KeyCode code) {
        return code == KeyCode.MINUS || code == KeyCode.SUBTRACT;
    }
}
