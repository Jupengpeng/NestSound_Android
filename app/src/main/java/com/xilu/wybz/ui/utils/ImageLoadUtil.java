package com.xilu.wybz.ui.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.xilu.wybz.R;

/**
 * Created by June on 16/5/3.
 */
public class ImageLoadUtil {
    public static void loadImage(String url, SimpleDraweeView mDraweeView) {
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                        .setResizeOptions(
                                new ResizeOptions(mDraweeView.getLayoutParams().width, mDraweeView.getLayoutParams().height))
                        .setProgressiveRenderingEnabled(true)
                        .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeView.setController(controller);
    }
    public static void loadImage(Context context, String url, ImageView imageView, int width, int height) {
        DrawableRequestBuilder requestBuilder = Glide.with(context).load(url);
        if (width > 0) requestBuilder.override(width, height);
        requestBuilder.placeholder(R.drawable.ic_default_pic);
        requestBuilder.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestBuilder.into(imageView);
    }
}
