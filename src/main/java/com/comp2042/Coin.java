package com.comp2042;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Coin {
    private Point pos;

    /**
     * Creates a coin on a random cell that is not covered by the snake or an obstacle.
     *
     * @param x        the board width in cells
     * @param y        the board height in cells
     * @param snake     the snake, whose cells are not free
     * @param obstacle the obstacles, whose cells are not free
     * @throws IllegalStateException if no cell is free; the game should have been won by then
     */
    public Coin(int x, int y, Snake snake, Obstacle obstacle) {
        List<Point> body = snake.getBody();
        List<Point> free = new ArrayList<>();
        for (int px = 0; px < x; px++) {
            for (int py = 0; py < y; py++) {
                Point p = new Point(px, py);
                if (!p.equals(snake.getHead()) && !body.contains(p) && !obstacle.getObstacles().contains(p)) {
                    free.add(p);
                }
            }
        }
        if (free.isEmpty()) {
            throw new IllegalStateException("No free cell left for a coin");
        }
        pos = free.get(new Random().nextInt(free.size()));
    }

    /**
     * Creates a coin on a given cell. Unit tests use it to put a coin exactly where the
     * snake will move next.
     *
     * @param pos the cell the coin occupies
     */
    Coin(Point pos) {
        this.pos = new Point(pos);
    }

    public Point getPos() {
        return new Point(pos);
    }

    public int getPoints() {
        int points = 5;
        return points;
    }

    /**
     * Returns the kind of this coin. The view uses it to choose the sprite to draw.
     *
     * @return {@link CoinType#RED} for a plain coin
     */
    public CoinType getType() {
        return CoinType.RED;
    }
}
