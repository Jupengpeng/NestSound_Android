package com.xilu.wybz.ui.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.RankingAdapter;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.RankingPresenter;
import com.xilu.wybz.ui.IView.IRankingView;
import com.xilu.wybz.ui.fragment.*;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by June on 16/5/7.
 */
public class RankingFragment extends com.xilu.wybz.ui.fragment.BaseFragment implements IRankingView {
    RankingPresenter rankingPresenter;
    RankingAdapter rankingSongAdapter;
    RankingAdapter rankingLyricsAdapter;
    List<WorksData> songDatas;
    List<WorksData> lyricsDatas;
    @Bind(R.id.tv_song_ranking)
    TextView tvSongRanking;
    @Bind(R.id.recycler_view_song)
    RecyclerView recyclerViewSong;
    @Bind(R.id.tv_lyrics_ranking)
    TextView tvLyricsRanking;
    @Bind(R.id.recycler_view_lyrics)
    RecyclerView recyclerViewLyrics;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_ranking;
    }

    @Override
    protected void initPresenter() {
        rankingPresenter = new RankingPresenter(context, this);
        rankingPresenter.init();
    }

    @Override
    public void initView() {
        songDatas = new ArrayList<>();
        lyricsDatas = new ArrayList<>();
        int space10 = DensityUtil.dip2px(context, 10);

        recyclerViewSong.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSong.addItemDecoration(new SpacesItemDecoration(space10));
        recyclerViewSong.setNestedScrollingEnabled(false);

        recyclerViewLyrics.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewLyrics.addItemDecoration(new SpacesItemDecoration(space10));
        recyclerViewLyrics.setNestedScrollingEnabled(false);

        for(int i=0;i<6;i++){
            songDatas.add(new WorksData());
        }

        lyricsDatas = songDatas;

        rankingSongAdapter = new RankingAdapter(context,songDatas,1);
        rankingLyricsAdapter = new RankingAdapter(context,lyricsDatas,2);
        recyclerViewSong.setAdapter(rankingSongAdapter);
        recyclerViewLyrics.setAdapter(rankingLyricsAdapter);

        rankingPresenter.loadRankingData(userId,1);
        rankingPresenter.loadRankingData(userId,2);
    }

    @Override
    public void showRankingSong(List<WorksData> songWorksDatas) {
        songDatas.addAll(songWorksDatas);
        rankingSongAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRankingLyrics(List<WorksData> lyricsWorksDatas) {
        lyricsDatas.addAll(lyricsWorksDatas);
        rankingLyricsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorView() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
