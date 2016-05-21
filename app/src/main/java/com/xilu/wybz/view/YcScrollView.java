package com.xilu.wybz.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 *
 *
 */
public class YcScrollView extends ScrollView {
    OnScrollListener onScrollListener;
    int lastScrollY;

    public YcScrollView(Context context) {
        this(context, null);
    }

    public YcScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YcScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            int scrollY = YcScrollView.this.getScrollY();
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
            }
            if (onScrollListener != null) {
                onScrollListener.onScroll(scrollY);
            }

        }

    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (onScrollListener != null) {
            onScrollListener.onScroll(lastScrollY = this.getScrollY());
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 5);
                break;
        }
        return super.onTouchEvent(ev);
    }

    public interface OnScrollListener {
        void onScroll(int scrollY);
    }

}
