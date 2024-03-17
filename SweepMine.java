public class SweepMine {
    public static boolean SweepOneMine(char [][]innerMap,boolean[][]outerMap,boolean[][]markMap,boolean [][]visited,int row,int column)
    {

        if(innerMap[row][column]=='*')
        {
            return true;
        }
        else
        {
            subSweep(innerMap,outerMap,markMap,visited,row,column);
            return false;
        }
    }//true----bomb,false----continue
    public static void subSweep(char[][]innerMap,boolean[][]outerMap,boolean[][]markMap,boolean [][]visited,int row,int column)
    {
        if(markMap[row][column])
        {
            return;
        }

        if(innerMap[row][column]!='*')
        {
            outerMap[row][column]=false;
            visited[row][column]=true;
        }
        else return;
        if(innerMap[row][column]=='0')
        {
            if(row>0&&!visited[row-1][column])
                subSweep(innerMap,outerMap,markMap,visited,row-1,column);
            if(row<innerMap.length-1&&!visited[row+1][column])
                subSweep(innerMap,outerMap,markMap,visited,row+1,column);
            if(column>0&&!visited[row][column-1])
                subSweep(innerMap,outerMap,markMap,visited,row,column-1);
            if(column<innerMap[0].length-1&&!visited[row][column+1])
                subSweep(innerMap,outerMap,markMap,visited,row,column+1);
            if(row<innerMap.length-1&&column>0&&!visited[row+1][column-1])
                subSweep(innerMap,outerMap,markMap,visited,row+1,column-1);
            if(row>0&&column<innerMap[0].length-1&&!visited[row-1][column+1])
                subSweep(innerMap,outerMap,markMap,visited,row-1,column+1);
            if(row>0&&column>0&&!visited[row-1][column-1])
                subSweep(innerMap, outerMap, markMap, visited, row-1, column-1);
            if(row< innerMap.length-1&&column<innerMap[0].length-1&&!visited[row+1][column+1])
                subSweep(innerMap, outerMap, markMap, visited, row+1, column+1);
        }
    }
    public static void markOneMine(boolean[][]markMap,int row,int column)
    {
        markMap[row][column]= !markMap[row][column];

    }

}
