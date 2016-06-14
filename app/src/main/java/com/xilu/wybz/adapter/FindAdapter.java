package com.xilu.wybz.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.xilu.wybz.ui.fragment.ActFragment;
import com.xilu.wybz.ui.fragment.LyricsFragment;
import com.xilu.wybz.ui.fragment.RankingFragment;
import com.xilu.wybz.ui.fragment.SongFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by June on 16/5/7.
 */
public class FindAdapter extends FragmentStatePagerAdapter{
    String[] title = {"活动","歌曲","歌词","榜单"};
    Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();
    public FindAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ActFragment actFragment = new ActFragment();
                mPageReferenceMap.put(position,actFragment);
                return actFragment;
            case 1:
                SongFragment songFragment = new SongFragment();
                mPageReferenceMap.put(position,songFragment);
                return songFragment;
            case 2:
                LyricsFragment lyricsFragment = new LyricsFragment();
                mPageReferenceMap.put(position,lyricsFragment);
                return lyricsFragment;
            case 3:
                RankingFragment rankingFragment = new RankingFragment();
                mPageReferenceMap.put(position,rankingFragment);
                return rankingFragment;
            default:
                return new ActFragment();
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
