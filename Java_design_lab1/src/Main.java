import java.util.*;
public class Main {
    public static void main(String[] args) {
        char [][]arr=new char[5][5];
        Scanner scn=new Scanner(System.in);
        input(scn,arr);

        while(true)
        {
            String cmd=scn.next();
            if(cmd.equals("print"))
            {
                printArr(arr);
            }
            else if(cmd.equals("at"))
            {
                int col=scn.nextInt();
                int row=scn.nextInt();

                printPoints(arr,col,row);
            }
            else if(cmd.equals("exit"))
            {
                break;
            }
            else
            {
                System.out.println("invalid input");
            }


        }
    }
    public static void input(Scanner input,char[][]arr)
    {
        for(int i=0;i<arr.length;i++)
        {
            String temp=input.next();
            for(int j=0;j<arr.length;j++)
            {
                arr[i][j]=temp.charAt(2*j);

            }
        }
    }

    public static void printArr(char[][]arr)
    {
        for(char []a:arr)
        {
            for(char b:a)
            {
                System.out.print(b+" ");
            }
            System.out.println();
        }

    }
    public static void printPoints(char[][]arr,int col,int row)
    {
        System.out.print("[");
        printPoint(arr[row][col],col,row);
        if(row>0)
            printPoint(arr[row-1][col],col,row-1);
        if(row<4)
            printPoint(arr[row+1][col],col,row+1);
        if(col>0)
            printPoint(arr[row][col-1],col-1,row);
        if(col<4)
            printPoint(arr[row][col+1],col+1,row);
        System.out.println("]");


    }
    public static void printPoint(char a,int col,int row)
    {
        System.out.print("("+col+","+row+","+a+")");
    }

}