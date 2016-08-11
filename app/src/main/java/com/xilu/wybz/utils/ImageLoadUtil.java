package com.xilu.wybz.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.squareup.picasso.Picasso;
import com.xilu.wybz.R;
import java.io.File;
/**
 * Created by June on 16/5/3.
 */
public class ImageLoadUtil {
    public static void loadImage(String url, SimpleDraweeView mDraweeView) {
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                        .setProgressiveRenderingEnabled(true)
                        .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeView.setController(controller);
    }

    public static void loadImage(String url, SimpleDraweeView mDraweeView, int width, int height) {
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                        .setResizeOptions(
                                new ResizeOptions(width, height))
                        .setProgressiveRenderingEnabled(true)
                        .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeView.setController(controller);
    }

    public static void loadImage(Context context, String url, ImageView imageView,int width, int height) {
        Picasso.with(context).load(url).placeholder(R.drawable.ic_default_pic).
                resize(width, height).centerCrop().into(imageView);
    }

    public static void loadHeadImage(Context context, String url, ImageView imageView,int width, int height) {
        Picasso.with(context).load(url).placeholder(R.drawable.ic_default_head_252).
                resize(width, height).centerCrop().into(imageView);
    }
    public static void loadImage(Context context, int res, ImageView imageView,int width, int height) {
        Picasso.with(context).load(res).placeholder(R.drawable.ic_default_pic).
                resize(width, height).centerCrop().into(imageView);
    }

    public static void loadImage(Context context, File file, ImageView imageView, int width, int height) {
        Picasso.with(context).load(file).placeholder(R.drawable.ic_default_pic).
                resize(width, height).centerCrop().into(imageView);
    }
}
