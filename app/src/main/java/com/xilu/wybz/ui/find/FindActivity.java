package com.xilu.wybz.ui.find;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.FindAdapter;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.MoreWindow;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/4/29.
 */
public class FindActivity extends BasePlayMenuActivity{
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.container)
    ViewPager container;
    public boolean isFirst;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_find;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void initView() {
        if(isFirst)return;
            else isFirst = true;
        FindAdapter pagerAdapter = new FindAdapter(getSupportFragmentManager());
        container.setAdapter(pagerAdapter);
        container.setOffscreenPageLimit(4);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(container);
    }
    @OnClick({R.id.ll_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivity(SearchWorksActivity.class);
                return;
        }

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
