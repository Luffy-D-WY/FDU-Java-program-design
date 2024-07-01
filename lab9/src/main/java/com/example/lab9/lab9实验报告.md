# lab9实验报告
吴优 22307130158
实验环境：**idea-itellij**
## 实验内容
在前几次lab的基础上，对中世纪大冒险小游戏实现局部功能的添加，包括奖励关卡的重新布置，冒险者背包功能的实现，最后也是主要任务是对这个小游戏实现图形化界面。
## 代码结构
`Coin`类：游戏中的金币，包括面值和金币真假的属性
`Item`类：游戏中的道具，包括道具的名称、价格以及道具作用的描述。
`Creature`类：是小游戏中各个生物的父类，实现Monster和hobbit的共有行为。
`Monster`类：包括小游戏中的三种怪物，实现各个怪物特有的属性以及方法。
`Hobbit`类：指代冒险者，实现冒险者的相关属性以及方法。
`Level`类：奖励关卡和普通关卡的父类，实现共有的属性和功能。
`NormalLevel`类：实现普通关卡的相关事务，主要是战斗以及战斗结果展示的函数。
`RewardLevel`类：实现奖励关卡的相关事务，包括奖励关卡的奖励分发以及结果的展示。
`Shop`类：商店的相关功能。
`GameUI`类：游戏的主要窗口，调用`Game`类中的具体函数实现那功能。
`Game`类：组织大部分代码，实现游戏的主要逻辑。
## 奖励关卡的重新布置
功能较为简单，在`Game`类中的`initialLevels`函数中修改成随机初始化一系列的有编号的奖励关卡，同时限制奖励关卡的数量为三分之一然后再将普通关卡插入即可。
```java
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
```
## 背包功能的实现
由于背包是依附于生物出现，直接在`creature`中添加一个为`list<Item>backpack`的属性。
p.s. 至于为什么直接在`Hobbit`类中添加背包属性，我的考虑是在上次lab中关于商店系统的大多数操作是直接在`creature`类中实现的，便于直接调用。而且在后续的人物状态栏表示是直接复用于`creature`中的相关代码，放在`creature`中便于同时使用
由于在上次lab中买了物品是立刻使用，此次将“使用”这部分的代码改成添加到背包中即可。

## UI部分代码分析


### 欢迎界面

```java
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
```
   - `StackPane` 用于堆叠背景图像和主布局 (`VBox`)。可以叠放多个子节点，后添加的节点显示在前面。
   - `VBox` 是一个垂直布局容器，用于按垂直顺序排列节点。设置了内部填充 (`setPadding`) 和对齐方式 (`setAlignment`)。
   - 使用 `Image` 和 `ImageView` 添加背景图片。背景图片设置了透明度（`setOpacity(0.8)`）、宽度、高度和不保持宽高比（`setPreserveRatio(false)`），以覆盖整个窗口。
   - 创建了多个 `Label`、`TextField` 和 `Button` 控件，提示用户输入冒险目的地和关卡数。设置了这些控件的布局和间距 (`VBox.setMargin`)。
   - **开始冒险按钮**:
     - 点击事件 (`setOnAction`) 中，检查输入的目的地和关卡数,如果有效，开始冒险 ，否则显示警告 (`showAlert`)。
   - **退出按钮**:
     - 点击事件中，关闭主窗口 (`primaryStage.close()`)。
   - 使用 `HBox` 布局按钮，水平排列并居中对齐。将所有控件添加到 `VBox` 主布局中。将背景图像和主布局添加到 `StackPane` 根布局中。


主要功能：
1. 显示一个欢迎屏幕，包含背景图像。
2. 用户可以输入冒险目的地和关卡数。
3. 用户可以点击“开始冒险”按钮以启动冒险，如果输入无效则提示错误。
4. 用户可以点击“退出游戏”按钮以关闭程序。

### 游戏闯关界面
```java
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
```
分析：
   - 使用 `StackPane` 堆叠背景图像和主布局 (`GridPane`)。创建 `GridPane` 并设置内边距和行列间距。设置列和行约束，使布局元素在窗口大小改变时自动调整大小和对齐方式。
   - **Buttons**: 创建多个按钮，包括冒险者状态、敌人状态、查看商店、开始战斗和下一关。
   - **Labels**: 创建显示当前关卡和目的地的标签。
   - **ImageViews**: 创建并设置图片（如冒险者和敌人的图像、金币或宝箱图像）。
   - 使用 `GridPane.setConstraints` 设置每个控件在 `GridPane` 中的位置。将所有控件添加到 `GridPane` 中。
   - 创建背景图像，并设置其透明度、宽度和高度以覆盖整个窗口，不保持宽高比。将背景图像和 `GridPane` 主布局添加到 `StackPane` 根布局中。创建 `Scene` 并设置大小为 800x400。
主要功能：
1. **按钮事件处理**:
   - **查看商店按钮 (`shopButton`)**: 访问商店 (`currentGame.visitShop()`).
   - **冒险者状态按钮 (`adventurerStatus`)**: 显示冒险者状态 (`currentGame.getHobbit().displayStatus()`).
   - **敌人状态按钮 (`enemyStatus`)**: 显示敌人状态 (`currentGame.getMonster().displayStatus()`).

