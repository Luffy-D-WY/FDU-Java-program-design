package com.example.lab9;
import java.util.ArrayList;

import java.util.List;
public class Shop {
    private List<Item> items;
    private List<Item> soldItems;

    // 构造函数，初始化道具列表
    public Shop() {
        items = new ArrayList<>();
        soldItems = new ArrayList<>();
        // 添加道具
        items.add(new Item(1, "生命药剂", 2, "恢复冒险者2点生命值"));
        items.add(new Item(2, "暴击药剂", 2, "如果冒险者购买后,下一关卡发生了战斗并获胜,可以让对手生命值减少3"));
        items.add(new Item(3, "闪避符咒", 3, "如果冒险者购买后,下一关卡发生了战斗并战败,可以免除生命值的减少"));
    }
    public List<Item>getItems()
    {
        return items;
    }

    // 展示可售卖的道具
    public void displayItems() {
        System.out.println("当前商店可售卖的道具:");
        int flag=0;
        for (Item item : items) {
            flag=1;
            System.out.println(item);
        }
        if(flag==0)
            System.out.println("当前商店道具已卖光");
    }

    // 购买道具的方法
    public boolean purchaseItem(int id, Creature adventurer) {
        for (Item item : items) {
            if (item.getId() == id) {
                if (canAfford(item, adventurer.getCoins())) {
                    soldItems.add(item);
                    items.remove(item);
                    deductCoins(item.getPrice(), adventurer.getCoins());
                    adventurer.addItemToBackpack(item);
                    System.out.println("成功购买道具: " + item.getName());
                    return true;
                } else {
                    System.out.println("金币不足，无法购买该道具！");
                    return false;
                }
            }
        }
        System.out.println("无效的道具ID！");
        return false;
    }

    // 检查是否有足够的真金币购买道具
    private boolean canAfford(Item item, List<Coin> coins) {
        int totalValue = 0;
        for (Coin coin : coins) {
            if (coin.isReal()) {
                totalValue += coin.getValue();
            }
        }
        return totalValue >= item.getPrice();
    }

    // 扣除购买道具所需的真金币
    private void deductCoins(int price, List<Coin> coins) {
        int remainingPrice = price;
        for (Coin coin : coins) {
            if (coin.isReal()) {
                if (coin.getValue() <= remainingPrice) {
                    remainingPrice -= coin.getValue();
                    coin.setReal(false); // 将真金币设为假币，表示已使用
                } else {
                    // 找零
                    int change = coin.getValue() - remainingPrice;
                    coin.setDenomination(change); // 修改面额为找零后的值
                    remainingPrice = 0;
                }
            }
            if (remainingPrice == 0) {
                break;
            }
        }
    }

}
