package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.xilu.wybz.R;
import com.xilu.wybz.utils.DensityUtil;


public class LrcDraftDialog extends Dialog {

    Context context;

    public LrcDraftDialog(Context context) {
        super(context, R.style.CommentDialog);
        this.context = context;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.CENTER);

//        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }

    View getDialogView() {

        View rootView = LayoutInflater.from(context).inflate(
                R.layout.dialog_lrc_draft, null);
        return rootView;
    }


    public void showDialog() {
        show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = DensityUtil.getScreenW(context);
        //显示dialog的时候,就显示软键盘
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
        //就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(params);
    }

}
