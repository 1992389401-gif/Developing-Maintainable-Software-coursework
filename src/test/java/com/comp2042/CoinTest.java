package com.comp2042;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

/**
 * Characterization tests that record where coins appear and what each kind of coin is worth
 * in the original code base.
 */
class CoinTest {

    @Test
    void spawnsOnTheOnlyCellTheSnakeDoesNotCover() {
        // On a 3x1 board the snake covers (1,0) and (2,0), leaving only (0,0) free.
        Obstacle obstacle = new Obstacle(3, 1);
        Snake snake = new Snake(3, 1, false, obstacle);

        Coin coin = new Coin(3, 1, snake, obstacle);

        assertEquals(new Point(0, 0), coin.getPos());
    }

    @Test
    void neverSpawnsOnAnObstacle() {
        // On a 3x3 board the snake covers (1,1) and (2,1); obstacles fill all but (2,2).
        Obstacle obstacle = new Obstacle(3, 3);
        for (Point p : new Point[]{new Point(0, 0), new Point(1, 0), new Point(2, 0),
                new Point(0, 1), new Point(0, 2), new Point(1, 2)}) {
            obstacle.getObstacles().add(p);
        }
        Snake snake = new Snake(3, 3, false, obstacle);

        Coin coin = new Coin(3, 3, snake, obstacle);

        assertEquals(new Point(2, 2), coin.getPos());
    }

    /** Regression test for B7: with no free cell left, the constructor used to loop forever. */
    @Test
    void refusesToSpawnOnAFullBoardInsteadOfHanging() {
        Obstacle obstacle = new Obstacle(3, 1);
        obstacle.getObstacles().add(new Point(0, 0));   // the one cell the snake leaves free
        Snake snake = new Snake(3, 1, false, obstacle);

        assertTimeoutPreemptively(Duration.ofSeconds(2),
                () -> assertThrows(IllegalStateException.class, () -> new Coin(3, 1, snake, obstacle)));
    }

    @Test
    void eachKindOfCoinHasItsOwnTypeAndPoints() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);

        assertCoin(new Coin(20, 20, snake, obstacle), CoinType.RED, 5);
        assertCoin(new GoldCoin(20, 20, snake, obstacle), CoinType.GOLD, 15);
        assertCoin(new SilverCoin(20, 20, snake, obstacle), CoinType.SILVER, 5);
        assertCoin(new BlueCoin(20, 20, snake, obstacle), CoinType.BLUE, 5);
        assertCoin(new RainbowCoin(20, 20, snake, obstacle), CoinType.RAINBOW, 5);
    }

    @Test
    void rainbowCoinGivesASpeedBonus() {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);

        assertEquals(5.0, new RainbowCoin(20, 20, snake, obstacle).getSpeedBonus());
    }

    private static void assertCoin(Coin coin, CoinType expectedType, int expectedPoints) {
        assertEquals(expectedType, coin.getType());
        assertEquals(expectedPoints, coin.getPoints());
    }
}
