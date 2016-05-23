package com.xilu.wybz.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/8.
 */
public abstract class WyBaseAdapter<T> extends android.widget.BaseAdapter {
    protected Context context;
    protected List<T> mList;
    protected int screenWidth;
    protected int type;
    protected String name;

    public WyBaseAdapter(Context context, List<T> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.context = context;
        this.mList = list;
        initData();
    }

    public WyBaseAdapter(Context context, List<T> list, int type) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.context = context;
        this.mList = list;
        this.type = type;
        initData();
    }

    public WyBaseAdapter(Context context, List<T> list, String name) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.context = context;
        this.mList = list;
        this.name = name;
        initData();
    }

    void initData() {
        screenWidth = DensityUtil.getScreenW(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    protected void loadImage(String image, ImageView imageView) {
        ZnImageLoader.getInstance().displayImage(image, imageView);
    }

    protected void loadHeadImage(String image, ImageView imageView, int res) {
        ZnImageLoader.getInstance().displayHeadImage(image, imageView, res);
    }

    protected void loadImage(int res, ImageView imageView) {
        ZnImageLoader.getInstance().displayImage("drawable://" + res, imageView);
    }

    protected void loadImage(File file, ImageView imageView) {
        ZnImageLoader.getInstance().displayImage("file:///" + file.getAbsolutePath(), imageView);
    }

    protected void loadImage(String url, SimpleDraweeView mDraweeView, int itemWidth, int itemHeight) {
        Uri imageUri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .setResizeOptions(new ResizeOptions(itemWidth, itemHeight))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setImageRequest(request)
                .build();
        mDraweeView.setController(controller);
    }

    protected void showMsg(String msg) {
        ToastUtils.toast(context, msg);
    }
}
