package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xilu.wybz.ui.fragment.OtherWorksDataFragment;
import com.xilu.wybz.ui.fragment.WorksDataFragment;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hujunwei on 16/6/3.
 */
public class OtherCenterListAdapter extends FragmentStatePagerAdapter {
    int userId;
    String userName;
    private List<String> mTitles = new ArrayList<>();
    Map<Integer, OtherWorksDataFragment> mPageReferenceMap = new HashMap<>();
    public OtherCenterListAdapter(Context context, FragmentManager fm, int userId, String userName) {
        super(fm);
        this.userId = userId;
        this.userName = userName;
        mTitles.add("歌曲");
        mTitles.add("歌词");
        mTitles.add("收藏");
    }

    @Override
    public Fragment getItem(int position) {
        OtherWorksDataFragment worksDataFragment = OtherWorksDataFragment.newInstance(position,userId,userName);
        mPageReferenceMap.put(position,worksDataFragment);
        return worksDataFragment;
    }
    public OtherWorksDataFragment getFragment(int key) {
        return mPageReferenceMap.get(key);
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

