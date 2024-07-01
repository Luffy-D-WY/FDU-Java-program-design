import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Batch_test {
    private static Tensor kernel;
    // 加载卷积核
    public static void loadKernel(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // 读取四个整数，分别表示输出通道数、输入通道数、卷积核高度、卷积核宽度
            String[] sizes = reader.readLine().trim().split("\\s+");
            int outputChannels = Integer.parseInt(sizes[0]);
            int inputChannels = Integer.parseInt(sizes[1]);
            int kernelHeight = Integer.parseInt(sizes[2]);
            int kernelWidth = Integer.parseInt(sizes[3]);

            // 根据大小创建四阶张量
            kernel = new Tensor(new int[]{outputChannels, inputChannels, kernelHeight, kernelWidth});

            // 读取每个卷积核的数据
            for (int o = 0; o < outputChannels; o++) {
                for (int i = 0; i < inputChannels; i++) {
                    for (int h = 0; h < kernelHeight; h++) {
                        String[] values = reader.readLine().trim().split("\\s+");
                        for (int w = 0; w < kernelWidth && w < values.length; w++) {
                            kernel.set( Integer.parseInt(values[w]),o, i, h, w);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 读取图像并转换成张量
    public static Tensor loadImageAsTensor(String imagePath) {
        try {
            BufferedImage image = ImageIO.read(new File(imagePath));
            int height = image.getHeight();
            int width = image.getWidth();
            int[][][] tensorData = new int[3][height][width]; // 假设图片的颜色通道数为 3
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    Color pixelColor = new Color(image.getRGB(j, i));
                    tensorData[0][i][j] = pixelColor.getRed(); // 红色通道
                    tensorData[1][i][j] = pixelColor.getGreen(); // 绿色通道
                    tensorData[2][i][j] = pixelColor.getBlue(); // 蓝色通道
                }
            }
            return new Tensor(tensorData);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    // 对张量进行卷积操作
    public static Tensor convolution(Tensor input) {
        // 获取输入张量的维度信息
        int[] inputDims = input.getDimensions();
        int inputChannels = inputDims[0];
        int inputHeight = inputDims[1];
        int inputWidth = inputDims[2];

        // 获取卷积核的维度信息
        int[] kernelDims = kernel.getDimensions();
        int outputChannels = kernelDims[0];

        // 创建输出张量
        int[][][] outputData = new int[outputChannels][inputHeight-2][inputWidth-2];

        // 对每个输出通道进行卷积操作
        for (int o = 0; o < outputChannels; o++) {
            // 遍历输入张量的每个像素，进行卷积
            for (int i = 0; i < inputHeight-2; i++) {
                for (int j = 0; j < inputWidth-2; j++) {
                    // 初始化卷积结果为 0
                    int result = 0;
                    // 对每个输入通道进行卷积操作
                    for (int c = 0; c < inputChannels; c++) {
                        // 对每个卷积核元素进行遍历
                        for (int m = 0; m < kernelDims[2]; m++) {
                            for (int n = 0; n < kernelDims[3]; n++) {
                                // 计算卷积核在输入张量上的索引位置
                                int inputI = i  + m;
                                int inputJ = j  + n;
                                    // 执行卷积操作
                                    result += (int)input.get(c, inputI, inputJ) * (int)kernel.get(o, c, m, n);
                            }
                        }
                    }
                    outputData[o][i][j] = result;
                }
            }
        }
        // 使用输出数据创建输出张量
        return new Tensor(outputData);
    }
    // 保存张量到文件
    public static void saveTensorToFile1(Tensor tensor, String filePath,int imageLength) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,true))) {
            int[][][] data = (int[][][])tensor.getData();
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[i].length; j++) {
                    for (int k = 0; k < data[i][j].length; k++) {
                            writer.write(data[i][j][k] + " ");
                    }
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveTensorToFile2(Tensor tensor, String filePath,int imageLength)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(imageLength + " ");
            int[]dimensions=tensor.getDimensions();
            for (int dim : dimensions) {
                writer.write(dim + " ");
            }
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static int[][][] convertToPrimitive(Object[] objectArray) {
        int[][][] result = new int[objectArray.length][][];

        for (int i = 0; i < objectArray.length; i++) {
            Object[] innerArray = (Object[]) objectArray[i];
            result[i] = new int[innerArray.length][];
            for (int j = 0; j < innerArray.length; j++) {
                Object[] iinnerArray=(Object[]) innerArray[j];
                result[i][j]=new int[iinnerArray.length];
                for (int k = 0; k < iinnerArray.length; k++) {
                    result[i][j][k] =(int) iinnerArray[k];
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        // 加载卷积核
        loadKernel("./conv_kernel.txt");
        // 遍历图像文件夹
        File figsDir = new File("./figs");
        File[] groups = figsDir.listFiles(File::isDirectory);
        if (groups != null) {
            for (File group : groups) {
                File[] images = group.listFiles();
                if (images != null && images.length > 0) {
                    // 创建一个张量，存储当前组中的所有图片
                    Tensor tensor = new Tensor(images.length, 3, loadImageAsTensor(images[0].getPath()).getDimensions()[1], loadImageAsTensor(images[0].getPath()).getDimensions()[2]);
                    for (int i = 0; i < images.length; i++) {
                        // 读取图像并转换成张量
                        Tensor imageTensor = loadImageAsTensor(images[i].getPath());
                        tensor.set(imageTensor.getData(), i);
                    }
                    Tensor padedTensor=tensor.pad(2,2);
                    int tag=0;
                    // 对张量进行填充
                    for(int i=0;i<images.length;i++)
                    {
                        int [][][]mm=convertToPrimitive(tensor.get(i));
                        Tensor temp=new Tensor(mm);
                        Tensor resultTensor = convolution(temp);//temp为三阶张量-输入*长宽，resultTensor为三阶张量-输出*长宽
                        String resultFilePath = String.format("./results/%03d_result.txt", Integer.parseInt(group.getName()));
                        if(tag == 0)
                            saveTensorToFile2(resultTensor, resultFilePath,images.length);
                        saveTensorToFile1(resultTensor, resultFilePath,images.length);
                        tag++;
                    }
                }
            }
        }
    }

//生成图像的主函数版本

//public static void main(String[] args) {
//    // 加载卷积核
//    loadKernel("./conv_kernel.txt");
//    // 遍历图像文件夹
//    File figsDir = new File("./figs");
//    File[] groups = figsDir.listFiles(File::isDirectory);
//    if (groups != null) {
//        for (File group : groups) {
//            File[] images = group.listFiles();
//            if (images != null && images.length > 0) {
//                // 创建一个张量，存储当前组中的所有图片
//                Tensor tensor = new Tensor(images.length, 3, loadImageAsTensor(images[0].getPath()).getDimensions()[1], loadImageAsTensor(images[0].getPath()).getDimensions()[2]);
//                for (int i = 0; i < images.length; i++) {
//                    // 读取图像并转换成张量
//                    Tensor imageTensor = loadImageAsTensor(images[i].getPath());
//                    tensor.set(imageTensor.getData(), i);
//                }
//                Tensor padedTensor=tensor.pad(2,2);
//                // 对张量进行填充
//                for(int i=0;i<images.length;i++)
//                {
//                    int [][][]mm=convertToPrimitive(tensor.get(i));
//                    Tensor temp=new Tensor(mm);
//                    Tensor resultTensor = convolution(temp);//temp为三阶张量-输入*长宽，resultTensor为三阶张量-输出*长宽
//                    // 将张量转换为图像
//                    BufferedImage resultImage = new BufferedImage(resultTensor.getDimensions()[2], resultTensor.getDimensions()[1], BufferedImage.TYPE_INT_RGB);
//                    for (int y = 0; y < resultTensor.getDimensions()[1]; y++) {
//                        for (int x = 0; x < resultTensor.getDimensions()[2]; x++) {
//                            // 将张量中的值映射到图像的像素值范围内
//                            int pixelValue = resultTensor.get(0, y, x); // 假设输出通道为1
//                            pixelValue = Math.min(Math.max(pixelValue, 0), 255); // 确保像素值在合法范围内
//                            int rgb = (pixelValue << 16) | (pixelValue << 8) | pixelValue; // 创建灰度值的RGB表示
//                            resultImage.setRGB(x, y, rgb);
//                        }
//                    }
//                    // 保存图像文件
//                    String resultFilePath = String.format("./results/%03d_result_%d.png", Integer.parseInt(group.getName()), i);
//                    try {
//                        ImageIO.write(resultImage, "png", new File(resultFilePath));
//                        System.out.println("Result image saved to: " + resultFilePath);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//    }
//}

}