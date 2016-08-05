package com.xilu.wybz.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.R;
import com.xilu.wybz.utils.KickBackAnimator;

public class MoreWindow{

    public static String TAG = MoreWindow.class.getSimpleName();

    private Activity mContext;
    private Handler mHandler = new Handler();
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;

    ImageView layer;
    ImageView close;

    private ViewGroup parent;
    private RelativeLayout layout;


    public boolean isShow;

    ObjectAnimator toCloseAnim;
    ObjectAnimator toAddAnim;

    /**
     * MoreWindow.
     */
    public MoreWindow(){

    }

    /**
     * MoreWindow.
     * @param context
     * @param parent
     * @param onClickListener
     */
    public MoreWindow(Activity context, ViewGroup parent, OnClickListener onClickListener) {

        init(context,parent,onClickListener);
    }

    /**
     * init.
     * @param context
     * @param parent
     * @param onClickListener
     */
    public void init( Activity context, ViewGroup parent, OnClickListener onClickListener) {

        this.mContext = context;
        this.parent = parent;


//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_publish, null);
//        parent.addView(layout,params);

        layout = (RelativeLayout)parent.findViewById(R.id.windows_root);

        close = (ImageView) layout.findViewById(R.id.buttonFloat);
        layer = (ImageView) layout.findViewById(R.id.over_layer);

        TextView tv_zuoci = (TextView) layout.findViewById(R.id.tv_zuoci);
        TextView tv_zuoqu = (TextView) layout.findViewById(R.id.tv_zuoqu);
        TextView tv_record = (TextView) layout.findViewById(R.id.tv_record);

        onCreateActionAnim();

        tv_zuoci.setOnClickListener(onClickListener);
        tv_zuoqu.setOnClickListener(onClickListener);
        tv_record.setOnClickListener(onClickListener);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnimation();
            }
        });
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnimation();
            }
        });
    }

    private Bitmap blur() {

        View view = mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache(true);

        mBitmap = view.getDrawingCache();

        float scaleFactor = 8;
        int radius = 30;
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        mBitmap.recycle();

        overlay = NativeStackBlur.process(overlay,radius);

        return overlay;
    }

//
//    public void showMoreWindow(Activity activity, View anchor, OnClickListener onClickListener) {
//
//        final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_publish, null);
//        setContentView(layout);
//        //设置SelectPicPopupWindow弹出窗体的宽
//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        //设置SelectPicPopupWindow弹出窗体的高
//        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
//        //实例化一个ColorDrawable颜色为半透明
//        setBackgroundDrawable(new BitmapDrawable());
//        ImageView close = (ImageView) layout.findViewById(R.id.buttonFloat);
//        TextView tv_zuoci = (TextView) layout.findViewById(R.id.tv_zuoci);
//        TextView tv_zuoqu = (TextView) layout.findViewById(R.id.tv_zuoqu);
//        TextView tv_record = (TextView) layout.findViewById(R.id.tv_record);
//        tv_zuoci.setOnClickListener(onClickListener);
//        tv_zuoqu.setOnClickListener(onClickListener);
//        tv_record.setOnClickListener(onClickListener);
//        close.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isShowing()) {
//                    closeAnimation();
//                }
//            }
//        });
//        layout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isShowing()) {
//                    closeAnimation();
//                }
//            }
//        });
//        showAnimation();
//        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
//        setOutsideTouchable(true);
//        setFocusable(true);
//        showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
//    }


    public void showByAnimation(){

//        layer.setImageDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        layer.setBackground(new BitmapDrawable(mContext.getResources(), blur()));
        parent.setVisibility(View.VISIBLE);
        showAnimation();
    }
    public void closeByAnimation(){
        closeAnimation();
    }

    public void onCreateActionAnim() {
        toCloseAnim = ObjectAnimator.ofFloat(close, "rotation", 45, 90);
        toAddAnim = ObjectAnimator.ofFloat(close, "rotation", 90, 45);
    }
    public void moveToClose(){
        toCloseAnim.setDuration(300);
        toCloseAnim.setRepeatCount(0);
        toCloseAnim.setRepeatMode(ObjectAnimator.REVERSE);
        toCloseAnim.start();
    }
    public void moveToAdd(){
        toAddAnim.setDuration(300);
        toAddAnim.setRepeatCount(0);
        toAddAnim.setRepeatMode(ObjectAnimator.REVERSE);
        toAddAnim.start();
    }



    private void showAnimation() {
        isShow = true;
        layer.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in));

        moveToClose();

        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.rl_bottom) {
                continue;
            }
            if (child.getId() == R.id.over_layer) {
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



    public void closeAnimation() {
        isShow = false;
        layer.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_out));

        moveToAdd();

        for (int i = 0; i < layout.getChildCount(); i++) {
            final View child = layout.getChildAt(i);
            if (child.getId() == R.id.rl_bottom) {
                continue;
            }
            if (child.getId() == R.id.over_layer) {
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
                    fadeAnim.addListener(new AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            child.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }
                    });
                }
            }, (layout.getChildCount() - i - 2) * 50);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                parent.setVisibility(View.GONE);
            }
        }, 300);
    }




    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
