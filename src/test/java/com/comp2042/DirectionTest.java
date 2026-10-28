package com.comp2042;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Direction}.
 */
class DirectionTest {

    @ParameterizedTest(name = "{0} moves by ({1},{2})")
    @CsvSource({
            "UP, 0, -1",
            "DOWN, 0, 1",
            "LEFT, -1, 0",
            "RIGHT, 1, 0"
    })
    void eachDirectionMovesOneCellAlongOneAxis(Direction direction, int dx, int dy) {
        assertEquals(dx, direction.getDx());
        assertEquals(dy, direction.getDy());
    }

    @ParameterizedTest(name = "opposite of {0} is {1}")
    @CsvSource({
            "UP, DOWN",
            "DOWN, UP",
            "LEFT, RIGHT",
            "RIGHT, LEFT"
    })
    void eachDirectionHasAnOpposite(Direction direction, Direction expected) {
        assertEquals(expected, direction.opposite());
    }
}
