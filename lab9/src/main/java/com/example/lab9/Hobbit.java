package com.example.lab9;
import java.util.Random;

public class Hobbit extends Creature {
   public Boolean Baoji;
   public Boolean Shanbi;
    public Hobbit() {
        super(generateRandomLife(), null);
        Baoji=false;
        Shanbi=false;
    }
    public boolean isForMoney(Creature adventurer)
    {
        System.out.println("调用错误！！");
        return getLife()>=2;
    }

    @Override
    public double getWinProbability(Creature adventurer) {
        System.out.println("调用错误！！");
        return 0;
    }

    @Override
    public void setLife(int newLife) {

        super.setLife(newLife);
    }

    private static int generateRandomLife() {
        return new Random().nextInt(5) + 5; // 5-9的随机生命值
    }
}
