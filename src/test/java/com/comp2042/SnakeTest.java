package com.comp2042;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.Point;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization tests that record how {@link Snake} moves, grows and collides in the
 * original code base, so that later fixes and refactoring can be checked against them.
 */
class SnakeTest {

    private static final int SIZE = 20;

    private final Obstacle obstacle = new Obstacle(SIZE, SIZE);
    /** A coin in a corner that none of the paths below reach, so moving never eats it. */
    private final Coin farCoin = new Coin(new Point(0, 0));

    /** Moves one cell the way the game loop does: the tail is dropped unless a coin was eaten. */
    private static void step(Snake snake, char direction, Coin coin) {
        snake.move(direction);
        if (!snake.getDead()) {
            snake.ateCoin(coin);
        }
    }

    private static void steps(Snake snake, char direction, Coin coin, int count) {
        for (int i = 0; i < count; i++) {
            step(snake, direction, coin);
        }
    }

    @Test
    void startsInTheMiddleWithItsTailToTheRight() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        assertEquals(new Point(10, 10), snake.getHead());
        assertEquals(List.of(new Point(11, 10)), snake.getBody());
        assertEquals(270, snake.getSnakeDir());
        assertFalse(snake.getDead());
    }

    @ParameterizedTest(name = "move {0} -> head at ({1},{2}), facing {3} degrees")
    @CsvSource({
            "U, 10, 9, 0",
            "D, 10, 11, 180",
            "L, 9, 10, 270"
    })
    void movesOneCellAndKeepsItsLength(char direction, int x, int y, int angle) {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        step(snake, direction, farCoin);

        assertEquals(new Point(x, y), snake.getHead());
        assertEquals(List.of(new Point(10, 10)), snake.getBody());
        assertEquals(angle, snake.getSnakeDir());
    }

    @Test
    void eatingACoinMakesTheSnakeOneCellLonger() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        Coin coin = new Coin(new Point(9, 10));

        snake.move('L');

        assertTrue(snake.ateCoin(coin));
        assertEquals(List.of(new Point(11, 10), new Point(10, 10)), snake.getBody());
    }

    @Test
    void solidWallKillsTheSnakeAndLeavesItsHeadOnTheBoard() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        steps(snake, 'L', farCoin, 10);
        assertFalse(snake.getDead());
        assertEquals(new Point(0, 10), snake.getHead());

        step(snake, 'L', farCoin);
        assertTrue(snake.getDead());
        assertEquals(new Point(0, 10), snake.getHead());
    }

    @ParameterizedTest(name = "{1} moves {0} on an open board -> head at ({2},{3})")
    @CsvSource({
            "L, 11, 19, 10",
            "U, 11, 10, 19",
            "D, 10, 10, 0"
    })
    void wrapsAroundWhenTheBoardHasNoBorder(char direction, int count, int x, int y) {
        Snake snake = new Snake(SIZE, SIZE, true, obstacle);

        steps(snake, direction, farCoin, count);

        assertFalse(snake.getDead());
        assertEquals(new Point(x, y), snake.getHead());
    }

    @Test
    void ignoringBordersAlsoWrapsOnABoardWithBorders() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        snake.ignoreBorders(true);

        steps(snake, 'L', farCoin, 11);

        assertFalse(snake.getDead());
        assertEquals(new Point(19, 10), snake.getHead());
    }

    @Test
    void runningIntoAnObstacleKillsTheSnake() {
        obstacle.getObstacles().add(new Point(9, 10));
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        step(snake, 'L', farCoin);

        assertTrue(snake.getDead());
        assertEquals(new Point(10, 10), snake.getHead());
    }

    @Test
    void bitingItsOwnBodyKillsALongSnake() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        step(snake, 'L', new Coin(new Point(9, 10)));
        step(snake, 'L', new Coin(new Point(8, 10)));
        step(snake, 'L', new Coin(new Point(7, 10)));   // length 5, head at (7,10)

        step(snake, 'U', farCoin);
        step(snake, 'R', farCoin);
        assertFalse(snake.getDead());

        step(snake, 'D', farCoin);                      // back down into (8,10)
        assertTrue(snake.getDead());
    }

    @Test
    void mayMoveIntoTheCellItsTailIsLeaving() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        step(snake, 'L', new Coin(new Point(9, 10)));
        step(snake, 'U', new Coin(new Point(9, 9)));    // length 4, curled into a 2x2 square
        step(snake, 'R', farCoin);

        step(snake, 'D', farCoin);                      // the head takes the tail's old cell

        assertFalse(snake.getDead());
        assertEquals(new Point(10, 10), snake.getHead());
    }
}
