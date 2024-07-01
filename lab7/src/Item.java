public class Item {
    private int id;
    private String name;
    private int price;
    private String description;

    // 构造函数
    public Item(int id, String name, int price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    // 获取道具ID的方法
    public int getId() {
        return id;
    }

    // 获取道具名称的方法
    public String getName() {
        return name;
    }

    // 获取道具价格的方法
    public int getPrice() {
        return price;
    }

    // 获取道具描述的方法
    public String getDescription() {
        return description;
    }

    // 重写toString方法，便于打印Item对象信息
    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
