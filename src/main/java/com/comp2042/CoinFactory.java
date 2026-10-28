package com.comp2042;

import java.util.Random;

/**
 * Decides which kind of coin appears next and creates it.
 * <p>
 * With power-ups on, a roll from 0 to 11 picks the coin: 1 gold, 2 rainbow, 3 blue,
 * 4 silver (only while the board has no obstacles) and anything else a plain red coin.
 */
public class CoinFactory {

    private static final int ROLL_RANGE = 12;

    private final Random random;

    /** Creates a factory that rolls with a new {@link Random}. */
    public CoinFactory() {
        this(new Random());
    }

    /**
     * Creates a factory that rolls with the given random source, so that tests can fix the seed.
     *
     * @param random the source of the rolls
     */
    public CoinFactory(Random random) {
        this.random = random;
    }

    /**
     * Rolls the type of the next coin.
     *
     * @param powerUps         whether special coins are enabled for this game
     * @param obstaclesOnBoard whether the board already has obstacles
     * @return the type of the next coin; always {@link CoinType#RED} without power-ups
     */
    public CoinType nextType(boolean powerUps, boolean obstaclesOnBoard) {
        if (!powerUps) {
            return CoinType.RED;
        }
        return typeForRoll(random.nextInt(ROLL_RANGE), obstaclesOnBoard);
    }

    /**
     * Maps one roll to a coin type.
     *
     * @param roll             a number from 0 to 11
     * @param obstaclesOnBoard whether the board already has obstacles, which rules out silver
     * @return the coin type for that roll
     */
    static CoinType typeForRoll(int roll, boolean obstaclesOnBoard) {
        return switch (roll) {
            case 1 -> CoinType.GOLD;
            case 2 -> CoinType.RAINBOW;
            case 3 -> CoinType.BLUE;
            case 4 -> obstaclesOnBoard ? CoinType.RED : CoinType.SILVER;
            default -> CoinType.RED;
        };
    }

    /**
     * Creates a coin of the given type on a random free cell.
     *
     * @param type     the kind of coin to create
     * @param width    the board width in cells
     * @param height   the board height in cells
     * @param snake    the snake, whose cells are not free
     * @param obstacle the obstacles, whose cells are not free
     * @return the new coin
     */
    public Coin create(CoinType type, int width, int height, Snake snake, Obstacle obstacle) {
        return switch (type) {
            case RED -> new Coin(width, height, snake, obstacle);
            case GOLD -> new GoldCoin(width, height, snake, obstacle);
            case SILVER -> new SilverCoin(width, height, snake, obstacle);
            case BLUE -> new BlueCoin(width, height, snake, obstacle);
            case RAINBOW -> new RainbowCoin(width, height, snake, obstacle);
        };
    }
}
