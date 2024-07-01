package com.example.lab9;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class NormalLevel extends Level {

    public NormalLevel(int levelNumber) {
        super(levelNumber);
    }

    @Override
    public int play(Creature adventurer, Creature opponent) {
        Coin coin = getCoin();
        Hobbit hobbit = new Hobbit();

        VBox resultBox = new VBox(10);
        resultBox.setPadding(new javafx.geometry.Insets(10));

        Label resultLabel = new Label();
        resultBox.getChildren().add(resultLabel);

        if (adventurer instanceof Hobbit)
            hobbit = (Hobbit) adventurer;
        boolean opponentFights = opponent.isForMoney(adventurer);
        if (opponentFights && adventurer.getLife() >= 2) {
            double winProbability = opponent.getWinProbability(adventurer);
            boolean opponentWins = new Random().nextDouble() < winProbability;
            if (opponentWins) {
                if (!hobbit.Shanbi) {
                    adventurer.setLife(adventurer.getLife() - 2);
                    resultLabel.setText("对手赢得了战斗! 冒险者生命值减少2.");
                } else {
                    resultLabel.setText("对手赢得了战斗！冒险者由于闪避药水生命值不变");
                }
                opponent.addCoin(coin);
            } else {
                if (!hobbit.Baoji) {
                    opponent.setLife(opponent.getLife() - 2);
                    resultLabel.setText("冒险者赢得了战斗! 对手生命值减少2.");
                } else {
                    opponent.setLife(opponent.getLife() - 3);
                    resultLabel.setText("冒险者赢得了战斗！由于暴击药水对手生命值减少3");
                }
                adventurer.addCoin(coin);
            }
        } else if (adventurer.getLife() < 2 && (opponent instanceof Dwarf) && !opponentFights) {
            adventurer.addCoin(coin);
            resultLabel.setText("冒险者没有遇到战斗，矮人逃窜，获得金币");
        } else if (adventurer.getLife() < 2) {
            opponent.addCoin(coin);
            resultLabel.setText("冒险者血量过低，不发生战斗，金币由对手拿走");
        }

        if (adventurer.getLife() > 2) {
            adventurer.setLife(adventurer.getLife() + 1);
        } else {
            resultLabel.setText(resultLabel.getText() + "\n冒险者伤重，生命值减少1点");
            adventurer.setLife(adventurer.getLife() - 1);
            if (adventurer.getLife() == 0) {
                resultLabel.setText(resultLabel.getText() + "\n冒险者生命值归零，游戏失败");
                showResultWindow(resultBox);
                return 1;
            }
            if (adventurer.getLife() == 1) {
                showResultWindow(resultBox);
                return 2;
            }
        }

        if (opponent.getLife() > 0) {
            opponent.setLife(opponent.getLife() + 1);
        } else {
            opponent.setLife(0);
            resultLabel.setText(resultLabel.getText() + "\n对手生命值已降为0");
        }

        hobbit.Baoji = false;
        hobbit.Shanbi = false;

        String resultText = "本关卡结果:\n";
        resultText += "普通关卡 " + getLevelNumber() + " 完成: 一个金币，面额: " + coin.getDenomination() + "，真假: " + (coin.isReal() ? "真" : "假") + "，生命值增加1点\n";
        resultText += "冒险者的金币数: " + adventurer.getTotalValueOfRealCoins() + "\n";
        resultText += "生命值: " + adventurer.getLife() + "\n";
        resultText += "对手的金币数: " + opponent.getTotalValueOfRealCoins() + "\n";
        resultText += "生命值: " + opponent.getLife() + "\n";

        resultLabel.setText(resultLabel.getText() + "\n" + resultText);

        showResultWindow(resultBox);
        return 0;
    }

    private void showResultWindow(VBox resultBox) {
        Platform.runLater(() -> {
            Stage resultStage = new Stage();
            Scene resultScene = new Scene(resultBox, 400, 300);
            resultStage.setScene(resultScene);
            resultStage.setTitle("战斗结果");
            resultStage.show();
        });
    }
}
