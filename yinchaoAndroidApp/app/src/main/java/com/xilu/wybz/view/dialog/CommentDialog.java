package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.utils.DensityUtil;


public class CommentDialog extends Dialog implements View.OnClickListener {

    Context context;
    EditText inputEt;
    RelativeLayout rl_send;
    ImageView sendIv;
    ICommentListener icl;

    public CommentDialog(Context context, ICommentListener icl) {
        super(context, R.style.CommentDialog);
        this.context = context;
        this.icl = icl;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);

        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }

    View getDialogView() {

        View rootView = LayoutInflater.from(context).inflate(
                R.layout.dialog_comment, null);
        inputEt = (EditText) rootView.findViewById(R.id.comment_et_input);
        rl_send = (RelativeLayout) rootView.findViewById(R.id.rl_send);
        sendIv = (ImageView) rootView.findViewById(R.id.comment_iv_send);

        rl_send.setOnClickListener(this);
        inputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                rl_send.setBackgroundResource(s.length() > 0 ? R.drawable.btn_send : R.drawable.btn_send2);
            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_send:
                if (icl != null) {
                    icl.toSend(inputEt.getText().toString());
                }
                break;
        }
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

    public void cleanData() {
        inputEt.setText("");
    }

    public interface ICommentListener {
        void toSend(String comment);
    }

}
