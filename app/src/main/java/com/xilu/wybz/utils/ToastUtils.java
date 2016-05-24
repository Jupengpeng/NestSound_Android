package com.xilu.wybz.utils;

import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;

import com.xilu.wybz.view.dialog.ToastDialog;

public class ToastUtils {
    public static void toast(Context context, String text) {
        final ToastDialog toastDialog = new ToastDialog(context, text);
        if (!toastDialog.isShowing()) {
            toastDialog.show();
            WindowManager.LayoutParams params = toastDialog.getWindow().getAttributes();
            params.width = DensityUtil.getScreenW(context);
            toastDialog.getWindow().setAttributes(params);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toastDialog.dismiss();
            }
        }, 1200);

    }
}