2. **关卡类型处理**:
   - 如果当前关卡是普通关卡 (`NormalLevel`):启用开始战斗按钮 (`startBattleButton`) 并禁用下一关按钮 (`nextButton`)。绑定开始战斗按钮事件，处理战斗逻辑并根据结果调整按钮状态。绑定下一关按钮事件，前往下一关或显示结果。
   - 如果当前关卡是奖励关卡 (`RewardLevel`):禁用开始战斗按钮并启用下一关按钮。自动进行奖励关卡逻辑，并绑定下一关按钮事件。
### 商店界面
```java
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
```
代码分析：

   - 创建一个新的 `Stage` (`shopStage`) 用于显示商店界面。使用 `VBox` 作为布局，设置内边距和子节点之间的间距。创建 `TableView<Item>` 来显示道具列表，不允许编辑 (`tableView.setEditable(false)`)。

   - **道具名称列 (`nameColumn`)**: 显示道具名称，使用 `PropertyValueFactory` 绑定数据。
   - **道具价格列 (`priceColumn`)**: 显示道具价格，使用 `PropertyValueFactory` 绑定数据。
   - **道具作用列 (`effectColumn`)**: 显示道具作用，使用 `PropertyValueFactory` 绑定数据。
   - **购买道具按钮列 (`actionColumn`)**: 显示购买按钮，使用 `TableCell` 创建自定义单元格，按钮点击事件处理购买逻辑。
   - 创建一个 `ObservableList<Item>` 并将商店的道具列表添加到其中 (`shop.getItems()` 返回道具列表)。将 `ObservableList` 设置为 `TableView` 的数据源。将所有列添加到 `TableView` 中。将 `Label` 和 `TableView` 添加到 `VBox` 布局中。创建 `Scene` 并设置大小为 600x300。将场景设置到商店窗口上并显示 。


主要功能：

1. **显示道具信息**:
   - 通过 `TableView` 显示道具名称、价格和作用。
   - 使用 `PropertyValueFactory` 绑定 `Item` 对象的属性到对应的列。

2. **购买道具按钮**:
   - 在 `actionColumn` 中创建自定义单元格，每个单元格包含一个购买按钮。
   - 按钮点击事件处理逻辑：获取当前行的 `Item` 对象，通过 `shop.purchaseItem(item.getId(), hobbit)` 购买道具，并关闭商店窗口。




### 游戏结算界面

```java
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

```
代码分析：
   - 创建一个新的 `Stage` (`resultStage`) 用于显示游戏结果。使用 `StackPane` 堆叠背景图像和主布局 (`GridPane`)。创建 `GridPane` 并设置内边距和行列间距。设置列和行约束，使布局元素在窗口大小改变时自动调整大小和对齐方式。
   - **ImageView**: 显示冒险者和敌人的图像。
   - **Labels**: 显示游戏结果文本和冒险者评分。
   - **Buttons**: 显示冒险者最终状态、敌人最终状态和退出游戏按钮。
   - 使用 `GridPane.setConstraints` 设置每个控件在 `GridPane` 中的位置。将所有控件添加到 `GridPane` 中。创建背景图像，并设置其透明度、宽度和高度以覆盖整个窗口，不保持宽高比。将背景图像和 `GridPane` 主布局添加到 `StackPane` 根布局中。创建 `Scene` 并设置大小为 400x300。


主要功能：

1. **游戏结果计算**:
   - 如果冒险者的生命值大于0并且到达最终检查点，则胜利方为冒险者，否则为对手。
   - 显示游戏结果文本，包括目的地名称和胜利方。

1. **冒险者评分计算**:
   - 计算冒险者的总金币价值与总金币的比例，并格式化为百分比。

2. **按钮事件处理**:
   - **冒险者最终状态按钮**：显示冒险者的最终状态。
   - **敌人最终状态按钮**：显示敌人的最终状态。
   - **退出游戏按钮**：关闭结果窗口和主窗口。
### 角色状态与背包界面
```java
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
```

代码分析：

创建一个新的 `Stage` (`statusStage`) 用于显示角色状态界面。使用 `GridPane` 作为布局，设置内边距和行列之间的间距。创建一个 `HBox` (`lifeBox`) 用于水平排列心形图标来表示角色的生命值。使用 `ImageView` 加载心形图标，并根据角色类型（`Hobbit` 或其他）选择不同的心形图标.循环添加心形图标到 `lifeBox`，并将 `lifeBox` 添加到 `GridPane` 的第一行。创建一个 `HBox` (`coinBox`) 用于水平排列金币图标。获取角色的金币列表，并按金币面值和真假排序。循环添加金币图标到 `coinBox`，根据金币的面值和真假设置图标的大小和透明度。将 `coinBox` 添加到 `GridPane` 的第二行。创建三个按钮分别代表三个道具槽位，并为每个按钮设置点击事件处理程序。将按钮添加到 `GridPane` 的第三行。创建 `Scene` 并设置大小为 400x300并显示。

