package lab5;



class Product implements Comparable<Product> {
    public String name;
    public double price;
    public int life;
    public int produceDate;//以开业时间为零点计算生产日期
    public int produceYear;
    public int produceMonth;
    public int produceDay;


    @Override
    public int compareTo(Product other) {
        // 比较生产日期，将日期转换为Comparable格式进行比较
        return Integer.compare(this.produceDate, other.produceDate);
    }
}
