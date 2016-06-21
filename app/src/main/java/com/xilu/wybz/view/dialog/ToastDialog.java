package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.utils.DensityUtil;


public class ToastDialog extends Dialog {
    public ToastDialog(Context context, String text) {
        super(context, R.style.ToastDialog);
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.app_msg, null);
        TextView tv_msg = (TextView) rootView.findViewById(android.R.id.message);
        tv_msg.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.actionbar_size)));
        tv_msg.setText(text);
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(rootView);
    }
}
