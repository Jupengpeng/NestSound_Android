package com.xilu.wybz.common;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPool {

    static MyThreadPool threadPool;
    ThreadPoolExecutor threadPoolExe;

    MyThreadPool() {
        threadPoolExe = new ThreadPoolExecutor(
                3, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public static MyThreadPool getInstance() {
        if (threadPool == null) {
            threadPool = new MyThreadPool();
        }
        return threadPool;
    }


    public void doTask(Runnable run) {
        threadPoolExe.execute(run);
    }

}
