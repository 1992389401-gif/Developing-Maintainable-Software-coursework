package com.comp2042;

import org.junit.jupiter.api.Test;

import java.awt.Point;

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

    @Test
    void resetRemovesEveryObstacle() {
        obstacle.newObstacles(snake, coin, 7);

        obstacle.reset();

        assertTrue(obstacle.getObstacles().isEmpty());
    }
}
