package com.comp2042;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link CoinFactory}.
 */
class CoinFactoryTest {

    @ParameterizedTest(name = "roll {0} -> {1}")
    @CsvSource({
            "0, RED",
            "1, GOLD",
            "2, RAINBOW",
            "3, BLUE",
            "4, SILVER",
            "5, RED",
            "11, RED"
    })
    void eachRollPicksACoinType(int roll, CoinType expected) {
        assertEquals(expected, CoinFactory.typeForRoll(roll, false));
    }

    /** Regression test for B2: the rainbow coin's branch repeated "ran == 1", so it never came up. */
    @Test
    void everyCoinTypeCanAppear() {
        Set<CoinType> seen = EnumSet.noneOf(CoinType.class);
        for (int roll = 0; roll < 12; roll++) {
            seen.add(CoinFactory.typeForRoll(roll, false));
        }

        assertEquals(EnumSet.allOf(CoinType.class), seen);
    }

    @Test
    void silverCoinsOnlyAppearWhileTheBoardHasNoObstacles() {
        assertEquals(CoinType.RED, CoinFactory.typeForRoll(4, true));
    }

    @Test
    void rollsFromTheGivenRandomSource() {
        CoinFactory factory = new CoinFactory(new Random(42));
        Set<CoinType> seen = EnumSet.noneOf(CoinType.class);
        for (int i = 0; i < 200; i++) {
            seen.add(factory.nextType(true, false));
        }

        assertEquals(EnumSet.allOf(CoinType.class), seen);
    }

    @Test
    void onlyRedCoinsWhenPowerUpsAreOff() {
        CoinFactory factory = new CoinFactory(new Random(42));

        for (int i = 0; i < 50; i++) {
            assertEquals(CoinType.RED, factory.nextType(false, false));
        }
    }

    @ParameterizedTest(name = "create {0}")
    @EnumSource(CoinType.class)
    void createsACoinOfTheRequestedType(CoinType type) {
        Obstacle obstacle = new Obstacle(20, 20);
        Snake snake = new Snake(20, 20, false, obstacle);

        Coin coin = new CoinFactory().create(type, 20, 20, snake, obstacle);

        assertEquals(type, coin.getType());
    }
}
