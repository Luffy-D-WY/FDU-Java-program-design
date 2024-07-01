package com.example.lab9;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
                    coin.setReal(false);
                } else {
                    int change = coin.getValue() - remainingAmount;
                    coin.setDenomination(change);
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
    public void displayStatus() {
        Stage statusStage = new Stage();
        statusStage.setTitle("角色状态");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // 生命值
        HBox lifeBox = new HBox(5);
        int life = getLife();
        for (int i = 0; i < life; i++) {
            ImageView heart;
            if(this instanceof Hobbit)
                heart = new ImageView(new Image("file:src/image/heart.png"));
            else heart=new ImageView(new Image("file:src/image/blackheart.png"));
            heart.setFitHeight(30);
            heart.setFitWidth(30);
            lifeBox.getChildren().add(heart);
        }
        grid.add(lifeBox, 0, 0, 3, 1);

        // 金币
        HBox coinBox = new HBox(5);
        List<Coin> coins = getCoins();
        coins.sort((a, b) -> {
            if (a.getValue() != b.getValue()) {
                return Integer.compare(b.getValue(), a.getValue());
            }
            return Boolean.compare(b.isReal(), a.isReal());
        });
        for (Coin coin : coins) {
            ImageView coinImage;
            if (coin.getValue() == 2) {
                coinImage = new ImageView("file:src/image/money.png");
                coinImage.setFitHeight(50);
                coinImage.setFitWidth(50);
            } else {
                coinImage = new ImageView("file:src/image/money.png");
                coinImage.setFitHeight(20);
                coinImage.setFitWidth(20);
            }
            if (!coin.isReal()) {
                coinImage.setOpacity(0.5);
            }
            coinBox.getChildren().add(coinImage);
        }
        grid.add(coinBox, 0, 1, 3, 1);

        // 道具槽位
        Button slot1 = new Button("道具1槽位");
        slot1.setOnAction(e -> showItemDetails(1));
        Button slot2 = new Button("道具2槽位");
        slot2.setOnAction(e -> showItemDetails(2));
        Button slot3 = new Button("道具3槽位");
        slot3.setOnAction(e -> showItemDetails(3));

        grid.add(slot1, 0, 2);
        grid.add(slot2, 1, 2);
        grid.add(slot3, 2, 2);

        Scene scene = new Scene(grid, 400, 300);
        statusStage.setScene(scene);
        statusStage.showAndWait();
    }

    private void showItemDetails(int slot) {
        Item item;
        if (slot <= backpack.size()) {
            item = backpack.get(slot - 1);
        } else {
            item = null;
        }

        if (item == null) {
            showAlert("错误", "背包中没有找到该道具！");
            return;
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("道具详情");

        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10, 10, 10, 10));
        dialogVBox.setAlignment(Pos.CENTER);

        Label detailsLabel = new Label(item.toString());
        Button useButton = new Button("使用");
        useButton.setOnAction(e -> {
            int id = item.getId();
            useItemFromBackpack(id);
            dialog.close();
        });
        Button cancelButton = new Button("取消");
        cancelButton.setOnAction(e -> dialog.close());

        dialogVBox.getChildren().addAll(detailsLabel, useButton, cancelButton);

        Scene dialogScene = new Scene(dialogVBox, 500, 150);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
