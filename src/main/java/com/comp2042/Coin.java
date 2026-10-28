package com.comp2042;

import java.awt.*;
import java.util.Random;

public class Coin {
    private Point pos;

    public Coin(int x, int y, Snake snek, Obstacle obstacle) {
        int tx;
        int ty;
        do {
            Random rnd = new Random();

            tx = rnd.nextInt(x);
            ty = rnd.nextInt(y);
            pos = new Point(tx, ty);
        } while (snek.getHead().equals(pos) || snek.getBody().contains(pos) || obstacle.getObstacles().contains(pos));
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
