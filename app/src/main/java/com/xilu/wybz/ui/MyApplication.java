package com.xilu.wybz.ui;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.multidex.MultiDex;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qiniu.android.storage.UploadManager;
import com.sina.weibo.sdk.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.socialize.PlatformConfig;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.service.MainService;
import com.xilu.wybz.utils.PhoneInfoUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by June on 2016/3/1.
 */
public class MyApplication extends Application implements ServiceConnection {
    public MainService mMainService;
    public static Context context;
    public static String musicId = "";
    public static String from;
    public static String id;
    public static List<Integer> ids;
    public static Map<Integer, Integer> posMap;
    public static boolean isPlay;
    public static UploadManager uploadManager;
    public int userid;
    public boolean isLogin;
    public static MyApplication instance;

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static MyApplication getInstance() {
        if (instance == null) {
            instance = new MyApplication();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        ids = new ArrayList<>();
        posMap = new HashMap<>();
        //检查版本
        if (PrefsUtil.getInt("versionCode", context) == 0) {
            PrefsUtil.clearData(context);
        }
        PrefsUtil.putInt("versionCode", PhoneInfoUtil.getVersionCode(context), context);
        String url = PrefsUtil.getString("domain", this);
        if (StringUtil.isNotBlank(url)) {
            MyHttpClient.ROOT_URL = url;
            MyHttpClient.BASE_URL = MyHttpClient.ROOT_URL+MyHttpClient.BASE_PATH;
        }
        userid = PrefsUtil.getUserId(context);
        isLogin = userid > 0;
        Fresco.initialize(this);
        //Umeng分享
        PlatformConfig.setWeixin(MyCommon.WECHAT_APP_ID, MyCommon.WECHAT_APP_SECRET);//微信
        PlatformConfig.setSinaWeibo(MyCommon.SINA_APP_KEY, MyCommon.SINA_APP_SECRET);//新浪微博
        PlatformConfig.setQQZone(MyCommon.QQAppId, MyCommon.QQAppKey);//QQ
        //七牛上传
        uploadManager = new UploadManager();
        Fresco.initialize(this);
        initImageLoader(getApplicationContext());
        PushAgent.getInstance(context).setDebugMode(false);
        startMainService();
        bindMainService();
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

    public void startMainService() {
        Intent it = new Intent(this, MainService.class);
        startService(it);
    }

    public void stopMainService() {
        Intent it = new Intent(this, MainService.class);
        stopService(it);
    }

    private void bindMainService() {
        Intent it = new Intent(this, MainService.class);
        this.bindService(it, this, Service.BIND_AUTO_CREATE);
    }

    private void unbindMainService() {
        this.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (service instanceof MainService.ServiceBinder) {
            MainService.ServiceBinder binder = (MainService.ServiceBinder) service;
            mMainService = binder.getService();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    public MainService getMainService() {
        return mMainService;
    }
}