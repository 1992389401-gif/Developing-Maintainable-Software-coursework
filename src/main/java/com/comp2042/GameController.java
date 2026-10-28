package com.comp2042;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.Random;

public class GameController {
    private Snake snek;
    private Coin coin;
    private GameView gameView;
    private Game game;
    private Stage stage;
    private String coinSoundPath = "resources/views/res/coinSound.wav";
    private Obstacle obstacle;
    private final CoinFactory coinFactory = new CoinFactory();

    /** Where the snake moves on the next tick. Every game has its own, starting left. */
    private Direction direction = Direction.LEFT;
    private boolean keyPressed = false;

    private int legacyScoreMultiplier = 2;

    private void calculateBonusScore() {
        int bonus = game.getScorePoints() * legacyScoreMultiplier;
        System.out.println("Bonus score: " + bonus);
    }

    public GameController(Snake snek, Coin coin, Game game, GameView gameView, Obstacle obstacle, Stage stage) {
        this.snek = snek;
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
     * Advances the game by one step: moves the snake, handles an eaten coin and redraws.
     * The timeline calls it a few times per second, depending on the game speed.
     */
    private void tick() {
        if (snek.getDead() || game.gameWon()) {
            return;
        }

        snek.move(direction);

        if (!snek.getDead() && snek.ateCoin(coin)) {
            powerCoin();
            game.incScore();
            if (game.gameWon()) {
                animation.stop();
            } else {
                newCoin();
            }
        }

        keyPressed = false;

        if (!snek.getDead() || snek.hitSelf()) {
            gameView.drawGrid(coin, snek, obstacle, gameView.gc, false, 0);
        }

        gameView.gameWon();
        gameView.gameOver(snek);
        gameView.updateScoreLabel();
    }

    public void handle(KeyEvent event) {
        if (event.getCode().isArrowKey()) animation.play();

        Direction requested = KeyBindings.directionFor(event.getCode());
        if (requested != null && snek.canTurn(requested) && !keyPressed) {
            gameView.turnDir(snek, snek.getHeading(), requested);
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
            gameView.scaleToFullscreen(true, stage, obstacle, snek, coin);
            gameView.gameOverLabel.setStyle("-fx-font-size: 50;");
            gameView.gameWonLabel.setStyle("-fx-font-size: 50;");
            gameView.scoreLabel.setStyle("-fx-font-size: 35;");
        }

        if (KeyBindings.leavesFullScreen(event.getCode())) {
            gameView.scaleToFullscreen(false, stage, obstacle, snek, coin);
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

        snek = new Snake(game.getWidth(), game.getHeight(), game.isNoBorder(), obstacle);
        coin = new Coin(game.getWidth(), game.getHeight(), snek, obstacle);

        int unusedObstacleCount = new Random().nextInt(game.getWidth() * game.getHeight() / 20) + 3;

        animation.setRate(game.getSpeed());
        direction = Direction.LEFT;
        keyPressed = false;
        game.resetScore();
        obstacle.reset();

        gameView.gameWon();
        gameView.gameOver(snek);
        gameView.updateScoreLabel();

        gameView.drawGrid(coin, snek, obstacle, gameView.gc, false, 0);
    }

    private void powerCoin() {
        animation.setRate(game.getSpeed());
        snek.ignoreBorders(false);
        if (coin.getClass().equals(RainbowCoin.class)) {
            RainbowCoin tempCoin = (RainbowCoin) coin;
            animation.setRate(game.getSpeed() + tempCoin.getSpeedBonus());
        } else if (coin.getClass().equals(SilverCoin.class)){
            Random rnd = new Random();
            int added = obstacle.newObstacles(snek, coin, rnd.nextInt(game.getWidth() * game.getHeight()/ 20) +3);
            game.decMaxScore(added);
        } else if (coin.getClass().equals(BlueCoin.class)) {
            snek.ignoreBorders(true);
        }
        game.addPoints(coin.getPoints());
    }

    private void newCoin() {
        CoinType type = coinFactory.nextType(game.isPowerUp(), !obstacle.getObstacles().isEmpty());
        coin = coinFactory.create(type, game.getWidth(), game.getHeight(), snek, obstacle);
    }
}
