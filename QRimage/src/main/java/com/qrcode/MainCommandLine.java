package com.qrcode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import  com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Integer.parseUnsignedInt;


/**
 * Created by qxp on 2018/8/29.
 */
public class MainCommandLine {
    //当使用bmp格式时，MatrixToImageConfig ARGB模式，设置的oncolor\offcolor A必须是FF,否者需要设置为png.
    public static final String FORMAT = "bmp";
    public static final String CHARTSET = "utf-8";
    public static final int WIDTH = 300;
    public static final int HEIGHT = 300;

    public static void run() {
        Runtime runtime = Runtime.getRuntime();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(runtime.exec("ipconfig").getInputStream()));
            String line = null;
            StringBuffer b = new StringBuffer();
            while ((line = br.readLine()) != null) {
                b.append(line + "\n");
            }
            System.out.println(b.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args)
    {
        String inputFile=null;
        String outputPath=null;
        String withImagePath=null;
        String withImageFlag=null;
        String groundColor=null;
        String imageColor=null;
        String logoPath=null;

        for(String s: args)
        {
            String[] splints=s.split("=");
            if(splints!=null && splints.length==2 )
            {
               // System.out.println(s);
                if(splints[0].equalsIgnoreCase("iPath") && splints[1].length()>0)//输入文件路径
                {
                    inputFile=splints[1];
                    //System.out.println("inputFile="+inputFile);
                    continue;
                }

                if(splints[0].equalsIgnoreCase("oPath") && splints[1].length()>0)//输出文件路径
                {
                    outputPath=splints[1];
                   // System.out.println("outputPath="+outputPath);
                        continue;
                }

                if(splints[0].equalsIgnoreCase("imgFlag") && splints[1].length()>0)//输出文件路径
                {
                    withImageFlag=splints[1];
                   // System.out.println("withImageFlag="+withImageFlag);
                    continue;
                }

                if(splints[0].equalsIgnoreCase("imgPath") && splints[1].length()>0)//输出文件路径
                {
                    withImagePath=splints[1];
                    //System.out.println("withImagePath="+withImagePath);
                    continue;
                }
                if(splints[0].equalsIgnoreCase("groundColor") && splints[1].length()>0)//输出文件路径
                {
                    groundColor=splints[1];
                   // System.out.println("groundColor="+groundColor);
                    continue;
                }

                if(splints[0].equalsIgnoreCase("logoPath") && splints[1].length()>0)//输出文件路径
                {
                    logoPath = splints[1];
                    System.out.println("logoPath="+logoPath);
                }


                if(splints[0].equalsIgnoreCase("color") && splints[1].length()>0)//输出文件路径
                {
                    imageColor = splints[1];
                    System.out.println("imageColor="+imageColor);
                }

            }
        }

        if(inputFile==null)
        {
            System.out.println("请输入二维码内容文件路径，每行一个二维码的内容");
            return;
        }

        //当输出路径为空时，默认输出当当前路径当/qrimg下面
        if(outputPath==null)
        {
            File directory = new File("");//设定为当前文件夹
            try{
                outputPath=directory.getAbsolutePath()+System.getProperty("file.separator");//获取绝对路径
                System.out.println("outputPath"+outputPath);
            }catch(Exception e){}


        }

        //读取文件内容，按照行将每行的内容生成二维码图片，并且保存，保存时以内容命名二维码图片，图片存在outputPath中
        File file=new File(inputFile);
        BufferedReader reader=null;
        try
        {
            reader=new BufferedReader(new FileReader(file));
            String content=null;
            if(logoPath!=null)
            {
                if(imageColor!=null && groundColor!=null)
                {
                    int onColor=Integer.parseUnsignedInt(imageColor,16);
                    int offColor=Integer.parseUnsignedInt(groundColor,16);
                    while((content=reader.readLine())!=null)
                    {
                        if(content.length()>0) {
                            //创建QR
                            String qrPath=outputPath+content+"."+FORMAT;
                            GeneratorQRCodeRunable runable = new GeneratorQRCodeRunable(content,qrPath,logoPath,onColor,offColor);
                            QRService.getInstance().submit(runable);
                        }
                    }
                }
                else
                {
                    while((content=reader.readLine())!=null)
                    {
                        if(content.length()>0) {
                            //创建QR
                            String qrPath=outputPath+content+"."+FORMAT;
                            GeneratorQRCodeRunable runable = new GeneratorQRCodeRunable(content,qrPath,logoPath);
                            QRService.getInstance().submit(runable);
                        }
                    }
                }

            }else
            {
                if(imageColor!=null && groundColor!=null)
                {
                    int onColor=Integer.parseUnsignedInt(imageColor,16);
                    int offColor=Integer.parseUnsignedInt(groundColor,16);
                    while((content=reader.readLine())!=null)
                    {
                        if(content.length()>0) {
                            //创建QR
                            String qrPath=outputPath+content+"."+FORMAT;
                            GeneratorQRCodeRunable runable = new GeneratorQRCodeRunable(content,qrPath,onColor,offColor);
                            QRService.getInstance().submit(runable);
                        }
                    }
                }
                else
                {
                    while((content=reader.readLine())!=null)
                    {
                        if(content.length()>0) {
                            //创建QR
                            String qrPath=outputPath+content+"."+FORMAT;
                            GeneratorQRCodeRunable runable = new GeneratorQRCodeRunable(content,qrPath);
                            QRService.getInstance().submit(runable);
                        }
                    }
                }
            }


        }
        catch(IOException e)
        {
            //e.printStackTrace();
            System.out.println("文件异常："+e.getMessage());
        }
        finally
        {
            try {
                reader.close();
            }catch (IOException e)
            {

            }
            QRService.getInstance().shutdown();
        }








//
//      String content="http://test.yuyuebus.com/dist/setOfTicket/index.html";
//      String filePath=outputPath+"yuyebus."+FORMAT;
//        MainCommandLine commandLine=new MainCommandLine();
////        int a=0xff0000ff;
////        int b=0xffff0000;
////        System.out.println("a:"+a+" "+Integer.toHexString(a)+" b:"+b+" "+Integer.toHexString(b) );
////        int c=0xffff01;
////        int d=0xffff;
////        System.out.println("c:"+c+" "+Integer.toHexString(c)+" b:"+d+" "+Integer.toHexString(d) );
//        int onColor=Integer.parseUnsignedInt(imageColor,16);
//        int offColor=Integer.parseUnsignedInt(groundColor,16);
//        System.out.println("groudcolor="+offColor+"  imgColor="+onColor);
//        //commandLine.createQRImg(content,filePath);
//      //  commandLine.createQRImg(content,filePath,onColor,offColor);
//       // commandLine.createQRImg(content,filePath,a,b);
//       /* filePath="/Users/qxp/Desktop/1535534569.png";
//        Result result=commandLine.getQRresult(filePath);
//        System.out.println("二维码识别结果:"+result.getText());*/
//
//      //  int ileng=10000;
//      //  String[] contents=new String[ileng];


//        String contentOrg="yycx_dfjdkfdk";
//        String qrPath=outputPath;
//        String logoPath="/Users/qxp/Desktop/aa.jpg";
//        for(int i=0;i<1000;i++)
//        {
//            String conContent=contentOrg+i;
//            String qrImgPathFile=qrPath+conContent+"."+FORMAT;
//            GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile);
//            //GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,onColor,offColor);
//            //GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath);
//            // GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath,onColor,offColor);
//            QRService.getInstance().submit(runable);
//        }
//        contentOrg="yycx_111111111FK";
//        for(int i=0;i<1000;i++)
//        {
//            String conContent=contentOrg+i;
//            String qrImgPathFile=qrPath+conContent+"."+FORMAT;
//            //GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile);
//             GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,onColor,offColor);
//            //GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath);
//            // GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath,onColor,offColor);
//            QRService.getInstance().submit(runable);
//        }
//
//        contentOrg="yycx_13333333333TT";
//        for(int i=0;i<1000;i++)
//        {
//            String conContent=contentOrg+i;
//            String qrImgPathFile=qrPath+conContent+"."+FORMAT;
//            //GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile);
//           // GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,onColor,offColor);
//            GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath);
//           // GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath,onColor,offColor);
//            QRService.getInstance().submit(runable);
//        }
//
//        contentOrg="yy_232328738KD";
//        for(int i=0;i<1000;i++)
//        {
//            String conContent=contentOrg+i;
//            String qrImgPathFile=qrPath+conContent+"."+FORMAT;
//            //GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile);
//            // GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,onColor,offColor);
//            //GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath);
//             GeneratorQRCodeRunable runable=new GeneratorQRCodeRunable(conContent,qrImgPathFile,logoPath,onColor,offColor);
//            QRService.getInstance().submit(runable);
//        }


//        String logoPath="/Users/qxp/Desktop/aa.jpg";
//        commandLine.createQRImg(content,filePath,logoPath,onColor,offColor);
//        filePath=outputPath+"yuyebus1."+FORMAT;
//        commandLine.createQRImg(content,filePath,logoPath);

    }



    public static void writeToPath2(BufferedImage image,String format,Path path,String logoPath) throws IOException {
        Graphics2D gs = image.createGraphics();

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
        if(!ImageIO.write(image, format, path.toFile())){
            throw new IOException("Could not write an image of format " + format + " to " + path);
        }
    }

    public static void writeToPath(BufferedImage image,String format,Path path,String logoPath) throws IOException {

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
