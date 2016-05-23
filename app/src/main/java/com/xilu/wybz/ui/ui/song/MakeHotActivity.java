package com.xilu.wybz.ui.ui.song;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.BanzouFragment;
import com.xilu.wybz.view.CustomViewPager;
import com.xilu.wybz.view.TopScrollView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/5/6.
 */
public class MakeHotActivity extends ToolbarActivity {
    @Bind(R.id.iv_qc)
    SimpleDraweeView iv_qc;
    @Bind(R.id.tv_new)
    CheckedTextView tv_new;
    @Bind(R.id.tv_hot)
    CheckedTextView tv_hot;
    @Bind(R.id.viewPager)
    CustomViewPager mPager;
    @Bind(R.id.myScrollView)
    TopScrollView mScrollView;
    MyFragmentPagerAdapter adapter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makehot;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
    }
    public void initView() {
        setTitle("原唱伴奏");
        loadImage("res://x/" + R.drawable.ic_qc_bg, iv_qc);
    }
    public void initEvent() {
        mScrollView.setScrollBottomListener(new TopScrollView.OnScrollToBottomListener() {
            @Override
            public void scrollBottom() {
                if (adapter.getFragment(mPager.getCurrentItem()) != null) {
                    adapter.getFragment(mPager.getCurrentItem()).loadMore();
                }
            }
        });
    }
    @OnClick({R.id.iv_qc, R.id.tv_new, R.id.tv_hot, R.id.rl_right})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.rl_right:
                startActivity(SearchHotActivity.class);
                break;
            case R.id.iv_qc:
                Intent intent = new Intent(context, MakeHotActivity.class);
                intent.putExtra("id", "108");
                intent.putExtra("name", "清唱");
                intent.putExtra("url", "http://7xru8x.com2.z0.glb.qiniucdn.com/empty.mp3");
                intent.putExtra("times", "0");
                startActivity(intent);
                break;
            case R.id.tv_new:
                if (mPager.getCurrentItem() == 1) {
                    mPager.setCurrentItem(0);
                    tv_new.setChecked(true);
                    tv_hot.setChecked(false);
                }
                break;
            case R.id.tv_hot:
                if (mPager.getCurrentItem() == 0) {
                    mPager.setCurrentItem(1);
                    tv_new.setChecked(false);
                    tv_hot.setChecked(true);
                }
                break;
        }
    }

    public void initData() {
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setCurrentItem(0);
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        Map<Integer, BanzouFragment> mPageReferenceMap = new HashMap<>();

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BanzouFragment f = (BanzouFragment) super.instantiateItem(container, position);
            f.setType(position);
            return f;
        }

        @Override
        public Fragment getItem(int position) {
            BanzouFragment banzouFragment = new BanzouFragment();
            mPageReferenceMap.put(position, banzouFragment);
            return banzouFragment;
        }

        public BanzouFragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            if (arg0 == 0) {
                tv_new.setChecked(true);
                tv_hot.setChecked(false);
            } else {
                tv_new.setChecked(false);
                tv_hot.setChecked(true);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
