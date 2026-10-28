package com.comp2042;

import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.Point;
import java.util.EnumMap;
import java.util.Map;

public class GameView {

    public Canvas gridCanvas;
    public GraphicsContext gc;

    public Label gameOverLabel;
    public Label gameWonLabel;
    public Label scoreLabel;
    private boolean gameOverVis = false;
    private boolean gameWonVis = false;

    private Game game;
    private double initX;
    private double initY;
    private double fullScale = 1;
    private boolean fullsize = false;
    private int[][] turnArray;

    private final ImageView headSprite = new ImageView(new Image("resources/views/res/snakehead.png"));
    private final ImageView bodySprite = new ImageView(new Image("resources/views/res/snakebody.png"));
    private final ImageView turnSprite = new ImageView(new Image("resources/views/res/snaketurnleft.png"));
    private final ImageView tailSprite = new ImageView(new Image("resources/views/res/snaketail.png"));
    private final Map<CoinType, Image> coinSprites = loadCoinSprites();

    public GameView(Game game) {
        this.game = game;
        this.turnArray = new int[game.getWidth()][game.getHeight()];
    }

    /**
     * Forgets the corners recorded in the previous round, so that a restarted game does not
     * draw old corner sprites.
     */
    public void clearTurns() {
        turnArray = new int[game.getWidth()][game.getHeight()];
    }

    /**
     * Loads every coin sprite once, so placing a new coin does not reload its image.
     *
     * @return the sprite for each coin type
     */
    private static Map<CoinType, Image> loadCoinSprites() {
        Map<CoinType, Image> sprites = new EnumMap<>(CoinType.class);
        sprites.put(CoinType.RED, new Image("resources/views/res/redcoin.png"));
        sprites.put(CoinType.GOLD, new Image("resources/views/res/goldcoin.png"));
        sprites.put(CoinType.SILVER, new Image("resources/views/res/silvercoin.png"));
        sprites.put(CoinType.BLUE, new Image("resources/views/res/bluecoin.png"));
        sprites.put(CoinType.RAINBOW, new Image("resources/views/res/rainbowcoin.png"));
        return sprites;
    }

    public void makeScene(Scene scene, Snake snake, Coin coin, Obstacle obstacle) {
        Scene gameScene = scene;
        gridCanvas = new Canvas(gameScene.getWidth(), gameScene.getHeight());
        initX = scene.getWidth();
        initY = scene.getHeight();
        gc = gridCanvas.getGraphicsContext2D();
        drawGrid(coin, snake, obstacle);

        gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setId("gameOverLabel");
        String css = "resources/views/styling.css";
        gameOverLabel.getStylesheets().add(css);
        gameOverLabel.setVisible(false);

        gameWonLabel = new Label("YOU WON!");
        gameWonLabel.setId("gameWonLabel");
        gameWonLabel.getStylesheets().add(css);
        gameWonLabel.setVisible(false);

        scoreLabel = new Label("Score: " + game.getScorePoints());
        scoreLabel.setId("scoreLabel");
        scoreLabel.getStylesheets().add(css);
        scoreLabel.setFont(new Font("Arial", 20));
        scoreLabel.setVisible(true);
    }

    public void scaleToFullscreen(boolean fullScreen, Stage primaryStage, Obstacle obstacle, Snake snake, Coin coin) {
        primaryStage.setFullScreen(fullScreen);

        double xScaling = Screen.getPrimary().getVisualBounds().getHeight() / initY;
        double yScaling = Screen.getPrimary().getVisualBounds().getWidth() / initX;

        if (fullScreen && !fullsize) {
            if (xScaling < yScaling) {
                fullScale = xScaling;
            } else {
                fullScale = yScaling;
            }
            gridCanvas.setWidth(gridCanvas.getWidth() * fullScale);
            gridCanvas.setHeight(gridCanvas.getHeight() * fullScale);
            fullsize = true;
        } else if (!fullScreen) {
            fullScale = 1;
            gridCanvas.setWidth(initX);
            gridCanvas.setHeight(initY);
            fullsize = false;
        }

        gameOverLabel.setFont(new Font("Arial", 25 * fullScale));
        gameWonLabel.setFont(new Font("Arial", 25 * fullScale));
        drawGrid(coin, snake, obstacle);
    }

    public void drawGrid(Coin coin, Snake snake, Obstacle obstacle) {
        gc.setFill(game.getColor());
        gc.fillRect(0, 0, gridCanvas.getWidth(), gridCanvas.getHeight());

        gc.drawImage(coinSprites.get(coin.getType()), (coin.getPos().x * game.getCellSize()) * fullScale, (coin.getPos().y * game.getCellSize()) * fullScale, game.getCellSize() * fullScale - 3 * fullScale, game.getCellSize() * fullScale - 3 * fullScale);

        int bodyIndex = 0;
        for (Point point : snake.getBody()) {
            drawBody(snake, point, bodyIndex);
            bodyIndex++;
        }

        drawHead(snake);

        gc.setFill(obstacle.getColor());
        for (Point point : obstacle.getObstacles()) {
            gc.fillRect(point.x * game.getCellSize() * fullScale, point.y * game.getCellSize() * fullScale, game.getCellSize() * fullScale, game.getCellSize() * fullScale);
        }
    }

