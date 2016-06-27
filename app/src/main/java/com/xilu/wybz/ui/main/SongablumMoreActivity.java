package com.xilu.wybz.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.SongablumViewHolder;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.presenter.SongablumMorePresenter;
import com.xilu.wybz.ui.IView.ISongablumMoreView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunwei on 16/5/20.
 */
public class SongablumMoreActivity extends BaseListActivity<SongAlbum> implements ISongablumMoreView {
    private int page = 1;
    private int action = 0;
    private String nodata = "暂无歌单";
    private SongablumMorePresenter songablumMorePresenter;

    @Override
    protected void initPresenter() {
        songablumMorePresenter = new SongablumMorePresenter(this, this);
        songablumMorePresenter.init();
    }

    @Override
    public void initView() {
        setTitle("歌单");
        tvNoData.setText(nodata);
    }

    public boolean hasPadding() {
        return true;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        songablumMorePresenter.loadData(page++);
    }

    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(getApplicationContext(), 2);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridSpacingItemDecoration(2, dip10, false);
    }

    @Override
    public void showSongAblumData(List<SongAlbum> songAlbumList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(songAlbumList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadFail() {
        if (recycler == null){
            return;
        }
        recycler.onRefreshCompleted();
    }


    @Override
    public void loadNoMore() {
        if (recycler == null){
            return;
        }
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (recycler == null){
            return;
        }
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_home_songalbum_item, parent, false);
        return new SongablumViewHolder(view, context, mDataList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(songablumMorePresenter!=null)
            songablumMorePresenter.cancelRequest();
    }
}
