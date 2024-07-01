package com.example.lab9;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        if (adventurer instanceof Hobbit hobbit) {
            hobbit.Baoji = false;
            hobbit.Shanbi = false;
        }

        VBox resultBox = new VBox(10);
        resultBox.setPadding(new javafx.geometry.Insets(10));

        Label resultLabel = new Label();
        resultBox.getChildren().add(resultLabel);

        StringBuilder resultText = new StringBuilder("本关卡结果:\n");

        if (opponent.getLife() > 0) {
            opponent.setLife(opponent.getLife() + 2);
            adventurer.addCoin(coin1);
            opponent.addCoin(coin2);
            resultText.append("奖励关卡\n").append(getLevelNumber()).append(" 完成: 冒险者和对手各获得一个面额为 2 的真金币\n每人生命值增加2点\n");
        } else {
            resultText.append("对手生命值已降为零\n");
            adventurer.addCoin(coin1);
            resultText.append("奖励关卡 ").append(getLevelNumber()).append(" 完成: 冒险者获得一个面额为 2 的真金币，生命值增加2点\n");
        }

        resultText.append("\n冒险者的金币数: ").append(adventurer.getTotalValueOfRealCoins());
        resultText.append("\n生命值: ").append(adventurer.getLife());
        resultText.append("\n对手的金币数: ").append(opponent.getTotalValueOfRealCoins());
        resultText.append("\n生命值: ").append(opponent.getLife());

        resultLabel.setText(resultText.toString());

        showResultWindow(resultBox);

        return 0;
    }

    private void showResultWindow(VBox resultBox) {
        Platform.runLater(() -> {
            Stage resultStage = new Stage();
            Scene resultScene = new Scene(resultBox, 400, 300);
            resultStage.setScene(resultScene);
            resultStage.setTitle("奖励关卡结果");
            resultStage.show();
        });
    }
}
