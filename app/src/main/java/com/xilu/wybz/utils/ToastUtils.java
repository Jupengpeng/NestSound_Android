package com.xilu.wybz.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.view.dialog.ToastDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

public class ToastUtils {
    public static void toast(Context context, String msg, int type) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_toast, null);
        TextView tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        tvMsg.setText(msg);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, DensityUtil.dip2px(context,70));
        toast.setDuration(type==0?Toast.LENGTH_SHORT:Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
    public static void toast(Context context, String msg) {
        toast(context,msg,0);
    }
    public static void toastLong(Context context, String msg) {
        toast(context,msg,1);
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