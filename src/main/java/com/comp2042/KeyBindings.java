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
}
