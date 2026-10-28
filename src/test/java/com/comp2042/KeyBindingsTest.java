package com.comp2042;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
}
