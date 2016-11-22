package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.view.SystemBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CooperationActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private List<Fragment> fragments = new ArrayList<>();
    @Bind(R.id.tablayout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.activity_cooperation)
    LinearLayout mAppbar;
    @Bind(R.id.iv)
    ImageView iv;
    public boolean isFirst;
    CooperationPageAdapter adapter;
    CooperationFragment cooperationfragment;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cooperation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isFirst) return;
        else isFirst = true;
        adapter = new CooperationPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(this);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
        mAppbar.setPadding(0, SystemBarHelper.getStatusBarHeight(this), 0, 0);

    }

    @OnClick(R.id.llayout)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llayout:
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (position) {
                    case 1:
                            MineFragment mineFragment = (MineFragment) adapter.getFragment(1);
                            if (mineFragment != null) {
//                                mineFragment.initPresenter();
                            }

                        break;
                    case 2:

                            CollectFragment collectFragment = (CollectFragment) adapter.getFragment(2);
                            if (collectFragment != null) {
//                                collectFragment.initPresenter();
                            }
                        break;
                }
            }
        }, 200);
    }

    public void isLogin() {
        Intent intent = new Intent();
        intent.setClass(CooperationActivity.this, LoginActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            finish();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
