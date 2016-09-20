package com.xilu.wybz.ui;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class ExitApplication extends Application {

    List<Activity> activityList = new LinkedList();
    static ExitApplication instance;

    ExitApplication() {
    }

    //单例模式中获取唯一的ExitApplication实例
    public static ExitApplication getInstance() {
        if (null == instance) {
            instance = new ExitApplication();
        }
        return instance;

    }

    //添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }
    //遍历所有Activity并finish

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }
}