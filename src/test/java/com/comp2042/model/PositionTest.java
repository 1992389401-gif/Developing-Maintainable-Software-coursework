package com.comp2042.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Position}.
 */
class PositionTest {

    @ParameterizedTest(name = "{0} from (5,5) -> ({1},{2})")
    @CsvSource({
            "UP, 5, 4",
            "DOWN, 5, 6",
            "LEFT, 4, 5",
            "RIGHT, 6, 5"
    })
    void stepMovesOneCellInTheDirection(Direction direction, int x, int y) {
        assertEquals(new Position(x, y), new Position(5, 5).step(direction));
    }

    @ParameterizedTest(name = "({0},{1}) on a 20x20 board: {2}")
    @CsvSource({
            "0, 0, true",
            "19, 19, true",
            "-1, 5, false",
            "5, -1, false",
            "20, 5, false",
            "5, 20, false"
    })
    void onlyCellsFromZeroToSizeMinusOneAreInside(int x, int y, boolean inside) {
        assertEquals(inside, new Position(x, y).isInside(20, 20));
    }

    @ParameterizedTest(name = "({0},{1}) wraps to ({2},{3})")
    @CsvSource({
            "-1, 5, 19, 5",
            "20, 5, 0, 5",
            "5, -1, 5, 19",
            "5, 20, 5, 0",
            "7, 8, 7, 8"
    })
    void wrapBringsACellBackOnTheOppositeSide(int x, int y, int wrappedX, int wrappedY) {
        assertEquals(new Position(wrappedX, wrappedY), new Position(x, y).wrap(20, 20));
    }

    @Test
    void positionsWithTheSameCoordinatesAreEqual() {
        assertEquals(new Position(3, 4), new Position(3, 4));
        assertEquals(new Position(3, 4).hashCode(), new Position(3, 4).hashCode());
    }
}
