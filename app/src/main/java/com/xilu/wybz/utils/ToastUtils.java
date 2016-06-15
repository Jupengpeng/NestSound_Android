package com.xilu.wybz.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.WindowManager;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.view.dialog.ToastDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

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
        }, 1000);
    }
    public static void toastLong(Context context, String text) {
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
        }, 2500);
    }
    public static void logingTip(Context context, String msg) {
        new MaterialDialog.Builder(context)
                .title(context.getResources().getString(R.string.progress_dialog))
                .content(msg)
                .positiveText("登录")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                }).negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                }).show();
    }
}