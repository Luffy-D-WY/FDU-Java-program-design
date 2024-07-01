import java.util.Arrays;
import java.util.Random;

public class Tensor {
    private  int[] dimensions;
    private Object data;

    // 构造函数，递归调用以创建高维数组
    public Tensor(int... dimensions) {
        this.dimensions = dimensions;
        this.data = createData(dimensions, 0);
    }
    // 构造函数，接受一个 int[][][] 数组作为参数
    public Tensor(int[][][] data) {
        this.data = data;
        this.dimensions = new int[]{data.length, data[0].length, data[0][0].length};
    }
    public Object getData(){
        return this.data;
    }

    // 递归方法，用于创建数据
    private Object createData(int[] dimensions, int index) {
        if (index == dimensions.length - 1) {
            // 创建最内层的数组
            return new Object[dimensions[index]];
        } else {
            // 递归创建多维数组
            Object[] dataArray = new Object[dimensions[index]];
            for (int i = 0; i < dimensions[index]; i++) {
                dataArray[i] = createData(dimensions, index + 1);
            }
            return dataArray;
        }
    }
    // 用于访问Tensor中元素的getter方法
    public <T> T get(int... indices) {
        Object current = data;
        for (int index : indices) {
            if (current instanceof Object[]) {
                current = ((Object[]) current)[index];
            } else if (current instanceof int[]) {
                current = ((int[]) current)[index];
            } else {
                throw new IllegalStateException("Unsupported data type");
            }
        }
        // 强制类型转换并返回
        return (T) current;
    }


    // 用于设置Tensor中元素的setter方法
    public void set(Object value, int... indices) {
        Object current = data;
        for (int i = 0; i < indices.length - 1; i++) {
            current = ((Object[]) current)[indices[i]];
        }
        ((Object[]) current)[indices[indices.length - 1]] = value;
    }


    // 用于获取Tensor的维度信息
    public int[] getDimensions() {
        return dimensions.clone();
    }
    public Tensor sub(Tensor other)
    {
        int[]dims=getDimensions();
        Tensor result=new Tensor(dims);
        subRecursive(this.data,other.data,result.data,0);
        return result;
    }
    // 实现两个Tensor的加法
    public Tensor add(Tensor other) {
        int[] dims = getDimensions();
        Tensor result = new Tensor(dims);
        addRecursive(this.data, other.data, result.data, 0);
        return result;
    }
    private void subRecursive(Object data1, Object data2, Object result, int index) {
        if (index == dimensions.length - 1) {
            int length = ((Object[]) data1).length;
            for (int i = 0; i < length; i++) {
                // Assuming the data types support addition
                // You might need to handle different data types separately
                ((Object[]) result)[i] = (int)((Object[]) data1)[i] - (int)((Object[]) data2)[i];
            }
        } else {
            int length = ((Object[]) data1).length;
            for (int i = 0; i < length; i++) {
                addRecursive(((Object[]) data1)[i], ((Object[]) data2)[i], ((Object[]) result)[i], index + 1);
            }
        }
    }
    // 递归方法，用于实现两个Tensor的加法
    private void addRecursive(Object data1, Object data2, Object result, int index) {
        if (index == dimensions.length - 1) {
            int length = ((Object[]) data1).length;
            for (int i = 0; i < length; i++) {
                ((Object[]) result)[i] = (int)((Object[]) data1)[i] + (int)((Object[]) data2)[i];
            }
        } else {
            int length = ((Object[]) data1).length;
            for (int i = 0; i < length; i++) {
                addRecursive(((Object[]) data1)[i], ((Object[]) data2)[i], ((Object[]) result)[i], index + 1);
            }
        }
    }
        public Tensor pad(int padHeight, int padWidth)
        {
            int[] originalDimensions = getDimensions();
            int originalHeight = originalDimensions[originalDimensions.length - 2];
            int originalWidth = originalDimensions[originalDimensions.length - 1];

            // 计算填充后的尺寸
            int paddedHeight = originalHeight + 2 * padHeight;
            int paddedWidth = originalWidth + 2 * padWidth;

            // 创建填充后的张量
            int[] paddedDimensions = Arrays.copyOf(originalDimensions, originalDimensions.length);
            paddedDimensions[originalDimensions.length - 2] = paddedHeight;
            paddedDimensions[originalDimensions.length - 1] = paddedWidth;
            Tensor paddedTensor = new Tensor(paddedDimensions);
            for(int m=0;m<paddedTensor.dimensions[0];m++)
            {
                for(int n=0;n<paddedTensor.dimensions[1];n++)
                {
                    // 填充操作
                    for (int i = 0; i < originalHeight; i++) {
                        for (int j = 0; j < originalWidth; j++) {
                            // 复制原始张量的数据到填充后的张量中心区域
                            paddedTensor.set(get(m,n,i, j),m,n, i + padHeight, j + padWidth);
                        }
                    }

                    // 左侧和右侧填充
                    for (int i = 0; i < paddedHeight; i++)
                    {
                        for (int j = 0; j < padWidth; j++) {
                            paddedTensor.set(0,m,n, i, j); // 左侧填充
                            paddedTensor.set(0, m,n,i, paddedWidth - 1 - j); // 右侧填充
                        }
                    }

                    // 上侧和下侧填充
                    for (int i = 0; i < padHeight; i++) {
                        for (int j = 0; j < paddedWidth; j++) {
                            paddedTensor.set(0,m,n, i, j); // 上侧填充
                            paddedTensor.set(0, m,n,paddedHeight - 1 - i, j); // 下侧填充
                        }
                    }
                }
            }
            this.data=paddedTensor.data;
            this.dimensions=paddedTensor.dimensions;
            return paddedTensor;
        }

    public Tensor stretch(int newHeight, int newWidth) {
        int[] originalDimensions = getDimensions();
        int originalHeight = originalDimensions[originalDimensions.length - 2];
        int originalWidth = originalDimensions[originalDimensions.length - 1];

        // 创建拉伸后的张量
        int[] stretchedDimensions = Arrays.copyOf(originalDimensions, originalDimensions.length);
        stretchedDimensions[originalDimensions.length - 2] = newHeight;
        stretchedDimensions[originalDimensions.length - 1] = newWidth;
        Tensor stretchedTensor = new Tensor(stretchedDimensions);

        // 计算拉伸比例
        double heightRatio = (double)newHeight / originalHeight;
        double widthRatio = (double)newWidth / originalWidth;

        // 根据拉伸比例进行像素值映射
        Random random = new Random(); // 用于生成随机数
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                // 计算在原始张量中对应的位置
                int originalRow = (int)(i / heightRatio);
                int originalCol = (int)(j / widthRatio);

                // 获取对应位置的像素值
                Object pixelValue;
                if (originalRow < originalHeight && originalCol < originalWidth) {
                    pixelValue = get(originalRow, originalCol);
                } else {
                    // 如果超出了原始张量的范围，可以选择不同的处理方式，这里选择随机取值
                    pixelValue = random.nextInt(); // 随机取值
                }

                // 将像素值设置到拉伸后的张量中
                stretchedTensor.set(pixelValue, i, j);
            }
        }

        return stretchedTensor;
    }


}
