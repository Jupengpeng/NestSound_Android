package com.xilu.wybz.view.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.ActionAdapter;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.KickBackAnimator;

import java.util.List;


public class MakeSongTipDialog extends Dialog{
    Context mContext;
    View.OnClickListener ipl;
    private Handler mHandler = new Handler();
    RelativeLayout layout;
    public MakeSongTipDialog(Context context, View.OnClickListener ipl) {
        super(context, R.style.ToastDialog);
        mContext = context;
        this.ipl = ipl;
        setCanceledOnTouchOutside(true);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }

    public View getDialogView() {
        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_publish, null);
        setContentView(layout);
        ImageView close = (ImageView) layout.findViewById(R.id.buttonFloat);
        TextView tv_zuoci = (TextView) layout.findViewById(R.id.tv_zuoci);
        TextView tv_zuoqu = (TextView) layout.findViewById(R.id.tv_zuoqu);
        TextView tv_record = (TextView) layout.findViewById(R.id.tv_record);
        tv_zuoci.setOnClickListener(ipl);
        tv_zuoqu.setOnClickListener(ipl);
        tv_record.setOnClickListener(ipl);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    dismiss();
                }
            }
        });
        return layout;
    }

    private void showAnimation(ViewGroup layout) {
        layout.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.rl_bottom) {
                continue;
            }
            child.setVisibility(View.INVISIBLE);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 800, 0);
                    fadeAnim.setDuration(400);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(200);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50);
        }
    }

    private void closeAnimation(final ViewGroup layout) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.rl_bottom) {
                continue;
            }
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 800);
                    fadeAnim.setDuration(400);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(200);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                    fadeAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            // TODO Auto-generated method stub
                        }
                    });
                }
            }, (layout.getChildCount() - i - 1) * 50);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        }, 200);
    }
    public void showDialog() {
        show();
//        showAnimation(layout);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount=0.4f;
        params.width = DensityUtil.getScreenW(mContext);
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
