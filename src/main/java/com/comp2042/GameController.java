package com.comp2042;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Random;

public class GameController {
    private Snake snake;
    private Coin coin;
    private GameView gameView;
    private Game game;
    private Stage stage;
    private final AudioClip coinSound = loadSound("/resources/views/res/coinSound.wav");
    private Obstacle obstacle;
    private final CoinFactory coinFactory = new CoinFactory();

    /** Where the snake moves on the next tick. Every game has its own, starting left. */
    private Direction direction = Direction.LEFT;
    private boolean keyPressed = false;

    public GameController(Snake snake, Coin coin, Game game, GameView gameView, Obstacle obstacle, Stage stage) {
        this.snake = snake;
        this.coin = coin;
        this.gameView = gameView;
        this.game = game;
        this.stage = stage;

        this.obstacle = obstacle;
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setRate(game.getSpeed());
    }

    private final Timeline animation = new Timeline(new KeyFrame(Duration.seconds(1), event -> tick()));

    /**
     * Loads a short sound effect from the class path. A missing file only produces a
     * warning, so the game still runs without that sound.
     *
     * @param path the resource path of the sound file
     * @return the loaded clip, or {@code null} if the file was not found
     */
    private static AudioClip loadSound(String path) {
        URL url = GameController.class.getResource(path);
        if (url == null) {
            System.err.println("Sound not found: " + path);
            return null;
        }
        return new AudioClip(url.toExternalForm());
    }

    /**
     * Advances the game by one step: moves the snake, handles an eaten coin and redraws.
     * The timeline calls it a few times per second, depending on the game speed.
     */
    private void tick() {
        if (snake.isDead() || game.gameWon()) {
            return;
        }

        snake.move(direction);

        if (!snake.isDead() && snake.ateCoin(coin)) {
            if (coinSound != null) {
                coinSound.play();
            }
            powerCoin();
            game.incScore();
            if (game.gameWon()) {
                animation.stop();
            } else {
                newCoin();
            }
        }

        keyPressed = false;

        if (!snake.isDead() || snake.hitSelf()) {
            gameView.drawGrid(coin, snake, obstacle);
        }

        gameView.gameWon();
        gameView.gameOver(snake);
        gameView.updateScoreLabel();
    }

    public void handle(KeyEvent event) {
        if (event.getCode().isArrowKey()) animation.play();

        Direction requested = KeyBindings.directionFor(event.getCode());
        if (requested != null && snake.canTurn(requested) && !keyPressed) {
            gameView.turnDir(snake, snake.getHeading(), requested);
            direction = requested;
            keyPressed = true;
        }

        if (event.getCode().equals(KeyCode.P)) {
            animation.stop();
        }

        if (event.getCode().equals(KeyCode.TAB)) {
            reset();
            gameView.scoreLabel.setText("Score: " + game.getScorePoints());
        }

        if (KeyBindings.entersFullScreen(event.getCode())) {
            gameView.scaleToFullscreen(true, stage, obstacle, snake, coin);
            gameView.gameOverLabel.setStyle("-fx-font-size: 50;");
            gameView.gameWonLabel.setStyle("-fx-font-size: 50;");
            gameView.scoreLabel.setStyle("-fx-font-size: 35;");
        }

        if (KeyBindings.leavesFullScreen(event.getCode())) {
            gameView.scaleToFullscreen(false, stage, obstacle, snake, coin);
            gameView.gameOverLabel.setStyle("-fx-font-size: 15;");
            gameView.gameWonLabel.setStyle("-fx-font-size: 15;");
            gameView.scoreLabel.setStyle("-fx-font-size: 15;");
        }

        if (event.getCode().equals(KeyCode.ESCAPE)) {
            animation.stop();
            MainMenuController mainMenuController = new MainMenuController();
            mainMenuController.getViewManager().getPrimaryStage().show();
            mainMenuController.getViewManager().getGameStage().close();
        }
    }

    private void reset() {

        animation.jumpTo(Duration.ZERO);
        animation.stop();

        snake = new Snake(game.getWidth(), game.getHeight(), game.isNoBorder(), obstacle);
        coin = new Coin(game.getWidth(), game.getHeight(), snake, obstacle);

        animation.setRate(game.getSpeed());
        direction = Direction.LEFT;
        keyPressed = false;
        game.resetScore();
        obstacle.reset();
        gameView.clearTurns();

        gameView.gameWon();
        gameView.gameOver(snake);
        gameView.updateScoreLabel();

        gameView.drawGrid(coin, snake, obstacle);
    }

    private void powerCoin() {
        animation.setRate(game.getSpeed());
        snake.ignoreBorders(false);
        if (coin.getClass().equals(RainbowCoin.class)) {
            RainbowCoin tempCoin = (RainbowCoin) coin;
            animation.setRate(game.getSpeed() + tempCoin.getSpeedBonus());
        } else if (coin.getClass().equals(SilverCoin.class)){
            Random rnd = new Random();
            int added = obstacle.newObstacles(snake, coin, rnd.nextInt(game.getWidth() * game.getHeight()/ 20) +3);
            game.decMaxScore(added);
        } else if (coin.getClass().equals(BlueCoin.class)) {
            snake.ignoreBorders(true);
        }
        game.addPoints(coin.getPoints());
    }

    private void newCoin() {
        CoinType type = coinFactory.nextType(game.isPowerUp(), !obstacle.getObstacles().isEmpty());
        coin = coinFactory.create(type, game.getWidth(), game.getHeight(), snake, obstacle);
    }
}
