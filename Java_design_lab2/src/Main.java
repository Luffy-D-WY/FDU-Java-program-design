import java.util.*;
public class Main {
    public static void main(String[] args)
    {
        int L,W,num;
        Scanner input=new Scanner(System.in);
        L=input.nextInt();
        W=input.nextInt();
        num=input.nextInt();
        char [][]map=new char[L][W];

        makeMap(map,num);
        System.out.println("欢迎来到" +
                "吴优_22307130158" +
                "制作的扫雷游戏！打印的地雷图如下");
        printMap(map);


    }

    public static void makeMap(char [][]a,int num)
    {
       Random generator=new Random();
       for(int i=0;i<num;)
       {
           int m1=generator.nextInt(a.length),m2= generator.nextInt(a[0].length);
           if(a[m1][m2]!='*')
           {
               a[m1][m2]='*';
               i++;
           }
       }
       for(int i=0;i<a.length;i++)
       {
           for(int j=0;j<a[0].length;j++)
           {
               int val=0;
               if(a[i][j]!='*')
               {
                   for(int k=Math.max(i-1,0);k<=Math.min(i+1,a.length-1);k++){
                       for(int t=Math.max(j-1,0);t<=Math.min(j+1,a[0].length-1);t++){
                           if(a[k][t]=='*'){
                               val++;
                           }
                       }
                   }
                   a[i][j]=(char)(val+'0');
               }

           }
       }


    }
    public static void printMap(char[][]a)
    {
        for(char[]arr:a)
        {
            for(char i:arr)
            {
                System.out.print(i+" ");
            }
            System.out.println();
        }

    }
}