import java.util.ArrayList;
import java.util.List;

public abstract class Creature {
    private int life;
    private List<Coin> coins;
    private List<Item> backpack;

    public Creature(int initialLife, List<Coin> initialCoins) {
        this.life = initialLife;
        this.coins = initialCoins != null ? new ArrayList<>(initialCoins) : new ArrayList<>();
        this.backpack=new ArrayList<>();
    }

    public Boolean isDead() {
        return life == 0;
    }

    public abstract boolean isForMoney(Creature adventurer);

    public abstract double getWinProbability(Creature adventurer);

    public void setLife(int newLife) {
        this.life = newLife;
    }

    public int getLife() {
        return life;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public void addCoin(Coin coin) {
        coins.add(coin);
    }

    public boolean removeCoin(Coin coin) {
        return coins.remove(coin);
    }
    public void addItemToBackpack(Item item) {
        this.backpack.add(item);
    }
    public void useItemFromBackpack(int itemId) {
        for (Item item : backpack) {
            if (item.getId() == itemId) {
                // 使用道具的逻辑
                if (item.getId() == 1) {
                    this.setLife(this.getLife() + 2);
                    System.out.println("使用生命药剂，生命值增加2点");
                } else {
                    Hobbit hobbit = (Hobbit) this;
                    if (item.getId() == 2) {
                        hobbit.Baoji = true;
                        System.out.println("使用暴击药剂，下一关卡战斗中暴击效果生效");
                    } else if (item.getId() == 3) {
                        hobbit.Shanbi = true;
                        System.out.println("使用闪避符咒，下一关卡战斗中闪避效果生效");
                    }
                }
                backpack.remove(item);
                return;
            }
        }
        System.out.println("背包中没有找到该道具！");
    }

    public void displayBackpack() {
        System.out.println("当前背包中的道具:");
        for (Item item : backpack) {
            System.out.println(item);
        }
    }


    public int getTotalValueOfRealCoins() {
        int totalValue = 0;
        for (Coin coin : coins) {
            if (coin.isReal()) {
                totalValue += coin.getValue();
            }
        }
        return totalValue;
    }

    public void removeCoinsForPurchase(int amount) {
        int remainingAmount = amount;
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            if (coin.isReal()) {
                if (coin.getValue() <= remainingAmount) {
                    remainingAmount -= coin.getValue();
                    coin.setReal(false); // 将真币标记为假币，表示已使用
                } else {
                    int change = coin.getValue() - remainingAmount;
                    coin.setDenomination(change); // 修改面额为找零后的值
                    remainingAmount = 0;
                }
            }
            if (remainingAmount == 0) {
                break;
            }
        }
    }

    public void addCoins(List<Coin> newCoins) {
        coins.addAll(newCoins);
    }

    // 显示金币信息
    public void displayCoins() {
        System.out.println("当前金币列表:");
        for (Coin coin : coins) {
            System.out.println(coin);
        }
    }
}
