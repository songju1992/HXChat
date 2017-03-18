package com.app.song.hxchat.utils;

import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Handler;

/**
 * Created by song on 2017/3/15 20:24
 * 通过线程池工具类来封装newThread
 */


public class ThreadUtils {
    private static android.os.Handler sHandler = new android.os.Handler(Looper.getMainLooper());

    private static Executor sExecutor = Executors.newSingleThreadExecutor();//单线程的线程池（在此牵扯不到并发情况，所以使用单线程）
    //    private static Executor sExecutor = Executors.newFixedThreadPool(3);//3个线程的线程池
    public static void runOnSubThread(Runnable runnable){
        sExecutor.execute(runnable);
    }

    public static void runOnMainThread(Runnable runnable){
        sHandler.post(runnable);
    }
}
