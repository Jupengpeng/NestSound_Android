package com.xilu.wybz.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/13.
 */
public class ThreadUtils {


    public static void schedule(TimerTask task){

        Timer timer = new Timer();
        timer.schedule(task, 1000);//开

//        timer.cancel();
    }



}
