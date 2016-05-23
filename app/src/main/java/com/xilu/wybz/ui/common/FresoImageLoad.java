package com.xilu.wybz.ui.common;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by June on 16/4/24.
 */
public class FresoImageLoad {
    public static FresoImageLoad imageLoader;
    public FresoImageLoad() {

    }
    public static FresoImageLoad getInstance() {
        if (imageLoader == null) {
            imageLoader = new FresoImageLoad();
        }
        return imageLoader;
    }
    public void loadImage(String url, SimpleDraweeView mDraweeView) {
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
}
