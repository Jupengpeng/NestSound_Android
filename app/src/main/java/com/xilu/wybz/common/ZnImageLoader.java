package com.xilu.wybz.common;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xilu.wybz.R;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ZnImageLoader {

    static ZnImageLoader imageLoader;
    ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    public DisplayImageOptions options;
    public DisplayImageOptions playOptions;
    public DisplayImageOptions headOptions;

    ZnImageLoader() {
        initOpt();
    }

    public static ZnImageLoader getInstance() {
        if (imageLoader == null) {
            imageLoader = new ZnImageLoader();
        }
        return imageLoader;
    }

    void initOpt() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_pic)
                .showImageForEmptyUri(R.drawable.ic_default_pic)
                .showImageOnFail(R.drawable.ic_default_pic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        playOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_pressed2)
                .showImageForEmptyUri(R.drawable.bg_play_default)
                .showImageOnFail(R.drawable.bg_play_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        headOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_default_head_252)
                .showImageForEmptyUri(R.drawable.ic_default_head_252)
                .showImageOnFail(R.drawable.ic_default_head_252)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    public DisplayImageOptions getOptions(int res) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(res)
                .showImageForEmptyUri(res)
                .showImageOnFail(res)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        return options;
    }

    public void clearCache() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiscCache();
    }

    public void displayImage(String picUrl, DisplayImageOptions options, ImageView imgView) {
        ImageLoader.getInstance().displayImage(picUrl, imgView, options, animateFirstListener);
    }
    public void displayImage(String picUrl, ImageView imgView) {
        ImageLoader.getInstance().displayImage(picUrl, imgView, options, animateFirstListener);
    }
    public void displayHeadImage(String picUrl, ImageView imgView, int defaultPic) {
        ImageLoader.getInstance().displayImage(picUrl, imgView, getOptions(defaultPic), animateFirstListener);
    }

    static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
