package com.xilu.wybz.ui.adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.xilu.wybz.ui.fragment.ActFragment;
import com.xilu.wybz.ui.fragment.LyricsFragment;
import com.xilu.wybz.ui.fragment.RankingFragment;
import com.xilu.wybz.ui.fragment.SongFragment;

/**
 * Created by June on 16/5/7.
 */
public class FindAdapter extends FragmentStatePagerAdapter{
    String[] title = {"活动","歌曲","歌词","榜单"};

    public FindAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ActFragment();
            case 1:
                return new SongFragment();
            case 2:
                return new LyricsFragment();
            case 3:
                return new RankingFragment();
            default:
                return new ActFragment();
        }
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
