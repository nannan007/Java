package com.qrcode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by qxp on 2018/8/30.
 */
public class GeneratorQRCodeRunable implements Runnable {
    String qrContent=null;
    String outPath=null;
    int onColor;
    int offColor;
    int type=1;
    String logoPath=null;
    public GeneratorQRCodeRunable(String content,String strPath)
    {
        this.type=1;
        this.qrContent=content;
        this.outPath=strPath;
    }

    public GeneratorQRCodeRunable(String content,String strPath,int onColor,int offColor)
    {
        this.type=2;
        this.qrContent=content;
        this.outPath=strPath;
        this.onColor=onColor;
        this.offColor=offColor;
    }

    public GeneratorQRCodeRunable(String content,String strPath,String logoPath)
    {
        this.type=3;
        this.qrContent=content;
        this.outPath=strPath;
        this.logoPath=logoPath;
    }

    public GeneratorQRCodeRunable(String content,String strPath,String logoPath,int onColor,int offColor)
    {
        this.type=4;
        this.qrContent=content;
        this.outPath=strPath;
        this.onColor=onColor;
        this.offColor=offColor;
        this.logoPath=logoPath;
    }

    @Override
    public void run() {

        QRCodeOperator qr=new QRCodeOperator();

        switch (this.type)
        {
            case 1:
            {
                qr.createQRImg(this.qrContent,this.outPath);
            }
            break;
            case 2:
            {
                qr.createQRImg(this.qrContent,this.outPath,this.onColor,this.offColor);
            }
            break;
            case 3:
            {
                qr.createQRImg(this.qrContent,this.outPath,this.logoPath);
            }
                break;
            case 4:
            {
                qr.createQRImg(this.qrContent,this.outPath,this.logoPath,this.onColor,this.offColor);
            }
            break;
            default:
            {
                qr.createQRImg(this.qrContent,this.outPath);
            }
                break;
        }
        System.out.println(Thread.currentThread().getName() + ":" + this.qrContent);
    }

}
