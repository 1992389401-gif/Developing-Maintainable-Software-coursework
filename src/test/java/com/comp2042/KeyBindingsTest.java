package com.comp2042;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for {@link KeyBindings}.
 */
class KeyBindingsTest {

    @ParameterizedTest(name = "{0} key -> {1}")
    @CsvSource({
            "UP, UP",
            "DOWN, DOWN",
            "LEFT, LEFT",
            "RIGHT, RIGHT"
    })
    void arrowKeysStandForDirections(KeyCode key, Direction expected) {
        assertEquals(expected, KeyBindings.directionFor(key));
    }

    @ParameterizedTest(name = "{0} key is not a direction")
    @EnumSource(value = KeyCode.class, names = {"W", "SPACE", "P", "TAB", "ESCAPE"})
    void otherKeysAreNotDirections(KeyCode key) {
        assertNull(KeyBindings.directionFor(key));
    }

    /**
     * Regression test for B13: only PLUS was accepted, but US-style keyboards report the "+"
     * key as EQUALS and the numeric keypad reports ADD.
     */
    @ParameterizedTest(name = "{0} enters full screen")
    @EnumSource(value = KeyCode.class, names = {"PLUS", "EQUALS", "ADD"})
    void plusKeysEnterFullScreen(KeyCode key) {
        assertTrue(KeyBindings.entersFullScreen(key));
    }

    @ParameterizedTest(name = "{0} leaves full screen")
    @EnumSource(value = KeyCode.class, names = {"MINUS", "SUBTRACT"})
    void minusKeysLeaveFullScreen(KeyCode key) {
        assertTrue(KeyBindings.leavesFullScreen(key));
    }

    @ParameterizedTest(name = "{0} does not change the screen mode")
    @EnumSource(value = KeyCode.class, names = {"UP", "P", "TAB", "ESCAPE", "DIGIT0"})
    void otherKeysDoNotChangeTheScreenMode(KeyCode key) {
        assertFalse(KeyBindings.entersFullScreen(key));
        assertFalse(KeyBindings.leavesFullScreen(key));
    }
}
