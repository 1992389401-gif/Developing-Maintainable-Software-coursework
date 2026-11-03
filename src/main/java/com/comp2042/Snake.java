package com.comp2042;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private boolean dead = false;
    private Position head;
    private final List<Position> body = new ArrayList<>();
    private final int width;
    private final int height;
    private boolean ignoreBorders = false;
    private boolean noBorder;
    private Obstacle obstacle;
    private Direction heading = Direction.LEFT;

    public Snake(int x, int y, boolean noBorder, Obstacle obstacle) {
        this.obstacle = obstacle;
        this.noBorder = noBorder;
        this.head = new Position(x / 2, y / 2);
        this.body.add(new Position(x / 2 + 1, y / 2));
        this.width = x;
        this.height = y;
    }

    /**
     * Moves the head one cell in the given direction. The snake dies if it hits a solid
     * border, an obstacle or its own body; on an open board it wraps to the opposite edge.
     *
     * @param direction the direction to move in
     */
    public void move(Direction direction) {
        Position previous = head;
        body.add(previous);
        heading = direction;
        head = head.step(direction);
        if (!head.isInside(width, height)) {
            if (ignoreBorders || noBorder) {
                head = head.wrap(width, height);
            } else {
                head = previous;
                dead = true;
            }
        }
        // Checked after wrapping, so an obstacle on the far edge is not skipped.
        if (obstacle.getObstacles().contains(head)) {
            head = previous;
            dead = true;
        }

        if (hitSelf() && !body.get(0).equals(head)) {
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
        } else if (body.get(bodyCount).y() == body.get(bodyCount - 1).y()) {
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

    public boolean hitSelf() {
        return body.contains(head);
    }

    /**
     * Returns the body cells, from the tail up to the cell behind the head.
     *
     * @return an unmodifiable copy of the body
     */
    public List<Position> getBody() {
        return List.copyOf(body);
    }

    /**
     * Returns the cell of the head. Positions are immutable, so no copy is needed.
     *
     * @return the head position
     */
    public Position getHead() {
        return head;
    }

    public boolean isDead() {
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
