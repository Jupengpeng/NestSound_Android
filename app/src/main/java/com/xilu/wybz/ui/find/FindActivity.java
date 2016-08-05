package com.xilu.wybz.ui.find;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.FindAdapter;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.ActFragment;
import com.xilu.wybz.ui.fragment.LyricsFragment;
import com.xilu.wybz.ui.fragment.RankingFragment;
import com.xilu.wybz.ui.fragment.SearchLyricsFragment;
import com.xilu.wybz.ui.fragment.SearchSongFragment;
import com.xilu.wybz.ui.fragment.SearchUserFragment;
import com.xilu.wybz.ui.fragment.SongFragment;
import com.xilu.wybz.ui.login.NewLoginActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.MoreWindow;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/4/29.
 */
public class FindActivity extends BasePlayMenuActivity implements ViewPager.OnPageChangeListener{
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.container)
    ViewPager container;
    public boolean isFirst;
    public FindAdapter pagerAdapter;

    @Override
    public boolean canBack() {
        return false;
    }

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
        setTitle("");
        pagerAdapter = new FindAdapter(getSupportFragmentManager());
        container.setAdapter(pagerAdapter);
        container.setOffscreenPageLimit(4);
        container.setOnPageChangeListener(this);
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (position){
                    case 1:
                        SongFragment songFragment = (SongFragment) pagerAdapter.getFragment(1);
                        if(songFragment!=null){
                            songFragment.loadData();
                        }
                        break;
                    case 2:
                        LyricsFragment lyricsFragment = (LyricsFragment) pagerAdapter.getFragment(2);
                        if(lyricsFragment!=null){
                            lyricsFragment.loadData();
                        }
                        break;
                    case 3:
                        RankingFragment rankingFragment = (RankingFragment) pagerAdapter.getFragment(3);
                        if(rankingFragment!=null){
                            rankingFragment.loadData();
                        }
                        break;
                }
            }
        },200);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
