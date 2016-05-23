package com.xilu.wybz.ui.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.presenter.SongablumMorePresenter;
import com.xilu.wybz.ui.IView.ISongablumMoreView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
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

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        songablumMorePresenter.loadData(userId, page++);
    }
    protected ILayoutManager getLayoutManager() {
        return new MyGridLayoutManager(getApplicationContext(), 2);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridSpacingItemDecoration(2, DensityUtil.dip2px(context,10), false);
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
        recycler.onRefreshCompleted();
    }


    @Override
    public void loadNoMore() {
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_home_songalbum_item, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView mDraweeView;

        public SampleViewHolder(View itemView) {
            super(itemView);
            int itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 30)) / 2;
            int itemHeight = itemWidth * 21 / 32;
            mDraweeView.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        }

        @Override
        public void onBindViewHolder(int position) {
            String pic = mDataList.get(position).getPic();
            loadImage(pic, mDraweeView);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

}
