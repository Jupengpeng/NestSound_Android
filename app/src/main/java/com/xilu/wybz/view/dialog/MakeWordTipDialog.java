package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.utils.DensityUtil;


public class MakeWordTipDialog extends Dialog{
    Context mContext;
    RelativeLayout layout;
    int count;
    public MakeWordTipDialog(Context context) {
        super(context, R.style.ToastDialog);
        mContext = context;
        setCanceledOnTouchOutside(false);
        setCancelable(false);
//        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }

    public View getDialogView() {
        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.ll_makeword_tip, null);
        setContentView(layout);
//        RelativeLayout rlMain = (RelativeLayout) layout.findViewById(R.id.rl_main);
        RelativeLayout rlLeft = (RelativeLayout) layout.findViewById(R.id.rl_import);
        RelativeLayout rlRight = (RelativeLayout) layout.findViewById(R.id.rl_edit);

        ImageView ivLeft = (ImageView) layout.findViewById(R.id.flow_cover_left);
        ivLeft.setPadding(DensityUtil.getScreenW(mContext)*4/36,0,0,DensityUtil.dip2px(mContext,28));

        ImageView ivRight = (ImageView) layout.findViewById(R.id.flow_cover_right);
        ivRight.setPadding(0,0,DensityUtil.getScreenW(mContext)*9/36,DensityUtil.dip2px(mContext,28));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count==0) {
                    count++;
                    ivLeft.setVisibility(View.GONE);
                    ivRight.setVisibility(View.VISIBLE);
                    rlLeft.setVisibility(View.INVISIBLE);
                    rlRight.setVisibility(View.VISIBLE);
                }else{
                    dismiss();
                }
            }
        });
        return layout;
    }


    public void showDialog() {
        show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount=0.2f;
        params.width = DensityUtil.getScreenW(mContext);
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

}
