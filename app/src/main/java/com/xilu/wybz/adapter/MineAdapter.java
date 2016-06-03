package com.xilu.wybz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xilu.wybz.ui.fragment.HotFragment;
import com.xilu.wybz.ui.fragment.WorksDataFragment;

/**
 * Created by hujunwei on 16/6/3.
 */
public class MineAdapter extends FragmentStatePagerAdapter {
    int userId;
    private String[] mTitles = new String[]{"灵感记录", "歌曲", "歌词", "收藏"};

    public MineAdapter(FragmentManager fm,int userId) {
        super(fm);
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        return WorksDataFragment.newInstance(position,userId);
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

