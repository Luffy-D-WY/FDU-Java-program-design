package lab5;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FamilyMart {

    HashMap<String,PriorityQueue<Product>> allProducts;
    public int date;
    public double turnover;
    public FamilyMart()
    {
        allProducts=new HashMap<String,PriorityQueue<Product>>();
        date=0;
        turnover=0;
    }


    public void purchase(String filePath)
    {
       try(BufferedReader br=new BufferedReader(new FileReader(filePath))){
           String firstLine =br.readLine();
           String line;
           while((line=br.readLine())!=null)
           {
               String []data=line.split("\\s+");//以若干个空白符分割
               Product a=new Product();
               if(data.length==4)
               {
                   a.name=data[0];
                   a.price=Double.parseDouble(data[1]);
                   a.life=Integer.parseInt(data[2]);

                   String []dateData=data[3].split("/");
                   a.produceYear=Integer.parseInt(dateData[0]);
                   a.produceMonth=Integer.parseInt(dateData[1]);
                   a.produceDay=Integer.parseInt(dateData[2]);
                   LocalDate endDate = LocalDate.of(a.produceYear,a.produceMonth,a.produceDay);
                   LocalDate startDate = LocalDate.of(2022, 5, 2);

                   // 计算日期之间的天数差异
                   a.produceDate = (int)ChronoUnit.DAYS.between(startDate, endDate);

               }
               if(!isOutOfDate(a)) {
                   addProduct(a);
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       }

    }
    public void sell(String filePath)
    {
        try (BufferedReader br=new BufferedReader(new FileReader(filePath)))
        {
            String temp=br.readLine();
            String line;
            while((line=br.readLine())!=null) {
                String[] data = line.split("\\s+");
           if (allProducts.get(data[0]) != null) {
               Product soldProduct = allProducts.get(data[0]).poll();
               if (soldProduct != null && data.length == 1) {

                   turnover += soldProduct.price;

               }
               if (soldProduct != null && data.length == 2) {

                   turnover += soldProduct.price * Double.parseDouble(data[1]);
               }
           }
          }

        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public void WriteToFile()
    {
        String filePath = "src/lab5/result.txt"; // 要写入的文件路径

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath,true))) {
            // 要写入文件的内容
            String content=date+" day : turnover:"+String.format("%.2f",turnover);
            bw.write(content);
            bw.newLine();

            System.out.println("内容已成功写入文件：" + filePath);
        } catch (IOException e) {
            System.err.println("写入文件时出现异常：" + e.getMessage());
        }
    }

    public void clearOutOfDate() {
        List<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, PriorityQueue<Product>> entry : allProducts.entrySet()) {
            PriorityQueue<Product> queue = entry.getValue();
            Iterator<Product> iterator = queue.iterator();
            while (iterator.hasNext()) {
                Product product = iterator.next();
                if (isOutOfDate(product)) {
                    iterator.remove(); // 删除过期商品
                }
                else
                {
                    break;
                }
                // 如果队列为空，记录需要删除的键
                if (queue.isEmpty()) {
                    keysToRemove.add(entry.getKey());
                }
            }
            // 删除空队列对应的键值对

        }
        for (String key : keysToRemove) {
            allProducts.remove(key);
        }
    }
    public void addProduct(Product product) {

        String productName=product.name;
        PriorityQueue<Product> queue=allProducts.get(productName);
        if(queue==null)
        {
            queue=new PriorityQueue<>();
            allProducts.put(productName,queue);

        }
        queue.offer(product);


    }
    public Boolean  isOutOfDate(Product product)
    {
        if (product.produceDate+product.life<=date) {
            return true;
        }
        else return false;
    }


}
