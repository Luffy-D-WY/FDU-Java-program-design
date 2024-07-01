package com.example.lab9;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Game {
    private final String destination;
    private final Creature hobbit;
    private final Creature monster;
    private int checkpoint;
    private int finalCheckpoint;
    private List<Level> levels;
    private Scanner scanner;
    private Shop shop;

    public Game(String destination, Creature hobbit, Creature monster) {
        this.destination = destination;
        this.hobbit = hobbit;
        this.monster = monster;
        this.checkpoint = 1;
        this.levels = new ArrayList<>();
        this.shop=new Shop();
    }

    public void initializeLevels(int totalLevels) {
        finalCheckpoint = totalLevels;
        int rewardLevelsCount = totalLevels / 3;
        List<Integer> rewardLevels = new ArrayList<>();
        Random random = new Random();
        while (rewardLevels.size() < rewardLevelsCount) {
            int levelNum = random.nextInt(totalLevels) + 1;
            if (!rewardLevels.contains(levelNum)) {
                rewardLevels.add(levelNum);
            }
        }
        for (int i = 1; i <= totalLevels; i++) {
            if (rewardLevels.contains(i)) {
                levels.add(new RewardLevel(i));
            } else {
                levels.add(new NormalLevel(i));
            }
        }
    }
    public int play()
    {
        int status=levels.get(checkpoint-1).play(hobbit,monster);
        return status;
    }
    public int nextLevel() {
        checkpoint++;
        if (checkpoint > finalCheckpoint) {
            return 1;
        }

        return 0;
    }

    public int computeTotalMoney() {
        int totalMoney = 0;
        for (Level level : levels) {
            if (!(level instanceof RewardLevel)) {
                if (level.getCoin().isReal()) {
                    totalMoney++;
                }
            } else {
                totalMoney += 4;
            }
        }
        return totalMoney;
    }

    public String getDestination() {
        return destination;
    }

    public Creature getHobbit() {
        return hobbit;
    }

    public Creature getMonster() {
        return monster;
    }

    public int getCheckpoint() {
        return checkpoint;
    }

    public int getFinalCheckpoint() {
        return finalCheckpoint;
    }
    public List<Level> getLevels()
    {
        return levels;
    }



    public void visitShop() {
        Stage shopStage = new Stage();
        shopStage.setTitle("商店系统");

        TableView<Item> tableView = new TableView<>();
        tableView.setEditable(false);

        TableColumn<Item, String> nameColumn = new TableColumn<>("道具名称");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));



        TableColumn<Item, Integer> priceColumn = new TableColumn<>("道具价格");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Item, String> effectColumn = new TableColumn<>("道具作用");
        effectColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Item, Void> actionColumn = new TableColumn<>("购买道具的按钮");
        actionColumn.setCellFactory(col -> {
            TableCell<Item, Void> cell = new TableCell<>() {
                private final Button buyButton = new Button("购买");

                {
                    buyButton.setOnAction(e -> {
                        Item item = getTableView().getItems().get(getIndex());
                        shop.purchaseItem(item.getId(), hobbit);
                        shopStage.close();
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(buyButton);
                    }
                }
            };
            return cell;
        });

        ObservableList<Item> items = FXCollections.observableArrayList(shop.getItems());
        tableView.setItems(items);
        tableView.getColumns().addAll(nameColumn, priceColumn,effectColumn,  actionColumn);

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.getChildren().addAll(new Label("商店系统"), tableView);

        Scene scene = new Scene(layout, 600, 300);
        shopStage.setScene(scene);
        shopStage.showAndWait();
    }
    public void displayResult() {
        System.out.println("游戏结束！");

        // 显示目的地名称
        System.out.println("目的地名称：" + destination);

        // 判断胜利方
        String winner = (hobbit.getLife() > 0 && checkpoint == finalCheckpoint) ? "冒险者" : "对手";
        System.out.println("胜利方：" + winner);

        int totalMoney = computeTotalMoney();
        // 显示冒险者评分
        int totalHobbitMoney = hobbit.getTotalValueOfRealCoins();

        double hobbitScore = (double) totalHobbitMoney / totalMoney * 100;
        System.out.println("冒险者评分：" + hobbitScore + "%");
    }
}
