import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private final String destination;
    private final Creature hobbit;
    private final Creature monster;
    private int checkpoint;
    private int finalCheckpoint;
    private List<Level> levels;
    private Scanner scanner;

    public Game(String destination, Creature hobbit, Creature monster) {
        this.destination = destination;
        this.hobbit = hobbit;
        this.monster = monster;
        this.checkpoint = 1;
        this.levels = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    private void initializeLevels(int totalLevels) {
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

    public void start() {
        System.out.println("请输入关卡数：");
        finalCheckpoint = scanner.nextInt();
        scanner.nextLine(); // consume the newline

        System.out.println("冒险开始！目的地：" + destination);
        System.out.println("初始化关卡中.....");
        initializeLevels(finalCheckpoint);
        System.out.println("初始化成功！");
        System.out.println("本次冒险的对手是" + monster.toString());

        Shop shop = new Shop();
        for (Level level : levels) {
            int status = level.play(hobbit, monster);
            if (status == 1) {
                break;
            }

            if (hobbit.getLife() <= 0) {
                System.out.println("冒险者生命值已降为0，游戏结束！");
                break;
            }
            if (checkpoint == finalCheckpoint) {
                System.out.println("恭喜，冒险者成功抵达目的地！");
                break;
            }
            if (status == 2) {
                System.out.println("你最好在接下来的商店中购买生命药水，否则你将大概率失败！");
            }
            checkpoint++;

            visitShop(shop);

            System.out.println("是否继续下一关？(输入 y 继续，其他键退出)");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                System.out.println("游戏提前结束，直接结算（若冒险者未到达目的地则判负）");
                break;
            }
        }
    }

    private void visitShop(Shop shop) {
        while (true) {
            shop.displayItems();
            System.out.println("请输入要购买的道具ID，或输入y跳过购买：");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("y")) {
                break;
            }
            try {
                int id = Integer.parseInt(input);
                shop.purchaseItem(id, hobbit);
            } catch (NumberFormatException e) {
                System.out.println("输入不合法，请重新输入！");
            }
        }
    }

    public int computeTotalMoney() {
        int totalMoney = 0;
        for (int i = 0; i < levels.size(); i++) {
            if (i % 3 != 0 || i == 0) {
                if (levels.get(i).getCoin().isReal()) {
                    totalMoney++;
                }
            } else totalMoney += 4;
        }
        return totalMoney;
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
