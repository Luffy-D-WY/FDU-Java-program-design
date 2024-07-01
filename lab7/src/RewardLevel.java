public class RewardLevel extends Level {

    public RewardLevel(int levelNumber) {
        super(levelNumber);
    }

    @Override
    public int play(Creature adventurer, Creature opponent) {
        Coin coin1 = new Coin(2, true);
        Coin coin2 = new Coin(2, true);



        // 增加冒险者和对手的生命值
        adventurer.setLife(adventurer.getLife() + 2);

        if(adventurer instanceof Hobbit hobbit)
        {
            hobbit.Baoji=false;
            hobbit.Shanbi=false;
        }
        System.out.println("本关卡结果:");
        if(opponent.getLife()>0) {
            opponent.setLife(opponent.getLife() + 2);
            adventurer.addCoin(coin1);
            opponent.addCoin(coin2);
            System.out.println("奖励关卡 " + getLevelNumber() + " 完成: 冒险者和对手各获得一个面额为 2 的真金币，每人生命值增加2点");
        }
        else{
            System.out.println("对手生命值已降为零");
            adventurer.addCoin(coin1);
            System.out.println("奖励关卡 " + getLevelNumber() + " 完成: 冒险者获得一个面额为 2 的真金币，生命值增加2点");
        }


        System.out.println();
        System.out.println("冒险者的金币数:"+adventurer.getTotalValueOfRealCoins());
        System.out.println("生命值: " + adventurer.getLife());
        System.out.println("对手的金币数:"+opponent.getTotalValueOfRealCoins());
        System.out.println("生命值："+opponent.getLife());
        return 0;
    }
}
