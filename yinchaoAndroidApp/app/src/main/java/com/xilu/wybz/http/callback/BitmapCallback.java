package com.xilu.wybz.http.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import okhttp3.Response;

/**
 * Created by June on 16/04/28.
 */
public abstract class BitmapCallback extends Callback<Bitmap> {
    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
