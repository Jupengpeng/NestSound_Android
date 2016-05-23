package com.xilu.wybz.ui.http.callback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.xilu.wybz.http.callback.*;

import okhttp3.Response;

/**
 * Created by June on 16/04/28.
 */
public abstract class BitmapCallback extends com.xilu.wybz.http.callback.Callback<Bitmap> {
    @Override
    public Bitmap parseNetworkResponse(Response response) throws Exception {
        return BitmapFactory.decodeStream(response.body().byteStream());
    }

}
