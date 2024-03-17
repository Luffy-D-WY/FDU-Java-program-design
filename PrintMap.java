public class PrintMap {

    public static void printMap(char[][]a,boolean [][]b,boolean [][]c)
    {
        for(int i=0;i<a.length;i++)
        {
            for(int j=0;j<a[0].length;j++)
            {
                if(c[i][j])
                {
                    System.out.print("$ ");
                    continue;
                }
                if(b[i][j])
                {
                    System.out.print("# ");
                }
                else
                {
                    System.out.print(a[i][j]+" ");
                }

            }
            System.out.println();
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
