import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("欢迎来到Middle-Earth Adventure!");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("请输入冒险的目的地名称（4至16个字母和空格，不能以空格开头或结尾）:");
            String destination = scanner.nextLine();

            if (isValidDestination(destination)) {
                startAdventure(destination);
            } else {
                System.out.println("目的地名称格式不正确，请重新输入。");
            }

            System.out.println("是否继续游戏？(输入 y 继续，其他键退出)");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                break;
            }
        }

        System.out.println("游戏结束，谢谢参与！");
    }

    private static boolean isValidDestination(String destination) {
        return destination.matches("^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$") && destination.length() >= 4 && destination.length() <= 16;
    }

    private static void startAdventure(String destination) {
        Creature hobbit = new Hobbit();
        Creature monster = getRandomMonster();
        Game game = new Game(destination, hobbit, monster);
        game.start();
        game.displayResult();
    }

    private static Creature getRandomMonster() {
        int randomIndex = (int) (Math.random() * 3);

        switch (randomIndex) {
            case 0:
                return new Dwarf(9, null);//生命值为9
            case 1:
                return new Elf(7, null);//生命值为7
            default:
                return new Orc(8, null);//生命值为8
        }
    }
}
