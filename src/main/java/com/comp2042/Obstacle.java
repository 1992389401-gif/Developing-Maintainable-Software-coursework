package com.comp2042;

import javafx.scene.paint.Color;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Obstacle {
    /** How many cells straight ahead of the snake's head stay free of new obstacles. */
    static final int SAFE_DISTANCE = 3;

    private Color color = Color.BLACK;
    private ArrayList<Point> obstacles = new ArrayList<>();
    private int x;
    private int y;
    private final Random random;

    public Obstacle(int x, int y) {
       this(x, y, new Random());
    }

    /**
     * Creates an empty set of obstacles for a board of the given size.
     *
     * @param x      the board width in cells
     * @param y      the board height in cells
     * @param random the source used to pick obstacle cells, so that tests can fix the seed
     */
    public Obstacle(int x, int y, Random random) {
       this.x = x;
       this.y = y;
       this.random = random;
    }

    /**
     * Adds up to {@code amount} obstacles on random free cells. Cells under the snake or the
     * coin, cells that already hold an obstacle and the {@value #SAFE_DISTANCE} cells straight
     * ahead of the snake's head are never used, so the player always has time to react.
     *
     * @param snek   the snake to keep clear of
     * @param coin   the coin to keep clear of
     * @param amount how many obstacles to add
     * @return how many were added, which is less than {@code amount} when free cells run out
     */
    public int newObstacles(Snake snek, Coin coin, int amount) {
        if (obstacles.size() > 50) {
            return 0;
        }
        List<Point> body = snek.getBody();
        List<Point> ahead = cellsAhead(snek);
        List<Point> free = new ArrayList<>();
        for (int px = 0; px < x; px++) {
            for (int py = 0; py < y; py++) {
                Point p = new Point(px, py);
                boolean taken = obstacles.contains(p) || p.equals(coin.getPos()) || p.equals(snek.getHead())
                        || body.contains(p) || ahead.contains(p);
                if (!taken) {
                    free.add(p);
                }
            }
        }
        Collections.shuffle(free, random);
        int added = Math.min(amount, free.size());
        obstacles.addAll(free.subList(0, added));
        return added;
    }

    /**
     * Lists the cells straight ahead of the snake's head, wrapping around the board edges.
     *
     * @param snek the snake
     * @return the next {@value #SAFE_DISTANCE} cells in the snake's heading
     */
    private List<Point> cellsAhead(Snake snek) {
        List<Point> cells = new ArrayList<>();
        Point p = snek.getHead();
        Direction heading = snek.getHeading();
        for (int i = 0; i < SAFE_DISTANCE; i++) {
            p = new Point(Math.floorMod(p.x + heading.getDx(), x), Math.floorMod(p.y + heading.getDy(), y));
            cells.add(p);
        }
        return cells;
    }

    public Color getColor() {
        return color;
    }

    public ArrayList<Point> getObstacles() {
        return obstacles;
    }

    public void reset() {
        obstacles.clear();
    }
}
