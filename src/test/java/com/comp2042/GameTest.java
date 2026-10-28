package com.comp2042;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization tests that record how {@link Game} behaves in the original code base,
 * so that later refactoring can be checked against them.
 */
class GameTest {

    @ParameterizedTest(name = "difficulty {0} -> speed {1}, wrap-around borders {2}")
    @CsvSource({
            "E, 8.5, true",
            "N, 8.5, false",
            "H, 13.0, false"
    })
    void difficultySetsSpeedAndBorderMode(char difficulty, double expectedSpeed, boolean expectedNoBorder) {
        Game game = new Game(20, 20, true, difficulty);

        assertEquals(expectedSpeed, game.getSpeed());
        assertEquals(expectedNoBorder, game.isNoBorder());
    }

    @ParameterizedTest(name = "board height {0} -> cell size {1}px")
    @CsvSource({
            "20, 20.0",
            "50, 20.0",
            "51, 10.0",
            "60, 10.0"
    })
    void cellSizeHalvesForBoardsTallerThanFifty(int height, double expectedMultiplier) {
        Game game = new Game(20, height, true, 'N');

        assertEquals(expectedMultiplier, game.getMultiplier());
    }

    @Test
    void scorePointsAccumulateAndReset() {
        Game game = new Game(20, 20, true, 'N');

        game.addPoints(5);
        game.addPoints(15);
        assertEquals(20, game.getScorePoints());

        game.resetScore();
        assertEquals(0, game.getScorePoints());
    }

    @Test
    void gameIsWonOnlyOnceEveryFreeCellHasBeenEaten() {
        // A 3x3 board starts with a snake of length 2, so 7 coins fill it.
        Game game = new Game(3, 3, false, 'N');

        for (int i = 0; i < 6; i++) {
            game.incScore();
        }
        assertFalse(game.gameWon());

        game.incScore();
        assertTrue(game.gameWon());
    }

    /** Regression test for B7: each obstacle takes one free cell, so one coin fewer is needed. */
    @Test
    void eachObstacleMeansOneCoinFewerToWin() {
        Game game = new Game(3, 3, false, 'N');   // 7 free cells
        game.decMaxScore(3);                      // 3 of them become obstacles

        for (int i = 0; i < 3; i++) {
            game.incScore();
        }
        assertFalse(game.gameWon());

        game.incScore();
        assertTrue(game.gameWon());
    }
}
