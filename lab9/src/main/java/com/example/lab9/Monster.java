package com.example.lab9;
import java.util.List;
import java.util.List;
class Elf extends Creature {
    public Elf(int initialLife, List<Coin> initialCoins) {
        super(initialLife, initialCoins);
    }

    @Override
    public boolean isForMoney(Creature adventurer) {
        return getLife()>0; // Elf always fights for money unless adventurer's life < 2
    }

    @Override
    public double getWinProbability(Creature adventurer) {
        return 0.8;
    }
}



class Dwarf extends Creature {
    public Dwarf(int initialLife, List<Coin> initialCoins) {
        super(initialLife, initialCoins);
    }

    @Override
    public boolean isForMoney(Creature adventurer) {
        return this.getLife() > 2 ;
    }

    @Override
    public double getWinProbability(Creature adventurer) {
        return 0.5;
    }
}

class Orc extends Creature {
    public Orc(int initialLife, List<Coin> initialCoins) {
        super(initialLife, initialCoins);
    }

    @Override
    public boolean isForMoney(Creature adventurer) {
        return getLife()>0; // Orc always fights for money unless adventurer's life < 2
    }

    @Override
    public double getWinProbability(Creature adventurer) {
        if (this.getLife() > adventurer.getLife()) {
            return 0.6;
        } else if (this.getLife() < adventurer.getLife()) {
            return 0.3;
        } else {
            return 0.4;
        }
    }
}
