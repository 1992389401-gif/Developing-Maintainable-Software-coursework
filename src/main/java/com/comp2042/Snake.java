package com.comp2042;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;

public class Snake {
    private Color headColor = Color.web("2eb872");
    private Color bodyColor = Color.web("a3de83");
    private boolean dead = false;
    private final Point head = new Point();
    private final ArrayList<Point> body = new ArrayList<>();
    private final int gx;
    private final int gy;
    private boolean ignoreBorders = false;
    private boolean noBorder;
    private Obstacle obstacle;
    private Direction heading = Direction.LEFT;

    public Snake(int x, int y, boolean noBorder, Obstacle obstacle) {
        this.obstacle = obstacle;
        this.noBorder = noBorder;
        this.head.setLocation(x / 2, y / 2);
        this.body.add(new Point(x / 2 + 1, y / 2));
        this.gx = x;
        this.gy = y;
    }

    /**
     * Moves the head one cell in the given direction. The snake dies if it hits a solid
     * border, an obstacle or its own body; on an open board it wraps to the opposite edge.
     *
     * @param direction the direction to move in
     */
    public void move(Direction direction) {
        body.add(new Point(head));
        heading = direction;
        head.translate(direction.getDx(), direction.getDy());
        if (borderControl() && (!ignoreBorders && !noBorder) || obstacle.getObstacles().contains(head)) {
            head.translate(-direction.getDx(), -direction.getDy());
            dead = true;
        } else if (borderControl() && (ignoreBorders || noBorder)) {
            head.setLocation(Math.floorMod(head.x, gx), Math.floorMod(head.y, gy));
        }

        if (body.size() > 4 && hitSelf() && !body.get(0).equals(head)) {
            dead = true;
        }
    }

    /**
     * Tells whether the snake may move in the given direction next. Turning straight back
     * would drive the head into the body, so it is refused; turning sideways or keeping the
     * same direction is allowed.
     *
     * @param direction the direction the player asked for
     * @return {@code false} only for the direction opposite to the current heading
     */
    public boolean canTurn(Direction direction) {
        return direction != heading.opposite();
    }

    public int getBodyDir(int bodyCount) {
        if (bodyCount == 0) {
            return 0;
        } else if (body.get(bodyCount).getY() == body.get(bodyCount - 1).getY()) {
            return 270;
        } else {
            return 0;
        }
    }

    public boolean ateCoin(Coin coin) {
        if (!head.equals(coin.getPos())) {
            body.remove(0);
        }
        return head.equals(coin.getPos());
    }

    private boolean borderControl() {
        return head.getX() < 0 || head.getY() < 0 || head.getX() >= gx || head.getY() >= gy;
    }

    public boolean hitSelf() {
        return body.contains(head);
    }

    public ArrayList<Point> getBody() {
        return new ArrayList<>(body);
    }

    public Point getHead() {
        return new Point(head);
    }

    public Color getHeadColor() {
        return headColor;
    }

    public Color getBodyColor() {
        return bodyColor;
    }

    public boolean getDead() {
        return dead;
    }

    public void ignoreBorders(boolean ign) {
        ignoreBorders = ign;
    }

    /**
     * Returns the direction of the snake's most recent move. A new snake heads left,
     * because its tail starts to the right of its head.
     *
     * @return the current heading
     */
    public Direction getHeading() {
        return heading;
    }
}
