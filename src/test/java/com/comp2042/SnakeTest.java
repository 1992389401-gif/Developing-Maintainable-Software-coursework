package com.comp2042;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

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
    private static void step(Snake snake, Direction direction, Coin coin) {
        snake.move(direction);
        if (!snake.getDead()) {
            snake.ateCoin(coin);
        }
    }

    private static void steps(Snake snake, Direction direction, Coin coin, int count) {
        for (int i = 0; i < count; i++) {
            step(snake, direction, coin);
        }
    }

    /** Returns a snake whose last move was in the given direction. */
    private Snake snakeHeading(Direction heading) {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        if (heading == Direction.RIGHT) {
            step(snake, Direction.UP, farCoin);   // a new snake heads left, so go up before turning right
        }
        step(snake, heading, farCoin);
        return snake;
    }

    /** Regression test for B1: pressing DOWN while moving UP used to reverse the snake. */
    @ParameterizedTest(name = "heading {0} -> turning straight back is refused")
    @EnumSource(Direction.class)
    void cannotTurnStraightBack(Direction heading) {
        Snake snake = snakeHeading(heading);

        assertFalse(snake.canTurn(heading.opposite()));
    }

    /**
     * Regression test for B3: the heading used to live in static fields of GameController,
     * so a new game inherited the direction of the previous one.
     */
    @Test
    void everyNewSnakeStartsHeadingLeftWhateverEarlierSnakesDid() {
        Snake earlier = new Snake(SIZE, SIZE, false, obstacle);
        step(earlier, Direction.UP, farCoin);
        step(earlier, Direction.RIGHT, farCoin);

        Snake next = new Snake(SIZE, SIZE, false, obstacle);

        assertEquals(Direction.LEFT, next.getHeading());
        assertTrue(next.canTurn(Direction.LEFT));
        assertFalse(next.canTurn(Direction.RIGHT));
    }

    @ParameterizedTest(name = "heading {0} -> turning sideways or going on is allowed")
    @EnumSource(Direction.class)
    void canTurnSidewaysOrKeepGoing(Direction heading) {
        Snake snake = snakeHeading(heading);

        for (Direction direction : Direction.values()) {
            if (direction != heading.opposite()) {
                assertTrue(snake.canTurn(direction), "turn to " + direction);
            }
        }
    }

    @Test
    void startsInTheMiddleWithItsTailToTheRight() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        assertEquals(new Point(10, 10), snake.getHead());
        assertEquals(List.of(new Point(11, 10)), snake.getBody());
        assertEquals(Direction.LEFT, snake.getHeading());
        assertFalse(snake.getDead());
    }

    @ParameterizedTest(name = "move {0} -> head at ({1},{2})")
    @CsvSource({
            "UP, 10, 9",
            "DOWN, 10, 11",
            "LEFT, 9, 10"
    })
    void movesOneCellAndKeepsItsLength(Direction direction, int x, int y) {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        step(snake, direction, farCoin);

        assertEquals(new Point(x, y), snake.getHead());
        assertEquals(List.of(new Point(10, 10)), snake.getBody());
        assertEquals(direction, snake.getHeading());
    }

    @Test
    void eatingACoinMakesTheSnakeOneCellLonger() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        Coin coin = new Coin(new Point(9, 10));

        snake.move(Direction.LEFT);

        assertTrue(snake.ateCoin(coin));
        assertEquals(List.of(new Point(11, 10), new Point(10, 10)), snake.getBody());
    }

    @Test
    void solidWallKillsTheSnakeAndLeavesItsHeadOnTheBoard() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        steps(snake, Direction.LEFT, farCoin, 10);
        assertFalse(snake.getDead());
        assertEquals(new Point(0, 10), snake.getHead());

        step(snake, Direction.LEFT, farCoin);
        assertTrue(snake.getDead());
        assertEquals(new Point(0, 10), snake.getHead());
    }

    @ParameterizedTest(name = "{1} moves {0} on an open board -> head at ({2},{3})")
    @CsvSource({
            "LEFT, 11, 19, 10",
            "UP, 11, 10, 19",
            "DOWN, 10, 10, 0"
    })
    void wrapsAroundWhenTheBoardHasNoBorder(Direction direction, int count, int x, int y) {
        Snake snake = new Snake(SIZE, SIZE, true, obstacle);

        steps(snake, direction, farCoin, count);

        assertFalse(snake.getDead());
        assertEquals(new Point(x, y), snake.getHead());
    }

    @Test
    void ignoringBordersAlsoWrapsOnABoardWithBorders() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        snake.ignoreBorders(true);

        steps(snake, Direction.LEFT, farCoin, 11);

        assertFalse(snake.getDead());
        assertEquals(new Point(19, 10), snake.getHead());
    }

    @Test
    void runningIntoAnObstacleKillsTheSnake() {
        obstacle.getObstacles().add(new Point(9, 10));
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);

        step(snake, Direction.LEFT, farCoin);

        assertTrue(snake.getDead());
        assertEquals(new Point(10, 10), snake.getHead());
    }

    @Test
    void bitingItsOwnBodyKillsALongSnake() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        step(snake, Direction.LEFT, new Coin(new Point(9, 10)));
        step(snake, Direction.LEFT, new Coin(new Point(8, 10)));
        step(snake, Direction.LEFT, new Coin(new Point(7, 10)));   // length 5, head at (7,10)

        step(snake, Direction.UP, farCoin);
        step(snake, Direction.RIGHT, farCoin);
        assertFalse(snake.getDead());

        step(snake, Direction.DOWN, farCoin);                      // back down into (8,10)
        assertTrue(snake.getDead());
    }

    /**
     * Regression test for B10: self-collision was only checked for snakes longer than four
     * cells, so together with B1/B3 a short snake could reverse straight through itself.
     */
    @Test
    void aShortSnakeDiesWhenDrivenBackIntoItsBody() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        step(snake, Direction.LEFT, new Coin(new Point(9, 10)));   // length 3, head at (9,10)

        step(snake, Direction.RIGHT, farCoin);                     // into (10,10), which is not the tail

        assertTrue(snake.getDead());
    }

    @Test
    void mayMoveIntoTheCellItsTailIsLeaving() {
        Snake snake = new Snake(SIZE, SIZE, false, obstacle);
        step(snake, Direction.LEFT, new Coin(new Point(9, 10)));
        step(snake, Direction.UP, new Coin(new Point(9, 9)));    // length 4, curled into a 2x2 square
        step(snake, Direction.RIGHT, farCoin);

        step(snake, Direction.DOWN, farCoin);                      // the head takes the tail's old cell

        assertFalse(snake.getDead());
        assertEquals(new Point(10, 10), snake.getHead());
    }
}
