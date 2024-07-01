package com.example.lab9;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
public class GameUI extends Application {
    private Stage primaryStage;
    private Game currentGame;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        StackPane root = new StackPane(); // 使用StackPane来堆叠背景和主要布局

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        primaryStage.setTitle("中世纪大冒险");

        // 添加背景图片
        Image backgroundImage = new Image("file:src/image/background.jpg"); // 确保背景图片路径正确
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setOpacity(0.8); // 设置透明度为50%
        backgroundView.setFitWidth(800); // 设置图片宽度覆盖整个窗口
        backgroundView.setFitHeight(600); // 设置图片高度覆盖整个窗口
        backgroundView.setPreserveRatio(false); // 不保持宽高比，以覆盖整个窗口

        Label welcomeLabel = new Label("欢迎来到Middle-Earth Adventure!");
        VBox.setMargin(welcomeLabel, new Insets(10));
        Label destinationLabel = new Label("请输入冒险的目的地名称（4至16个字母和空格，不能以空格开头或结尾）:");
        TextField destinationInput = new TextField();
        VBox.setMargin(destinationLabel, new Insets(10));
        Label levelLabel = new Label("请输入关卡数:");
        TextField levelInput = new TextField();
        VBox.setMargin(levelInput, new Insets(10));
        Button startButton = new Button("开始冒险");
        Button exitButton = new Button("退出游戏");

        startButton.setOnAction(e -> {
            String destination = destinationInput.getText();
            String levelText = levelInput.getText();
            if (isValidDestination(destination) && isValidLevelCount(levelText)) {
                int totalLevels = Integer.parseInt(levelText);
                startAdventure(destination, totalLevels);
            } else {
                showAlert("输入格式不正确，请重新输入。");
            }
        });
        HBox buttonLayout = new HBox(20);
        buttonLayout.setAlignment(Pos.CENTER);
        buttonLayout.getChildren().addAll(startButton, exitButton);

        exitButton.setOnAction(e -> primaryStage.close());

        // Add all nodes to the main layout
        mainLayout.getChildren().addAll(welcomeLabel, destinationLabel, destinationInput, levelLabel, levelInput, buttonLayout);

        // 添加背景图片和主要布局到根布局
        root.getChildren().addAll(backgroundView, mainLayout);

