package com.xilu.wybz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xilu.wybz.ui.fragment.SearchLyricsFragment;
import com.xilu.wybz.ui.fragment.SearchSongFragment;
import com.xilu.wybz.ui.fragment.SearchUserFragment;

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
                SearchSongFragment searchSongFragment = new SearchSongFragment();
                mPageReferenceMap.put(position, searchSongFragment);
                return searchSongFragment;
            case 1:
                SearchLyricsFragment searchLyricsFragment = new SearchLyricsFragment();
                mPageReferenceMap.put(position, searchLyricsFragment);
                return searchLyricsFragment;
            case 2:
                SearchUserFragment searchUserFragment = new SearchUserFragment();
                mPageReferenceMap.put(position, searchUserFragment);
                return searchUserFragment;
            default:
                return null;
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
