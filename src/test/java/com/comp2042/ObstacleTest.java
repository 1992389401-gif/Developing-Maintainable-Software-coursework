package com.comp2042;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization tests that record how {@link Obstacle} adds and clears obstacles in the
 * original code base.
 */
class ObstacleTest {

    private final Obstacle obstacle = new Obstacle(20, 20);
    private final Snake snake = new Snake(20, 20, false, obstacle);
    private final Coin coin = new Coin(new Point(3, 3));

    @Test
    void startsWithoutObstacles() {
        assertTrue(obstacle.getObstacles().isEmpty());
    }

    @Test
    void addsTheRequestedNumberOfObstacles() {
        obstacle.newObstacles(snake, coin, 7);

        assertEquals(7, obstacle.getObstacles().size());
    }

    @Test
    void obstaclesAvoidTheSnakeAndTheCoin() {
        obstacle.newObstacles(snake, coin, 40);

        for (Point p : obstacle.getObstacles()) {
            assertFalse(p.equals(snake.getHead()) || snake.getBody().contains(p) || p.equals(coin.getPos()),
                    "obstacle placed on an occupied cell: " + p);
        }
    }

    /** Regression test for B6: x was rolled as nextInt(width) + 1, so x == width was possible. */
    @Test
    void obstaclesStayOnTheBoard() {
        obstacle.newObstacles(snake, coin, 200);

        for (Point p : obstacle.getObstacles()) {
            assertTrue(p.x >= 0 && p.x < 20 && p.y >= 0 && p.y < 20, "off the board: " + p);
        }
    }

    /** Regression test for B6: the same cell could be picked twice. */
    @Test
    void noTwoObstaclesShareACell() {
        obstacle.newObstacles(snake, coin, 200);

        assertEquals(obstacle.getObstacles().size(), new HashSet<>(obstacle.getObstacles()).size());
    }

    /** Regression test for B6: because of the "+ 1", the first column never got an obstacle. */
    @Test
    void theFirstColumnCanGetObstacles() {
        obstacle.newObstacles(snake, coin, 400);

        assertTrue(obstacle.getObstacles().stream().anyMatch(p -> p.x == 0));
    }

    /** Regression test for B6: an obstacle could appear right in front of the head. */
    @Test
    void keepsTheCellsAheadOfTheHeadClear() {
        obstacle.newObstacles(snake, coin, 400);

        // A new snake heads left from (10,10).
        for (int x = 7; x <= 9; x++) {
            assertFalse(obstacle.getObstacles().contains(new Point(x, 10)), "blocked: (" + x + ",10)");
        }
    }

    @Test
    void addsOnlyAsManyObstaclesAsThereAreFreeCells() {
        int added = obstacle.newObstacles(snake, coin, 1000);

        // 400 cells minus the snake (2), the coin (1) and the three cells ahead of the head.
        assertEquals(394, added);
        assertEquals(394, obstacle.getObstacles().size());
    }

    @Test
    void theSameSeedGivesTheSameObstacles() {
        Obstacle first = new Obstacle(20, 20, new Random(7));
        Obstacle second = new Obstacle(20, 20, new Random(7));

        first.newObstacles(snake, coin, 10);
        second.newObstacles(snake, coin, 10);

        assertEquals(first.getObstacles(), second.getObstacles());
    }

    @Test
    void resetRemovesEveryObstacle() {
        obstacle.newObstacles(snake, coin, 7);

        obstacle.reset();

        assertTrue(obstacle.getObstacles().isEmpty());
    }
}
