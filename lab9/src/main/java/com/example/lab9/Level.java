package com.example.lab9;
import java.util.Random;

public abstract class Level {
    private int levelNumber;
    private Coin coin;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.coin = generateCoin();
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public Coin getCoin() {
        return coin;
    }

    private Coin generateCoin() {
        Random random = new Random();
        boolean isReal = random.nextDouble() < 0.7; // 真金币的概率为 0.7
        int denomination = 1;
        return new Coin(denomination, isReal);
    }

    public abstract int  play(Creature adventurer, Creature opponent);
}
