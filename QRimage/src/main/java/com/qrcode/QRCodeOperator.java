package com.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.io.File;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;
import static com.qrcode.MainCommandLine.CHARTSET;
import static com.qrcode.MainCommandLine.writeToPath;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by qxp on 2018/8/30.
 */
public class QRCodeOperator {
    //当使用bmp格式时，MatrixToImageConfig ARGB模式，设置的oncolor\offcolor A必须是FF,否者需要设置为png.
    public static final String FORMAT = "bmp";
    public static final String CHARTSET = "utf-8";
    public static final int WIDTH = 300;
    public static final int HEIGHT = 300;
    int onColor=-16777216;
    int offColor=-1;


     private void writeToPath(BufferedImage image,String format,Path path,String logoPath) throws IOException {
        BufferedImage plane=new BufferedImage(image.getWidth(),image.getHeight(),TYPE_INT_RGB);
        Graphics2D gs = plane.createGraphics();
        gs.drawImage(image,0,0,image.getWidth(),image.getHeight(),null);

        int ratioWidth = image.getWidth()*2/10;
        int ratioHeight = image.getHeight()*2/10;
        //载入logo
        Image img = ImageIO.read(new File(logoPath));
        int logoWidth = img.getWidth(null)>ratioWidth?ratioWidth:img.getWidth(null);
        int logoHeight = img.getHeight(null)>ratioHeight?ratioHeight:img.getHeight(null);

        int x = (image.getWidth() - logoWidth) / 2;
        int y = (image.getHeight() - logoHeight) / 2;

        gs.drawImage(img, x, y, logoWidth, logoHeight, null);
        // gs.setColor(Color.red);
        //  gs.setBackground(Color.WHITE);
        gs.dispose();
        img.flush();
        image.flush();
        if(!ImageIO.write(plane, format, path.toFile())){
            throw new IOException("Could not write an image of format " + format + " to " + path);
        }
    }

    //创建带logo的二维码
    public  void createQRImg(String QRContent,String filePath,String logoPath)
    {
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARTSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(QRContent, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            Path file = new File(filePath).toPath();
            BufferedImage image = toBufferedImage(bitMatrix);
            writeToPath(image,FORMAT,file,logoPath);
            System.out.println("创建二维码完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //创建带logo，可设置前景、背景色的二维码
    public  void createQRImg(String QRContent,String filePath,String logoPath,int onColor,int offColor)
    {

        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARTSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(QRContent, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            Path file = new File(filePath).toPath();
            MatrixToImageConfig config=new MatrixToImageConfig(onColor,offColor);
            BufferedImage image = toBufferedImage(bitMatrix, config);
            writeToPath(image,FORMAT,file,logoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public  void createQRImg(String QRContent,String filePath)
    {
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARTSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(QRContent, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            Path file = new File(filePath).toPath();
            MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, file);
            System.out.println("创建二维码完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //使用颜色需要32位的ARGB,否则会生成失败，这里只要是因为使用了com.google.zxing.javase中的MatrixToImageConfig，使用的
    //javase中的MatrixToImageConfig类型自动得到的是ARGB类型导致的，这个类实现的有问题
    public  void createQRImg(String QRContent,String filePath,int groundColor,int foreColor)
    {
        HashMap<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARTSET);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(QRContent, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
            Path file = new File(filePath).toPath();
            MatrixToImageConfig config=new MatrixToImageConfig(foreColor,groundColor);
            MatrixToImageWriter.writeToPath(bitMatrix, FORMAT, file,config);
            System.out.println("创建二维码完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  Result getQRresult(String filePath) {
        /**
         * 如果用的jdk是1.9，需要配置下面这一行。
         */
        //System.setProperty("java.specification.version", "1.9");
        Result result = null;
        try {
            File file = new File(filePath);

            BufferedImage bufferedImage = ImageIO.read(file);
            BinaryBitmap bitmap = new BinaryBitmap(
                    new HybridBinarizer(new BufferedImageLuminanceSource(bufferedImage)));

            HashMap hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, CHARTSET);
            result = new MultiFormatReader().decode(bitmap, hints);
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
