package com.comp2042;

public class RainbowCoin extends Coin {

    public RainbowCoin(int x, int y, Snake snake, Obstacle obstacle) {
        super(x, y, snake, obstacle);
    }

    public int getPoints() {
        int points = 5;
        return points;
    }

    public double getSpeedBonus() {
        double speedBonus = 5;
        return speedBonus;
    }

    @Override
    public CoinType getType() {
        return CoinType.RAINBOW;
    }
}
