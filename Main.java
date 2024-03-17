import java.util.*;
public class Main {
    public static void main(String[] args) {
        while(true)
        {
            CommandControl cmd1=new CommandControl();
            cmd1.inputData();
            MakeMap makeMap=new MakeMap(cmd1);
            makeMap.initBoolMap();
            makeMap.makeMap();
            PrintMap.printMap(makeMap.innerMap, makeMap.outerMap,makeMap.markMap);
            cmd1.oneGame(makeMap);
            if(!cmd1.continueOrNot())
                break;
        }
    }

}