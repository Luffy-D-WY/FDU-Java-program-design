import java.util.Arrays;
import java.util.Random;
public class MakeMap {
    int row;
    int column;
    int num;
    char [][]innerMap;
    boolean [][]outerMap;//true---mask false----unmask
    boolean [][]markMap;//true---mark  false---unmark
    boolean [][]visited;
    public MakeMap(CommandControl cmd)
    {
        this.row=cmd.row;
        this.column=cmd.column;
        this.num=cmd.num;
    }
    public void makeMap()
    {

        Random generator=new Random();
        for(int i=0;i<num;)
        {
            int m1=generator.nextInt(innerMap.length),m2= generator.nextInt(innerMap[0].length);
            if(innerMap[m1][m2]!='*')
            {
                innerMap[m1][m2]='*';
                i++;
            }
        }
        for(int i=0;i<innerMap.length;i++)
        {
            for(int j=0;j<innerMap[0].length;j++)
            {
                int val=0;
                if(innerMap[i][j]!='*')
                {
                    for(int k=Math.max(i-1,0);k<=Math.min(i+1,innerMap.length-1);k++){
                        for(int t=Math.max(j-1,0);t<=Math.min(j+1,innerMap[0].length-1);t++){
                            if(innerMap[k][t]=='*'){
                                val++;
                            }
                        }
                    }
                    innerMap[i][j]=(char)(val+'0');
                }
            }
        }
    }
    public  void initBoolMap()
    {
        innerMap=new char[row][column];
        outerMap=new boolean[row][column];
        markMap=new boolean[row][column];
        visited=new boolean[row][column];
        for (boolean[] booleans : outerMap)
        {
            Arrays.fill(booleans, true);//表示全部被覆盖中
        }
        for(boolean[]booleans:markMap)
        {
            Arrays.fill(booleans,false);
        }
        for(boolean []booleans:visited)
        {
            Arrays.fill(booleans,false);
        }
    }
}
