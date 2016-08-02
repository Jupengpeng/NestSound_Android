package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckedTextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.HotFragment;
import butterknife.Bind;
import butterknife.OnClick;

public class MakeHotActivity extends ToolbarActivity {
    @Bind(R.id.iv_qc)
    SimpleDraweeView ivQc;
    @Bind(R.id.tv_new)
    CheckedTextView tvNew;
    @Bind(R.id.tv_hot)
    CheckedTextView tvHot;
    @Bind(R.id.id_stickynavlayout_viewpager)
    ViewPager container;
    private int currentIndex;
    private String[] mTitles = new String[]{"最新", "最热"};

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makehot;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setTitle("原唱伴奏");
        MyApplication.mMainService.doRelease();
        loadImage("res:///" + R.drawable.ic_qc_bg, ivQc);
        HotAdapter pagerAdapter = new HotAdapter(getSupportFragmentManager());
        container.setAdapter(pagerAdapter);
        container.setOffscreenPageLimit(mTitles.length);
        container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                changeTabColor();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.tv_new, R.id.tv_hot})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_new:
                if (currentIndex == 0) {
                    return;
                }
                currentIndex = 0;
                break;
            case R.id.tv_hot:
                if (currentIndex == 1) {
                    return;
                }
                currentIndex = 1;
                break;
        }
        changeTabColor();
        container.setCurrentItem(currentIndex);
    }

    private void changeTabColor() {
        tvHot.setChecked(currentIndex == 1);
        tvNew.setChecked(currentIndex == 0);
    }

    @OnClick(R.id.iv_qc)
    public void onClick() {
        TemplateBean templateBean = new TemplateBean();
        templateBean.id = "108";
        templateBean.title = "清唱";
        templateBean.mp3="http://7xsw6y.com2.z0.glb.qiniucdn.com/empty_hot_temp.mp3";
        templateBean.mp3times = 706;
        MakeSongActivity.ToMakeSongActivity(context,templateBean);
    }

    public class HotAdapter extends FragmentStatePagerAdapter {
        public HotAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return HotFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭播放
        PlayMediaInstance.getInstance().release();
    }


}
