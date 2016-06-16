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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.MainTabActivity;
import com.xilu.wybz.utils.FastBlur;
import com.xilu.wybz.utils.KickBackAnimator;

public class MoreWindow extends PopupWindow {

    private String TAG = MoreWindow.class.getSimpleName();
    Activity mContext;
    private Bitmap mBitmap = null;
    private Bitmap overlay = null;
    private Handler mHandler = new Handler();

    private ViewGroup parent;

    public MoreWindow(Activity context) {
        mContext = context;
    }
    RelativeLayout layout;
    public void init(Activity activity, ViewGroup parent, View anchor, OnClickListener onClickListener) {
        this.parent = parent;


        layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_publish, null);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        parent.addView(layout,params);


        ImageView close = (ImageView) layout.findViewById(R.id.buttonFloat);

        TextView tv_zuoci = (TextView) layout.findViewById(R.id.tv_zuoci);
        TextView tv_zuoqu = (TextView) layout.findViewById(R.id.tv_zuoqu);
        TextView tv_record = (TextView) layout.findViewById(R.id.tv_record);

        tv_zuoci.setOnClickListener(onClickListener);
        tv_zuoqu.setOnClickListener(onClickListener);
        tv_record.setOnClickListener(onClickListener);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnimation(layout);
            }
        });
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnimation(layout);
            }
        });
    }

    private Bitmap blur() {
//        if (null != overlay) {
//            return overlay;
//        }
        View view = mContext.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.destroyDrawingCache();
        view.buildDrawingCache(true);
        mBitmap = view.getDrawingCache();


//        mBitmap = Bitmap.createBitmap(mBitmap,0,50,mBitmap.getWidth(),mBitmap.getHeight()-50);


        float scaleFactor = 8;
        float radius = 30;
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();

        overlay = Bitmap.createBitmap((int) (width / scaleFactor), (int) (height / scaleFactor), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        overlay = FastBlur.doBlur(overlay, (int) radius, true);

//
//        Bitmap bitmap = Bitmap.createBitmap((int) (width ), (int) (height), Bitmap.Config.ARGB_8888);
//        canvas = new Canvas(bitmap);
//        canvas.scale(scaleFactor,scaleFactor);
//        paint = new Paint();
//        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
//        canvas.drawBitmap(overlay, 0, 0, paint);
//
//        bitmap = FastBlur.doGradual(bitmap, (int) (20));

        return overlay;
    }

    private Animation showAnimation1(final View view, int fromY, int toY) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
        go.setDuration(300);
        TranslateAnimation go1 = new TranslateAnimation(0, 0, -10, 2);
        go1.setDuration(100);
        go1.setStartOffset(250);
        set.addAnimation(go1);
        set.addAnimation(go);

        set.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
        return set;
    }
    public void showMoreWindow(MainTabActivity activity, View anchor, OnClickListener onClickListener) {
//        mActivity.setStatusColor(0x90000000);
        final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_publish, null);
        setContentView(layout);
        //设置SelectPicPopupWindow弹出窗体的宽
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //实例化一个ColorDrawable颜色为半透明
        setBackgroundDrawable(new BitmapDrawable());
        ImageView close = (ImageView) layout.findViewById(R.id.buttonFloat);
        TextView tv_zuoci = (TextView) layout.findViewById(R.id.tv_zuoci);
        TextView tv_zuoqu = (TextView) layout.findViewById(R.id.tv_zuoqu);
        TextView tv_record = (TextView) layout.findViewById(R.id.tv_record);
        tv_zuoci.setOnClickListener(onClickListener);
        tv_zuoqu.setOnClickListener(onClickListener);
        tv_record.setOnClickListener(onClickListener);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation(layout);
                }
            }
        });
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) {
                    closeAnimation(layout);
                }
            }
        });
        showAnimation(layout);
        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.TOP, 0, 0);
    }


    public void showto(){

        parent.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setFocusable(true);
        parent.setVisibility(View.VISIBLE);
        showAnimation(layout);

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
                            parent.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

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

    public void destroy() {
        if (null != overlay) {
            overlay.recycle();
            overlay = null;
            System.gc();
        }
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
            System.gc();
        }
    }
}
