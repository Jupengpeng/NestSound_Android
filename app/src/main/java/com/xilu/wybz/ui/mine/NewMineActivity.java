package com.xilu.wybz.ui.mine;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.nineoldandroids.view.ViewHelper;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.FragmentsViewPagerAdapter;
import com.xilu.wybz.ui.fragment.BaseFragment;
import com.xilu.wybz.ui.fragment.RecycleViewFragment;
import com.xilu.wybz.view.StickyNavLayout;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hujunwei on 16/6/12.
 */
public class NewMineActivity extends AppCompatActivity {
    @Bind(R.id.id_stickynavlayout_viewpager)
    ViewPager viewPager;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.id_stick)
    StickyNavLayout stickyNavLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_stick);
        ButterKnife.bind(this);

        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(RecycleViewFragment.newInstance());
        fragments.add(RecycleViewFragment.newInstance());
        fragments.add(RecycleViewFragment.newInstance());

        FragmentsViewPagerAdapter adapter = new FragmentsViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);

        ViewHelper.setAlpha(floatingActionButton, 0f);
        stickyNavLayout.setOnStickStateChangeListener(onStickStateChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    };

    private boolean lastIsTopHidden;//记录上次是否悬浮
    private StickyNavLayout.onStickStateChangeListener onStickStateChangeListener = new StickyNavLayout.onStickStateChangeListener() {
        @Override
        public void isStick(boolean isStick) {
            if (lastIsTopHidden != isStick) {
                lastIsTopHidden = isStick;
                if (isStick) {
//                    Toast.makeText(SimpleStickActivity.this, "本宝宝悬浮了", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(SimpleStickActivity.this, "本宝宝又不悬浮了", Toast.LENGTH_LONG).show();
                    //初始化其他几个listview的位置 自动到顶部

                }
            }
        }

        @Override
        public void scrollPercent(float percent) {
            ViewHelper.setAlpha(floatingActionButton, percent);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
