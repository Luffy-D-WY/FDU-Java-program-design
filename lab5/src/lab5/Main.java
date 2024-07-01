package lab5;

import java.io.File;

public class Main {
    public static void main(String []args)
    {

        FamilyMart a=new FamilyMart();

        for(int i=0;i<=5;i++)
        {
            String purchaseFilePath="src/lab5/test_cases/"+i+"_pur.txt";
            String sellFilePath="src/lab5/test_cases/"+i+"_sel.txt";
            a.purchase(purchaseFilePath);
            a.sell(sellFilePath);
            a.clearOutOfDate();
            a.date++;
            a.WriteToFile();
        }
    }

}
