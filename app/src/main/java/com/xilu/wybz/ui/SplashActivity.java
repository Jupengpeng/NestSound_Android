package com.xilu.wybz.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.ViewPagerAdapter;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by June on 16/4/15.
 */
public class SplashActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.tv_tohome)
    TextView tv_toHome;
    @Bind(R.id.ll)
    LinearLayout ll;
    ArrayList<View> views;
    //定义ViewPager适配器
    ViewPagerAdapter vpAdapter;
    //引导图片资源
    final int[] pics = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};
    //底部小点的图片
    ImageView[] points;
    //记录当前选中位置
    int currentIndex;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PrefsUtil.getInt("isFirstOpen", context) == 0) {
            PrefsUtil.putInt("isFirstOpen", 1, context);
            try {
                FileUtils.delAllFile();
            } catch (Exception e) {
            }
            initData();
        } else {
            startActivity(WelActivity.class);
            overridePendingTransition(0, 0);
            finish();
        }
        tv_toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainTabActivity.class);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    public void initData() {
        views = new ArrayList<>();
        //定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        //初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        //实例化ViewPager适配器
        vpAdapter = new ViewPagerAdapter(views);
        //设置数据
        viewPager.setAdapter(vpAdapter);
        //设置监听
        viewPager.setOnPageChangeListener(this);

        //初始化底部小点
        initPoint();
    }

    /**
     * 初始化底部小点
     */
    void initPoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll);

        points = new ImageView[pics.length];

        //循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            //得到一个LinearLayout下面的每一个子元素
            points[i] = (ImageView) linearLayout.getChildAt(i);
            //默认都设为灰色
            points[i].setEnabled(true);
            //给每个小点设置监听
            points[i].setOnClickListener(this);
            //设置位置tag，方便取出与当前位置对应
            points[i].setTag(i);
        }

        //设置当面默认的位置
        currentIndex = 0;
        //设置为白色，即选中状态
        points[currentIndex].setImageResource(R.drawable.point_select);
        points[currentIndex].setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    /**
     * 设置当前页面的位置
     */
    public void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }
        viewPager.setCurrentItem(position);
    }

    /**
     * 设置当前的小点的位置
     */
    public void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }
        if (positon == pics.length - 1) {
            ll.setVisibility(View.GONE);
            tv_toHome.setVisibility(View.VISIBLE);
            tv_toHome.startAnimation(AnimationUtils.loadAnimation(context, android.R.anim.fade_in));
        } else {
            ll.setVisibility(View.VISIBLE);
            tv_toHome.setVisibility(View.GONE);
        }
        points[positon].setEnabled(false);
        points[positon].setImageResource(R.drawable.point_select);
        points[currentIndex].setEnabled(true);
        points[currentIndex].setImageResource(R.drawable.point_normal);
        currentIndex = positon;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //设置底部小点选中状态
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
