import java.util.Random;

public class NormalLevel extends Level {

    public NormalLevel(int levelNumber) {
        super(levelNumber);
    }

    @Override
    public int play(Creature adventurer, Creature opponent) {
        Coin coin = getCoin();
        Hobbit hobbit = new Hobbit();

            // 决定对手是否战斗
            if(adventurer instanceof Hobbit)
                hobbit=(Hobbit)adventurer;
            boolean opponentFights = opponent.isForMoney(adventurer);
            if (opponentFights && adventurer.getLife() >= 2) {
                double winProbability = opponent.getWinProbability(adventurer);
                boolean opponentWins = new Random().nextDouble() < winProbability;
                if (opponentWins) {
                    if(!hobbit.Shanbi) {
                        adventurer.setLife(adventurer.getLife() - 2);
                        System.out.println("对手赢得了战斗! 冒险者生命值减少2.");
                    }
                    else System.out.println("对手赢得了战斗！冒险者由于闪避药水生命值不变");
                    opponent.addCoin(coin);


                } else {
                    if(!hobbit.Baoji) {
                        opponent.setLife(opponent.getLife() - 2);
                        System.out.println("冒险者赢得了战斗! 对手生命值减少2.");
                    }
                    else {
                        opponent.setLife(opponent.getLife()-3);
                        System.out.println("冒险者赢得了战斗！由于暴击药水对手生命值减少3");
                    }
                    adventurer.addCoin(coin);

                }
            }
            else if(adventurer.getLife()<2&&(opponent instanceof Dwarf)&&!opponentFights) {
                adventurer.addCoin(coin);
                System.out.println("冒险者没有遇到战斗，矮人逃窜，获得金币");

            }
            else if(adventurer.getLife()<2)
            {
                opponent.addCoin(coin);
                System.out.println("冒险者血量过低，不发生战斗，金币由对手拿走");
            }

            // 在普通关卡结束后，增加生命值
            if(adventurer.getLife()>2)
                adventurer.setLife(adventurer.getLife() + 1);
            else {
                System.out.println("冒险者伤重，生命值减少1点");
                adventurer.setLife(adventurer.getLife() - 1);
                if(adventurer.getLife()==0) {
                    System.out.println("冒险者生命值归零，游戏失败");
                    return 1;
                }
                if(adventurer.getLife()==1)
                {
                    return 2;
                }
            }
        if(opponent.getLife()>0)
        {
            opponent.setLife(opponent.getLife() + 1);
        }
        else{
            opponent.setLife(0);
            System.out.println("对手生命值已降为0");

        }

        hobbit.Baoji=false;
        hobbit.Shanbi=false;

        System.out.println("本关卡结果:");
        System.out.println("普通关卡 " + getLevelNumber() + " 完成: 一个金币，面额: " + coin.getDenomination() + "，真假: " + (coin.isReal() ? "真" : "假") + "，生命值增加1点");
        System.out.println();

        System.out.println("冒险者的金币数:"+adventurer.getTotalValueOfRealCoins());
        System.out.println("生命值: " + adventurer.getLife());
        System.out.println("对手的金币数:"+opponent.getTotalValueOfRealCoins());
        System.out.println("生命值："+opponent.getLife());
        return 0;

    }
}
