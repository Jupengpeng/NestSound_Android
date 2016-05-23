package com.xilu.wybz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.xilu.wybz.ui.fragment.ActFragment;
import com.xilu.wybz.ui.fragment.BanzouFragment;
import com.xilu.wybz.ui.fragment.LyricsFragment;
import com.xilu.wybz.ui.fragment.RankingFragment;
import com.xilu.wybz.ui.fragment.SearchLyricsFragment;
import com.xilu.wybz.ui.fragment.SearchSongFragment;
import com.xilu.wybz.ui.fragment.SearchUserFragment;
import com.xilu.wybz.ui.fragment.SongFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by June on 16/5/7.
 */
public class SearchAdapter extends FragmentStatePagerAdapter{
    String[] title = {"歌曲","歌词","用户"};
    Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();
    public SearchAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                mPageReferenceMap.put(position, new SearchSongFragment());
                return new SearchSongFragment();
            case 1:
                mPageReferenceMap.put(position, new SearchLyricsFragment());
                return new SearchLyricsFragment();
            case 2:
                mPageReferenceMap.put(position, new SearchUserFragment());
                return new SearchUserFragment();
            default:
                return new SearchSongFragment();
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
