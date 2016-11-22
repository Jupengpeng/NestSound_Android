package com.xilu.wybz.view;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.widget.OverScroller;

import com.xilu.wybz.ui.song.WaveSurfaceHelper;

/**
 * Created by Administrator on 2016/5/26.
 */
public class WaveSurfaceView extends SurfaceView {


    private WaveSurfaceHelper waveSurfaceHelper;

    private VelocityTracker mVelocityTracker;
    private OverScroller mScroller;

    private Context mContext;

    private boolean enableTouch = true;



    public WaveSurfaceView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public WaveSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public WaveSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    protected void init() {
        waveSurfaceHelper = new WaveSurfaceHelper(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    static float x1 = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (!enableTouch){
            return false;
        }

        int action = event.getAction() & MotionEvent.ACTION_MASK;
//        int minx = ViewConfiguration.get(mContext).getScaledTouchSlop();
        int minx = 2;


        float x2 = 0;
        float off;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
//                Log.d("sur","ACTION_DOWN:" + x1);
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = event.getX();
                off = x1 - x2;
                if (off > minx || off <-minx){
                    waveSurfaceHelper.setOffX((int)off);
                    x1 = x2;
                }

//                Log.d("sur","ACTION_MOVE:" + x2);
                break;
            case MotionEvent.ACTION_UP:
//                Log.d("sur","ACTION_UP:" );

                break;
            case MotionEvent.ACTION_CANCEL:
                break;

        }

        return true;
    }


    public boolean isEnableTouch() {
        return enableTouch;
    }

    public void setDisableTouch() {
        this.enableTouch = false;
    }

    public void setEnableTouch() {
        this.enableTouch = true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
    }

    private SavedState mSavedState;

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (mContext.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Some old apps reused IDs in ways they shouldn't have.
            // Don't break them, but they don't get scroll state restoration.
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mSavedState = ss;
        requestLayout();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        if (mContext.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // Some old apps reused IDs in ways they shouldn't have.
            // Don't break them, but they don't get scroll state restoration.
            return super.onSaveInstanceState();
        }
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
//        ss.scrollPosition = mScrollY;
        return ss;
    }


    static class SavedState extends BaseSavedState {
        public int scrollPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            scrollPosition = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(scrollPosition);
        }

        @Override
        public String toString() {
            return "HorizontalScrollView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " scrollPosition=" + scrollPosition + "}";
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }


    public WaveSurfaceHelper getWaveSurfaceHelper() {
        return waveSurfaceHelper;
    }

    public void setWaveSurfaceHelper(WaveSurfaceHelper waveSurfaceHelper) {
        this.waveSurfaceHelper = waveSurfaceHelper;
    }
}
