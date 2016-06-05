package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.xilu.wybz.ui.fragment.WorksDataFragment;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunwei on 16/6/3.
 */
public class MineAdapter extends FragmentStatePagerAdapter {
    int userId;
    String userName;
    private List<String> mTitles = new ArrayList<>();

    public MineAdapter(Context context, FragmentManager fm, int userId, String userName) {
        super(fm);
        this.userId = userId;
        this.userName = userName;
        if(userId== PrefsUtil.getUserId(context)){
            mTitles.add("灵感记录");
        }
        mTitles.add("我的歌曲");
        mTitles.add("我的歌词");
        mTitles.add("我的收藏");
    }

    @Override
    public Fragment getItem(int position) {
        return WorksDataFragment.newInstance(position,userId,userName);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}

