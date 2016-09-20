package com.xilu.wybz.common;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xilu.wybz.R;
import com.xilu.wybz.common.interfaces.MyBaseImageListener;

public class MyImageLoader {

    static MyImageLoader imageLoader;
    DisplayImageOptions options;
    MyBaseImageListener imgListener;

    MyImageLoader(Context context) {
        initOpt(context);
    }

    public static MyImageLoader getInstance(Context context) {
        if (imageLoader == null) {
            imageLoader = new MyImageLoader(context);
        }
        return imageLoader;
    }

    void initOpt(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .showImageOnLoading(R.drawable.ic_default_pic)
                .showImageOnFail(R.drawable.ic_default_pic)
                .showImageForEmptyUri(R.drawable.ic_default_pic)
                .displayer(new RoundedBitmapDisplayer(0)).build();
    }

    public void clearCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
    }

    public void loadImage(ImageView picIv, String url) {
        ImageLoader.getInstance()
                .displayImage(url, picIv, options, imgListener);
    }

    public void loadImage(String url, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(url, listener);
    }

    public void loadImage(ImageView picIv, String url,
                          ImageLoadingListener listener) {
        ImageLoader.getInstance().displayImage(url, picIv, listener);
    }

}