    /**
     * Converts a heading into the rotation of the head and tail sprites, which face up
     * when not rotated.
     *
     * @param direction the snake's heading
     * @return the clockwise rotation in degrees
     */
    private static int rotationOf(Direction direction) {
        return switch (direction) {
            case UP -> 0;
            case RIGHT -> 90;
            case DOWN -> 180;
            case LEFT -> 270;
        };
    }

    private void drawHead(Snake snake) {
        headSprite.setRotate(rotationOf(snake.getHeading()));
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Image rotatedImage = headSprite.snapshot(params, null);
        gc.drawImage(rotatedImage, (snake.getHead().x * game.getCellSize()) * fullScale, (snake.getHead().y * game.getCellSize()) * fullScale, game.getCellSize() * fullScale, game.getCellSize() * fullScale);
    }

    private void drawBody(Snake snake, Point point, int index) {
        Image rotatedImage;
        if (snake.getBody().size() <= 1) {
            tailSprite.setRotate(rotationOf(snake.getHeading()));
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            rotatedImage = tailSprite.snapshot(params, null);
        } else if (index == 0) {
            int rotateTail;
            if (snake.getBody().get(1).getX() == snake.getBody().get(0).getX() && snake.getBody().get(1).getY() == snake.getBody().get(0).getY() - 1) {
                rotateTail = 0;
            } else if (snake.getBody().get(1).getX() == snake.getBody().get(0).getX() && snake.getBody().get(1).getY() == snake.getBody().get(0).getY() + 1) {
                rotateTail = 180;
            } else if (snake.getBody().get(1).getX() == snake.getBody().get(0).getX() - 1 && snake.getBody().get(1).getY() == snake.getBody().get(0).getY()) {
                rotateTail = 270;
            } else {
                rotateTail = 90;
            }
            tailSprite.setRotate(rotateTail);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            rotatedImage = tailSprite.snapshot(params, null);
        } else if (index > 0 && (turnArray[(int) snake.getBody().get(index).getX()][(int) snake.getBody().get(index).getY()] != 0)) {
            turnSprite.setRotate(90 * turnArray[(int) snake.getBody().get(index).getX()][(int) snake.getBody().get(index).getY()]);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            rotatedImage = turnSprite.snapshot(params, null);
        } else {
            bodySprite.setRotate(snake.getBodyDir(index));
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            rotatedImage = bodySprite.snapshot(params, null);
        }

        if (turnArray[(int) snake.getBody().get(0).getX()][(int) snake.getBody().get(0).getY()] != 0) {
            turnArray[(int) snake.getBody().get(0).getX()][(int) snake.getBody().get(0).getY()] = 0;
        }

        gc.drawImage(rotatedImage, (point.x * game.getCellSize()) * fullScale, (point.y * game.getCellSize()) * fullScale, game.getCellSize() * fullScale, game.getCellSize() * fullScale);
    }

    /**
     * Records which corner sprite to draw on the head's cell when the snake turns there.
     *
     * @param snake the snake that is turning
     * @param from the direction of the snake's last move
     * @param to   the direction of its next move
     */
    public void turnDir(Snake snake, Direction from, Direction to) {
        if ((from == Direction.LEFT && to == Direction.UP) || (from == Direction.DOWN && to == Direction.RIGHT)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 2;
        } else if ((from == Direction.LEFT && to == Direction.DOWN) || (from == Direction.UP && to == Direction.RIGHT)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 3;
        } else if ((from == Direction.RIGHT && to == Direction.UP) || (from == Direction.DOWN && to == Direction.LEFT)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 1;
        } else if ((from == Direction.UP && to == Direction.LEFT) || (from == Direction.RIGHT && to == Direction.DOWN)) {
            turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 4;
        } else turnArray[(int) snake.getHead().getX()][(int) snake.getHead().getY()] = 0;
    }

    public void gameOver(Snake snake) {
        if (snake.isDead() && !gameOverVis) {
            gameOverVis = true;
            gameOverLabel.setVisible(true);
        } else if (!snake.isDead() && gameOverVis) {
            gameOverLabel.setVisible(false);
            gameOverVis = false;
        }
    }

    public void gameWon() {
        if (game.gameWon() && !gameWonVis) {
            gameWonVis = true;
            gameWonLabel.setVisible(true);
        } else if (!game.gameWon() && gameWonVis) {
            gameWonVis = false;
            gameWonLabel.setVisible(false);
        }
    }

    public void updateScoreLabel() {
        scoreLabel.setText("Score: " + game.getScorePoints());
    }

}