        Scene scene = new Scene(root, 800, 600); // 调整场景大小以适应背景图片
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void startAdventure(String destination, int totalLevels) {
        Creature hobbit = new Hobbit();
        Creature monster = getRandomMonster();
        currentGame = new Game(destination, hobbit, monster);
        currentGame.initializeLevels(totalLevels);

        showGameScreen();
    }
    private void showGameScreen() {
        StackPane root = new StackPane(); // 使用StackPane来堆叠背景和主要布局

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // 设置列约束
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        col2.setHalignment(HPos.CENTER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.ALWAYS);
        col3.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        // 设置行约束
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        row1.setValignment(VPos.CENTER);
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        row2.setValignment(VPos.CENTER);
        RowConstraints row3 = new RowConstraints();
        row3.setVgrow(Priority.ALWAYS);
        row3.setValignment(VPos.CENTER);
        RowConstraints row4 = new RowConstraints();
        row4.setVgrow(Priority.ALWAYS);
        row4.setValignment(VPos.CENTER);
        grid.getRowConstraints().addAll(row1, row2, row3, row4);

        Button adventurerStatus = new Button("冒险者状态");
        GridPane.setConstraints(adventurerStatus, 0, 0);

        Label levelLabel;
        if (currentGame.getLevels().get(currentGame.getCheckpoint() - 1) instanceof RewardLevel)
            levelLabel = new Label("关卡 " + currentGame.getCheckpoint() + "[奖励关卡]");
        else levelLabel = new Label("关卡 " + currentGame.getCheckpoint());
        GridPane.setConstraints(levelLabel, 1, 0);

        Button enemyStatus = new Button("敌人状态");
        GridPane.setConstraints(enemyStatus, 2, 0);

        ImageView adventurerLogo = new ImageView(new Image("file:src/image/saber.png"));
        adventurerLogo.setFitHeight(200);
        adventurerLogo.setFitWidth(200);
        GridPane.setConstraints(adventurerLogo, 0, 2);

        Button shopButton = new Button("查看商店");
        GridPane.setConstraints(shopButton, 1, 1);

        ImageView enemyLogo = new ImageView(new Image("file:src/image/berserker.jpg"));
        enemyLogo.setFitHeight(200);
        enemyLogo.setFitWidth(200);
        GridPane.setConstraints(enemyLogo, 2, 2);

        ImageView goldLabel;
        if (currentGame.getLevels().get(currentGame.getCheckpoint() - 1) instanceof RewardLevel)
            goldLabel = new ImageView(new Image("file:src/image/宝箱.png"));
        else goldLabel = new ImageView(new Image("file:src/image/金币.png"));
        goldLabel.setFitHeight(50);
        goldLabel.setFitWidth(50);
        GridPane.setConstraints(goldLabel, 1, 2);

        Button startBattleButton = new Button("开始战斗");
        GridPane.setConstraints(startBattleButton, 0, 4);

        Label destinationLabel = new Label("目的地:" + currentGame.getDestination() + ",余" + (currentGame.getFinalCheckpoint() - currentGame.getCheckpoint()) + "关");
        GridPane.setConstraints(destinationLabel, 1, 4);

        Button nextButton = new Button("下一关");
        GridPane.setConstraints(nextButton, 2, 4);

        // 添加所有控件到 GridPane
        grid.getChildren().addAll(adventurerStatus, levelLabel, enemyStatus, adventurerLogo, shopButton, enemyLogo, goldLabel, startBattleButton, destinationLabel, nextButton);

        // 添加背景图片
        Image backgroundImage = new Image("file:src/image/background1.jpeg"); // 确保背景图片路径正确
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setOpacity(0.8); // 设置透明度为50%
        backgroundView.setFitWidth(800); // 设置图片宽度覆盖整个窗口
        backgroundView.setFitHeight(400); // 设置图片高度覆盖整个窗口
        backgroundView.setPreserveRatio(false); // 不保持宽高比，以覆盖整个窗口

        // 添加背景图片和主要布局到根布局
        root.getChildren().addAll(backgroundView, grid);

        Scene scene = new Scene(root, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        shopButton.setOnAction(e -> {
            currentGame.visitShop();
        });
        adventurerStatus.setOnAction(e -> {
            currentGame.getHobbit().displayStatus();
        });
        enemyStatus.setOnAction(e -> {
            currentGame.getMonster().displayStatus();
        });

        if (currentGame.getLevels().get(currentGame.getCheckpoint() - 1) instanceof NormalLevel) {//普通关卡
            // 初始状态设置
            startBattleButton.setDisable(false);
            nextButton.setDisable(true);

            // 绑定按钮事件
            startBattleButton.setOnAction(e -> {
                startBattleButton.setDisable(true);  // 禁用开始战斗按钮
                int status = currentGame.play();
                if (status == 1 || currentGame.getHobbit().getLife() <= 0 || currentGame.getCheckpoint() == currentGame.getFinalCheckpoint()) {
                    showResult();
                } else {
                    nextButton.setDisable(false);  // 启用下一关按钮
                }
                if (!currentGame.getLevels().get(currentGame.getCheckpoint() - 1).getCoin().isReal()) // 当前关卡为假币
                {
                    goldLabel.setOpacity(0.3);
                }
            });
            nextButton.setOnAction(e -> {
                int status = currentGame.nextLevel();
                if (status == 1) {
                    showResult();
                } else {
                    showGameScreen();
                }
            });
        }
        else {//奖励关卡
            startBattleButton.setDisable(true);
            nextButton.setDisable(false);
            currentGame.play();
            nextButton.setOnAction(e -> {
                int status = currentGame.nextLevel();
                if (status == 1) {
                    showResult();
                } else {
                    showGameScreen();
                }
            });
        }
    }


    private void showResult() {
        Stage resultStage = new Stage();
        resultStage.setTitle("游戏结果");

        StackPane root = new StackPane(); // 使用StackPane来堆叠背景和主要布局

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // 设置列约束
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.ALWAYS);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        col2.setHalignment(HPos.CENTER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setHgrow(Priority.ALWAYS);
        col3.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().addAll(col1, col2, col3);

        // 设置行约束
        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        row1.setValignment(VPos.CENTER);
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        row2.setValignment(VPos.CENTER);
        RowConstraints row3 = new RowConstraints();
        row3.setVgrow(Priority.ALWAYS);
        row3.setValignment(VPos.CENTER);
        grid.getRowConstraints().addAll(row1, row2, row3);

        ImageView adventurerLogo = new ImageView(new Image("file:src/image/saber.png"));
        adventurerLogo.setFitHeight(100);
        adventurerLogo.setFitWidth(100);
        GridPane.setConstraints(adventurerLogo, 0, 0);

        String destination = currentGame.getDestination();
        String winner = (currentGame.getHobbit().getLife() > 0 && currentGame.getCheckpoint() == currentGame.getFinalCheckpoint()) ? "冒险者" : "对手";
        String resultText = String.format("游戏结束！\n目的地名称：%s\n胜利方：%s\n", destination, winner);

        ImageView enemyLogo = new ImageView(new Image("file:src/image/berserker.jpg"));
        enemyLogo.setFitHeight(100);
        enemyLogo.setFitWidth(100);
        GridPane.setConstraints(enemyLogo, 2, 0);

        // 游戏结果标签
        Label resultLabel = new Label(resultText);
        GridPane.setConstraints(resultLabel, 1, 0);

        // 冒险者最终状态按钮
        Button adventurerStatusButton = new Button("冒险者最终状态");
        adventurerStatusButton.setOnAction(e -> currentGame.getHobbit().displayStatus());
        GridPane.setConstraints(adventurerStatusButton, 0, 1);

        // 游戏评分按钮
        int totalMoney = currentGame.computeTotalMoney();
        int totalHobbitMoney = currentGame.getHobbit().getTotalValueOfRealCoins();
        double hobbitScore = (double) totalHobbitMoney / totalMoney * 100;
        String formattedScore = String.format("%.3f", hobbitScore);
        Label scoreLabel = new Label("冒险者评分：" + formattedScore + "%");

        GridPane.setConstraints(scoreLabel, 1, 1);

        // 敌人最终状态按钮
        Button enemyStatusButton = new Button("敌人最终状态");
        enemyStatusButton.setOnAction(e -> currentGame.getMonster().displayStatus());
        GridPane.setConstraints(enemyStatusButton, 2, 1);

        // 退出游戏按钮
        Button exitButton = new Button("退出游戏");
        exitButton.setOnAction(e -> {
            resultStage.close();
            primaryStage.close();
                }
        );
        GridPane.setConstraints(exitButton, 1, 2);

        // 添加所有控件到 GridPane
        grid.getChildren().addAll(adventurerLogo, enemyLogo, resultLabel, adventurerStatusButton, scoreLabel, enemyStatusButton, exitButton);

        // 添加背景图片
        Image backgroundImage = new Image("file:src/image/background3.jpg"); // 确保背景图片路径正确
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setOpacity(0.8); // 设置透明度为50%
        backgroundView.setFitWidth(400); // 设置图片宽度覆盖整个窗口
        backgroundView.setFitHeight(300); // 设置图片高度覆盖整个窗口
        backgroundView.setPreserveRatio(false); // 不保持宽高比，以覆盖整个窗口

        // 添加背景图片和主要布局到根布局
        root.getChildren().addAll(backgroundView, grid);

        Scene scene = new Scene(root, 400, 300);
        resultStage.setScene(scene);
        resultStage.show();
    }


    private boolean isValidDestination(String destination) {
        return destination.matches("^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$") && destination.length() >= 4 && destination.length() <= 16;
    }

    private boolean isValidLevelCount(String levelText) {
        try {
            int totalLevels = Integer.parseInt(levelText);
            return totalLevels > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Creature getRandomMonster() {
        int randomIndex = (int) (Math.random() * 3);
        switch (randomIndex) {
            case 0:
                return new Dwarf(9, null); // 生命值为9
            case 1:
                return new Elf(7, null); // 生命值为7
            default:
                return new Orc(8, null); // 生命值为8
        }
    }
}
