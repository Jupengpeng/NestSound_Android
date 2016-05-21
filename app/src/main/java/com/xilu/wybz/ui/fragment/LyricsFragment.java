package com.xilu.wybz.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.WorksAdapter;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.SongPresenter;
import com.xilu.wybz.ui.IView.ISongView;
import com.xilu.wybz.ui.find.MoreSongActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by June on 16/5/7.
 */
public class LyricsFragment extends BaseFragment implements ISongView {
    @Bind(R.id.recycler_view_hot)
    RecyclerView recyclerViewHot;
    @Bind(R.id.recycler_view_new)
    RecyclerView recyclerViewNew;
    @Bind(R.id.tv_hotsong)
    TextView tvHotsong;
    @Bind(R.id.tv_newsong)
    TextView tvNewsong;
    private WorksAdapter newWorksAdapter;
    private WorksAdapter hotWorksAdapter;
    private SongPresenter songPresenter;
    int column = 2;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_song;
    }

    @Override
    protected void initPresenter() {
        songPresenter = new SongPresenter(context, this);
        songPresenter.init();
    }

    @Override
    public void showNewSong(List<WorksData> newWorksDatas) {
        if(newWorksDatas.size()>0){
        newWorksAdapter = new WorksAdapter(context, newWorksDatas, column);
        recyclerViewNew.setAdapter(newWorksAdapter);
        }else{
            tvNewsong.setVisibility(View.GONE);
        }
    }

    @Override
    public void showHotSong(List<WorksData> hotWorksDatas) {
        if(hotWorksDatas.size()>0) {
            hotWorksAdapter = new WorksAdapter(context, hotWorksDatas, column);
            recyclerViewHot.setAdapter(hotWorksAdapter);
        }else{
            tvHotsong.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void initView() {
        tvNewsong.setText("最新歌词");
        tvHotsong.setText("热门歌词");
        int space10 = DensityUtil.dip2px(context, 10);
        recyclerViewHot.setNestedScrollingEnabled(false);
        recyclerViewHot.setLayoutManager(new GridLayoutManager(context, column));
        recyclerViewHot.addItemDecoration(new GridSpacingItemDecoration(column, space10, false));
        recyclerViewNew.setNestedScrollingEnabled(false);
        recyclerViewNew.setLayoutManager(new GridLayoutManager(context, column));
        recyclerViewNew.addItemDecoration(new GridSpacingItemDecoration(column, space10, false));

        songPresenter.getLyricsList("new", column);
        songPresenter.getLyricsList("hot", column);
    }

    @OnClick({R.id.tv_hot_more, R.id.tv_new_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hot_more:
                MoreSongActivity.toMoreSongActivity(context, "hot");
                break;
            case R.id.tv_new_more:
                MoreSongActivity.toMoreSongActivity(context, "new");
                break;
        }
    }
}
