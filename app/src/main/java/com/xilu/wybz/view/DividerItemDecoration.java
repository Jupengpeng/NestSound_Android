package com.xilu.wybz.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView中实现divider功能
 * 只支持LinearLayoutManager
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


    private Context mContext;
    private Paint mPaint;
    private int mOrientation;
    private int mDivider = 5;
    private int mSpace = 0;
    private int mColor = Color.parseColor("#6fe2e2e2");


    public DividerItemDecoration(Context context, int orientation) {
        setOrientation(orientation);
        this.mContext = context;
    }
    public DividerItemDecoration(Context context, int orientation, int color) {
        setOrientation(orientation);
        this.mContext = context;
        this.mColor = color;
    }
    public DividerItemDecoration(Context context, int orientation, int divider, int color) {
        setOrientation(orientation);
        this.mContext = context;
        this.mDivider = divider;
        this.mColor = color;
    }
    public DividerItemDecoration(Context context, int orientation, int divider, int color, int space) {
        setOrientation(orientation);
        this.mContext = context;
        this.mDivider = divider;
        this.mColor = color;
        this.mSpace = space;
    }

    public void setDivider(){

    }

    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + mSpace;
        final int right = parent.getWidth() - parent.getPaddingRight() - mSpace;

//        final int childCount = parent.getChildCount() -1;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider;
            c.drawRect(left,top,right,bottom,mPaint);
        }
    }
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + mSpace;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() -mSpace;

//        final int childCount = parent.getChildCount() -1;
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getLeft() + params.leftMargin ;
            final int right = left + mDivider ;
            c.drawRect(left,top,right,bottom,mPaint);
        }
    }


    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST){
            drawVertical(c,parent);
        } else if (mOrientation == HORIZONTAL_LIST){
            drawHorizontal(c,parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider);
        } else {
            outRect.set(0, 0, mDivider, 0);
        }
    }

}
