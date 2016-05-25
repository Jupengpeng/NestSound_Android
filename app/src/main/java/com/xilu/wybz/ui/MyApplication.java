package com.xilu.wybz.ui;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qiniu.android.storage.UploadManager;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.xilu.wybz.common.MyCommon;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by June on 2016/3/1.
 */
public class MyApplication extends Application {

    public static Context context;
    public static String musicId = "";
    public static String from;
    public static String id;
    public static List<String> ids;
    public static boolean isPlay;
    public static UploadManager uploadManager;


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ids = new ArrayList<>();
        //微信
        PlatformConfig.setWeixin(MyCommon.WECHAT_APP_ID, MyCommon.WECHAT_APP_SECRET);
        //新浪微博
        PlatformConfig.setSinaWeibo(MyCommon.SINA_APP_KEY, MyCommon.SINA_APP_SECRET);
        //QQ
        PlatformConfig.setQQZone(MyCommon.QQAppId, MyCommon.QQAppKey);
        uploadManager = new UploadManager();
        Fresco.initialize(this);
        initImageLoader(getApplicationContext());
        PushAgent.getInstance(context).setDebugMode(false);
    }
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        ImageLoader.getInstance().init(config.build());
    }
    public static MediaPlayer getMediaPlayer() {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(Context.class, cMediaTimeProvider, iSubtitleControllerListener);
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }

            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor", cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
            //Log.e("", "subtitle is setted :p");
        } catch (Exception e) {
        }
        return mediaplayer;
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}