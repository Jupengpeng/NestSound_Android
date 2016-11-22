package com.xilu.wybz.ui.cooperation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CooperationPageAdapter extends FragmentStatePagerAdapter {

    private String[] title = {"合作", "我的", "收藏"};
    Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();

    public CooperationPageAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                CooperationFragment cooperationFragment = new CooperationFragment();
                mPageReferenceMap.put(position, cooperationFragment);
                return cooperationFragment;
            case 1:
                MineFragment mineFragment = new MineFragment();
                mPageReferenceMap.put(position, mineFragment);
                return mineFragment;
            case 2:
                CollectFragment collectFragment = new CollectFragment();
                mPageReferenceMap.put(position, collectFragment);
                return collectFragment;
            default:
                return new CooperationFragment();
        }
    }

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
