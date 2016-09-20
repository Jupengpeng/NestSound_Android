package com.xilu.wybz.ui.preservation;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.ProductAllAdapter;
import com.xilu.wybz.ui.base.ToolbarActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ProductAllActivity extends ToolbarActivity implements ViewPager.OnPageChangeListener{

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.container)
    ViewPager container;
    public boolean isFirst;
    public ProductAllAdapter pagerAdapter;



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_preserv_product_all;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        setTitle("作品列表");
        pagerAdapter = new ProductAllAdapter(getSupportFragmentManager());
        container.setAdapter(pagerAdapter);
        container.addOnPageChangeListener(this);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(container);
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
//                    case 1:
//                        SongFragment songFragment = (SongFragment) pagerAdapter.getFragment(1);
//                        if(songFragment!=null){
//                            songFragment.loadData();
//                        }
//                        break;
//                    case 2:
//                        LyricsFragment lyricsFragment = (LyricsFragment) pagerAdapter.getFragment(2);
//                        if(lyricsFragment!=null){
//                            lyricsFragment.loadData();
//                        }
//                        break;
//                    case 3:
//                        RankingFragment rankingFragment = (RankingFragment) pagerAdapter.getFragment(3);
//                        if(rankingFragment!=null){
//                            rankingFragment.loadData();
//                        }
//                        break;
                }
            }
        },200);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
