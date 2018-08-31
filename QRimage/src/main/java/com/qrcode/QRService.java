package com.qrcode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Runtime.getRuntime;

/**
 * Created by qxp on 2018/8/30.
 */
public class QRService {
    public static QRService instance = new QRService();
    ExecutorService pool =null;

    public static QRService getInstance(){
        return instance;
    }

    public QRService()
    {
        //根据系统资源设置固定线程数
        int fixed=Runtime.getRuntime().availableProcessors();
        System.out.println("fixed:"+fixed);
        pool= Executors.newFixedThreadPool(fixed);
    }

    //添加线程
    public void submit(Runnable run)
    {
        if(run!=null) {
            pool.execute(run);
        }
    }

    //结束线程池
    public void shutdown()
    {
        pool.shutdown();
        System.out.println("shutdown");
    }
}
