package com.xilu.wybz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xilu.wybz.ui.preserve.ProductsFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by June on 16/5/7.
 */
public class ProductAllAdapter extends FragmentStatePagerAdapter{
    String[] title = {"歌曲","歌词"};
    Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();
    public ProductAllAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProductsFragment lyricFragment = new ProductsFragment();
                mPageReferenceMap.put(position,lyricFragment);
                return lyricFragment;
            case 1:
                ProductsFragment songFragment = new ProductsFragment();
                songFragment.viewType = 2;
                mPageReferenceMap.put(position,songFragment);
                return songFragment;

            default:
                return new ProductsFragment();
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
