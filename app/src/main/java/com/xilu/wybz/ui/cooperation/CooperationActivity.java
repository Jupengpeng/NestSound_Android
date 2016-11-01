package com.xilu.wybz.ui.cooperation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.view.SystemBarHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CooperationActivity extends BaseActivity {
    private List<Fragment> fragments = new ArrayList<>();
    @Bind(R.id.tablayout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.activity_cooperation)
    LinearLayout mAppbar;
    @Bind(R.id.iv)
    ImageView iv;

    CooperationPageAdapter adapter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_cooperation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFragment();
        adapter = new CooperationPageAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        mAppbar.setPadding(0, SystemBarHelper.getStatusBarHeight(this),0,0);

    }
    @OnClick(R.id.iv)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv:
                finish();
                break;
        }
    }


    private void initFragment() {
        CooperationFragment cooperationfragment = new CooperationFragment();
        fragments.add(cooperationfragment);
        MineFragment minefragment = new MineFragment();
        fragments.add(minefragment);
        CollectFragment collectfragment = new CollectFragment();
        fragments.add(collectfragment);
    }
}