主要功能：

1. **显示角色生命值**:
   - 使用心形图标表示角色的当前生命值，通过循环添加 `ImageView` 来动态显示。

2. **显示金币**:
   - 将金币按面值和真假排序，并通过循环添加 `ImageView` 显示金币图标。
   - 根据金币的真假设置图标的透明度，显示假币时图标半透明。

3. **显示道具槽位**:
   - 使用按钮代表道具槽位，并为每个按钮设置点击事件，以便显示道具详情。



### 物品详情界面
```java
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
```


代码功能：
1. **获取道具**:
   - 通过传入的 `slot` 参数确定要显示的道具槽位。
   - 从 `backpack`（背包）中获取对应槽位的道具。如果槽位超出背包大小，则设置 `item` 为 `null`。

2. **显示错误信息**:
   - 如果 `item` 为空，显示一个警告对话框提示 "背包中没有找到该道具！"，并返回。

3. **创建道具详情对话框**:
   - 创建一个新的 `Stage`（对话框窗口），并设置为模态窗口（阻塞其他窗口操作）。
   - 设置对话框的标题为 "道具详情"。

4. **设置对话框布局**:
   - 使用 `VBox` 布局来垂直排列对话框中的控件，设置间距和内边距，并将对齐方式设置为居中。

5. **显示道具详情**:
   - 创建一个 `Label` 显示道具的详细信息，通过 `item.toString()` 获取道具描述。
   - 创建两个按钮：“使用”和“取消”。

6. **按钮功能**:
   - “使用”按钮的事件处理：调用 `useItemFromBackpack(id)` 方法使用该道具，并关闭对话框。
   - “取消”按钮的事件处理：直接关闭对话框。
## 小游戏效果展示

![alt text](image.png)

运行程序，进入游戏，打印出欢迎信息，以及提示用户输入目的地以及关卡数量，点击“开始游戏”即可开始闯关，“退出游戏”就结束程序。

![alt text](image-1.png)![alt text](image-2.png)

点击“开始游戏”后开始闯关，第一关碰到奖励关卡，两个窗口在用户端看来几乎是同时弹出，奖励关卡的正中心的图标是一个宝箱，在上面的提示显示出关卡数量，同时在后面加上“[奖励关卡]”表示此关为奖励关，左右分别显示冒险者和敌人的logo，同时，开始游戏按钮始终无法点击，下一关按钮常亮可点击。

对于关卡结果提示，奖励关卡的情况提示在上一关点击后直接弹出，打印出这一关的具体情况，以及冒险者以及对手的此时状态，当然，也可以点击“冒险者状态”或“敌人状态”进行查看，此页面在后面详细展示。

![alt text](image-3.png)

进入普通关卡，与上一奖励关卡不同之处在于中间图标表示换为金币图标，还有开始战斗选项的存在，此时下一关是无法点击。

![alt text](image-4.png)

点击开始战斗后，弹出弹窗提示战斗具体信息，同时“开始战斗”按钮变灰无法点击，“下一关”按钮变亮可以点击。
由于此关的金币是假金币，为了表示这一点，窗口正中间的金币图标变的十分透明（emmm最开始用灰色不太好看）。

![alt text](image-5.png)

展示商店系统，显示的信息由道具名称，价格，作用以及购买道具的按钮。购买后自动关闭窗口，当再次打开时窗口中对应商品消失。

![alt text](image-6.png)

冒险者状态栏，可以看到红心数量表示当前生命值，大金币表示面值为2的金币，小金币表示面值为1的金币，浅色金币表示假金币或是已经用来购买道具的金币（此处我考虑到最后计算评分需要用到这些“曾经”的真金币，所以购买物品后没有直接将这些金币丢弃，而是将其置为假）道具栏按钮表示每一个道具

![alt text](image-7.png)

点击道具后如果道具已经购买或者说是这个道具槽已经被填充，显示出这个窗口，表示道具的基本信息。
![alt text](image-8.png)

若道具不存在，则打印出错误信息，提示没有找到道具。

![alt text](image-9.png)

敌人状态，显示敌人生命值为黑心，金币系统与冒险者一样，敌人无法购买道具，道具槽始终为空。

![alt text](image-11.png)
当结束时，弹出游戏结果的界面，显示冒险者评分，此时也可以查询冒险者状态。点击退出游戏即可终止程序。


![alt text](image-12.png)

点击“查询冒险者最终状态”可得此页面。

## 实验收获与感悟

对于这样的一个类肉鸽小游戏，从最开始的简单冒险功能，到添加商店系统最后到实现UI界面，代码一次一次重构，到最后类一共有11个之多，这几次实验确实使得面向对象的设计思想深入到自己的编码习惯中，尤其到最后重构代码实现UI界面，我发现，前几次lab自己面向对象做的好的地方实现UI界面明显比面向对象做的差的地方实现的容易很多。除此之外，使用Javafx实现UI界面也是我第一次写这种前端相关的代码，对这种写页面代码有了基本的认识和书写思路。虽然任务稍显繁杂，但确实提升了这两方面的能力，收获颇多😄

