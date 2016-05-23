package com.xilu.wybz.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xilu.wybz.R;


public class ToastDialog extends Dialog {
    Context context;
    public ToastDialog(Context context, String text) {
        super(context, R.style.ToastDialog);
        this.context = context;
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.ll_toast, null);
        TextView tv_msg = (TextView) rootView.findViewById(R.id.tv_msg);
        tv_msg.setText(text);
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(rootView);
    }

}
