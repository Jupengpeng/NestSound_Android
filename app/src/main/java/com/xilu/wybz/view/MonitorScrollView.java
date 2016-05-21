package com.xilu.wybz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ============================================================
 * <p/>
 * project name : TiantianFangFu
 * <p/>
 * copyright ZENG HUI (c) 2015
 * <p/>
 * author : HUI
 * <p/>
 * QQ ：240336124
 * <p/>
 * version : 1.0
 * <p/>
 * date created : On July, 2015
 * <p/>
 * description : 滚动位置监听的ScrollView
 * <p/>
 * revision history :
 * <p/>
 * ============================================================
 */
public class MonitorScrollView extends ScrollView {
    OnScrollListener mListener;

    public MonitorScrollView(Context context) {
        this(context, null);
    }

    public MonitorScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonitorScrollView(Context context, AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (t + getHeight() >= computeVerticalScrollRange()) {
            if (mListener != null)
                mListener.onBottom();
        }
        if (mListener != null)
            mListener.onScroll(t);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mListener = listener;
    }

    public interface OnScrollListener {
        void onScroll(int y);

        void onBottom();
    }
}
