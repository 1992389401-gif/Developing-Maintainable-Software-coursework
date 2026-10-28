package com.comp2042;

import javafx.scene.paint.Color;

public class Game {
    /** A new snake covers two cells: its head and one body segment. */
    private static final int START_LENGTH = 2;

    private Color color = Color.web("feffe4");
    private int width;
    private int height;
    private int score;
    private int maxScore;
    private double cellSize;
    private int scorePoints;
    private double speed;
    private boolean powerUp;
    private boolean noBorder;

    /**
     * Creates the settings and score keeping for one game.
     *
     * @param x          the board width in cells
     * @param y          the board height in cells
     * @param powerUp    whether special coins can appear
     * @param difficulty the speed and border rule of the game
     */
    public Game(int x, int y, boolean powerUp, Difficulty difficulty) {
        speed = difficulty.getSpeed();
        noBorder = difficulty.isWrapAround();
        this.powerUp = powerUp;
        width = x;
        height = y;
        maxScore = x * y - START_LENGTH;
        if (height > 50) {
            cellSize = 10;
        } else {
            cellSize = 20;
        }
    }

    public boolean gameWon() {
        return score == maxScore;
    }

    public void incScore() {
        score++;
    }

    /**
     * Lowers the number of coins needed to win, because obstacles now cover some free cells.
     *
     * @param num how many new obstacles were placed
     */
    public void decMaxScore(int num) {
        this.maxScore -= num;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Starts a new round: clears the score and restores the number of coins needed to win,
     * since the obstacles of the last round are removed as well.
     */
    public void resetScore() {
        score = 0;
        scorePoints = 0;
        maxScore = width * height - START_LENGTH;
    }

    public Color getColor() {
        return color;
    }

    public double getCellSize() {
        return cellSize;
    }

    public void addPoints(int n) {
        scorePoints += n;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public boolean isPowerUp() {
        return powerUp;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isNoBorder() {
        return noBorder;
    }
}
