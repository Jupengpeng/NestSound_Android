package com.xilu.wybz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.xilu.wybz.R;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.GeneralUtil;

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
 * description : 滚动控件头部悬浮的ScrollView
 * <p/>
 * revision history :
 * <p/>
 * ============================================================
 */
public class TopFloatScrollView extends FrameLayout implements MonitorScrollView.OnScrollListener {
    // 新增的放置悬浮的容器
    LinearLayout mAddFloatContainer;
    Context mContext;
    // 滚动的内容View
    MonitorScrollView mContainerSv;
    // ScrollView 的根内容
    ViewGroup mRootView;
    // 悬浮的View
    View mFloatView;
    View mTopbarView;
    // 悬浮View 在父View中的位置
    int mFloatViewPotion;
    // 悬浮View距父View头部的距离
    int mFloatToTopHeight;
    int dpi48;
    // 一直在头部的容器

    RelativeLayout mFloatTopContainer;
    OnScrollToBottomListener onScrollToBottom;


    View rl_top_view;
    View iv_left_top;

    public void initView(View rl_top_view, View iv_left_top) {
        this.rl_top_view = rl_top_view;
        this.iv_left_top = iv_left_top;
    }

    public TopFloatScrollView(Context context) {
        this(context, null);
    }

    public TopFloatScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopFloatScrollView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    void initView() {
        // 添加滚动的ScrollView
        mContainerSv = new MonitorScrollView(mContext);
        mContainerSv.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        dpi48 = DensityUtil.dip2px(mContext, 48);
        mContainerSv.setOnScrollListener(this);
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mContainerSv.setLayoutParams(params);
        super.addView(mContainerSv);

        // 添加头部容器
        mFloatTopContainer = new RelativeLayout(mContext);
        super.addView(mFloatTopContainer);
        mFloatTopContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 3) {
            // 只容许在xml中配置一个ChildView
            throw new IllegalStateException(
                    "TopFloatScrollView can host only one direct child");
        }
        initInnerView();
    }

    /**
     * 初始化内部的View
     */
    void initInnerView() {
        // 判断是不是只有一个ChildView
        int childCount = this.getChildCount();

        if (childCount == 2) {
            // 刚开始可能没有在layout中设置子View，需要在代码中动态添加
            return;
        }

        // 获取ScrollView的内容
        mRootView = (ViewGroup) this.getChildAt(2);

        if (mRootView == null) {
            return;
        }

        // 防止界面在填充数据的时候自己滚动
        mRootView.setFocusable(true);
        mRootView.setFocusableInTouchMode(true);

        // 先从this里面移除
        this.removeView(mRootView);
        // 添加到ScrollView里面
        mContainerSv.addView(mRootView);

        // 获取悬浮的View
        mFloatView = mRootView.findViewById(R.id.top_float);

        if (mFloatView == null) {
            throw new RuntimeException(
                    "Can't find the float View for id is top_float");
        }

    }

    /**
     * 把悬浮View放到新增容器中然后把新增容器放到主布局中
     */
    void initAddFloatContainerView() {
        if (mFloatView != null) {
            GeneralUtil.measureView(mFloatView);

            final int height = mFloatView.getHeight() == 0 ? mFloatView
                    .getMeasuredHeight() : mFloatView.getHeight();

            // 获取需要悬浮的View到这个顶部的高度
            mFloatToTopHeight = mFloatView.getTop();

            mRootView.removeView(mFloatView);

            // 存放float
            mAddFloatContainer = new LinearLayout(mContext);
            mAddFloatContainer.addView(mFloatView);
            mRootView.addView(mAddFloatContainer, mFloatViewPotion);

            // 设置临时高度
            mAddFloatContainer.getLayoutParams().height = height;

            mFloatTopContainer.getLayoutParams().height = height + dpi48;
            // 之所以要设置背景是因为快速向上滑动的时候会出现闪现的情况
//			mFloatTopContainer.setBackground(BitmapUtils
//					.bitmap2Drawable(BitmapUtils.getBitmapFromView(mFloatView)));
        }
    }

    /**
     * 找到FloatView在这个RootView中的位置
     */
    void findFloatViewPosition() {
        int count = mRootView.getChildCount();
        for (int position = 0; position < count; position++) {
            View childView = mRootView.getChildAt(position);
            if (childView == mFloatView) {
                mFloatViewPotion = position;
                break;
            }
        }
    }

    @Override
    public void onScroll(int y) {
        if (y >= 0 && y <= 255) {
            float alpha = y / 255f;
            rl_top_view.setAlpha(alpha);
        } else if (y > 255) {
            rl_top_view.setAlpha(1);
        }
        iv_left_top.setAlpha(rl_top_view.getAlpha());
        if (mFloatView != null && mFloatView.getVisibility() == View.VISIBLE) {
            // 在它滚动的时候去决定新增容器有多大
            initNewContanier();
            if (y > mFloatToTopHeight - dpi48) {
                addFloatToContainer();
            } else {
                addFloatToRootView();
            }
        }
    }

    @Override
    public void onBottom() {
        if (onScrollToBottom != null){
            onScrollToBottom.scrollBottom();
        }
    }

    void initNewContanier() {
        if (mFloatView.getVisibility() == View.VISIBLE && mAddFloatContainer == null) {
            // 找到FloatView在这个RootView中的位置
            findFloatViewPosition();
            // 把悬浮View放到新增容器中然后把新增容器放到主布局中
            initAddFloatContainerView();
        }
    }

    /**
     * 添加到原来的RooView中
     */
    void addFloatToRootView() {
        ViewParent currentParent = mFloatView.getParent();
        if (currentParent != mAddFloatContainer) {
            // 添加到原来的RooView中 我们之前有记录位置
            mFloatTopContainer.removeView(mFloatView);

            ViewGroup parent = (ViewGroup)mFloatView.getParent();
            if (parent != null){
                parent.removeView(mFloatView);
            }
            mAddFloatContainer.addView(mFloatView);
            mFloatTopContainer.setVisibility(View.GONE);
        }
    }

    /**
     * 添加悬浮到这个准备好的容器中
     */
    void addFloatToContainer() {
        ViewParent currentParent = mFloatView.getParent();
        if (currentParent != mFloatTopContainer) {
            // 添加到容器中
            mFloatTopContainer.setVisibility(View.VISIBLE);
            mFloatTopContainer.setPadding(0, dpi48, 0, 0);
            mAddFloatContainer.removeAllViews();

            ViewGroup g = (ViewGroup)mFloatView.getParent();
            if (g != null){
                g.removeView(mFloatView);
            }
            mFloatTopContainer.addView(mFloatView);
        }
    }

    /************************
     * 重载所有的addView()方法
     ************************/

    @Override
    public void addView(View child) {
        judgeChildCount();
        super.addView(child);
        initInnerView();
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        judgeChildCount();
        super.addView(child, params);
        initInnerView();
    }

    @Override
    public void addView(View child, int index) {
        judgeChildCount();
        super.addView(child, index);
        initInnerView();
    }

    @Override
    public void addView(View child, int index,
                        ViewGroup.LayoutParams params) {
        judgeChildCount();
        super.addView(child, index, params);
        initInnerView();
    }

    @Override
    public void addView(View child, int width, int height) {
        judgeChildCount();
        super.addView(child, width, height);
        initInnerView();
    }

    /**
     * 判断子View的个数
     */
    void judgeChildCount() {
        if (getChildCount() > 3 || mContainerSv.getChildCount() == 1) {
            // 只容许在xml中配置一个ChildView
            throw new IllegalStateException(
                    "TopFloatScrollView can host only one direct child");
        }
    }


    public void setScrollBottomListener(OnScrollToBottomListener scrollBottomListener) {
        onScrollToBottom = scrollBottomListener;
    }

    public interface OnScrollToBottomListener {
        void scrollBottom();
    }

}
