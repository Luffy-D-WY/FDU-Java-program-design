import java.util.Objects;
import java.util.Scanner;

public class CommandControl {
    int row;
    int column;
    int num;
    Scanner scn;
    public static boolean isSweepOut(char [][]innerMap,boolean[][]outerMap)
    {
        for(int i=0;i<innerMap.length;i++)
        {
            for(int j=0;j<innerMap[0].length;j++)
            {
                if(innerMap[i][j]!='*'&&outerMap[i][j])
                    return false;
            }
        }
        return true;
    }
    public void oneGame(MakeMap makeMap)
    {
        int flag=1;
        while (!CommandControl.isSweepOut(makeMap.innerMap, makeMap.outerMap)) {
            System.out.println("左键排雷则输入1，右键标记则输入2");
            int judgeNum = scn.nextInt();
            System.out.printf("请输入1-%d 1-%d来确定要操作的坐标\n", makeMap.row, makeMap.column);
            int inputRow = scn.nextInt();  int inputColumn = scn.nextInt();
            if (judgeNum == 1)
            {
                if (SweepMine.SweepOneMine(makeMap.innerMap, makeMap.outerMap, makeMap.markMap, makeMap.visited,inputRow- 1, inputColumn - 1))
                {
                    System.out.println("踩中地雷，游戏结束");
                    PrintMap.printMap(makeMap.innerMap);
                    flag = 0;
                    break;
                }
                else
                {
                    PrintMap.printMap(makeMap.innerMap, makeMap.outerMap, makeMap.markMap);
                }
            }
            else if(judgeNum==2)
            {
                SweepMine.markOneMine(makeMap.markMap, inputRow-1,inputColumn-1);
                PrintMap.printMap(makeMap.innerMap, makeMap.outerMap, makeMap.markMap);
            }
        }
        if (flag == 1)
            System.out.println("恭喜，扫雷完成");
    }
    public void inputData()
    {

        scn=new Scanner(System.in);
        System.out.println("请依次输入行、列以及地雷数");
        row=scn.nextInt();
        column=scn.nextInt();
        num=scn.nextInt();
    }
    public boolean continueOrNot()
    {
        System.out.println("是否继续游戏（1/0)");
        int input=scn.nextInt();
        return input != 0;
    }
}
