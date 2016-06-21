package com.xilu.wybz.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MusicTalkMorePresenter;
import com.xilu.wybz.presenter.SongablumMorePresenter;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.IMusicTalkMoreView;
import com.xilu.wybz.ui.IView.ISongablumMoreView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/5/20.
 */
public class MusicTalkMoreActivity extends BaseListActivity<MusicTalk> implements IMusicTalkMoreView {
    private int page = 1;
    private int action = 0;
    private String nodata = "暂无歌单";
    private MusicTalkMorePresenter musicTalkMorePresenter;

    @Override
    protected void initPresenter() {
        musicTalkMorePresenter = new MusicTalkMorePresenter(this, this);
        musicTalkMorePresenter.init();
    }

    @Override
    public void initView() {
        setTitle("乐说");
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
        musicTalkMorePresenter.loadData(page++);
    }
    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getApplicationContext());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpacesItemDecoration(dip10);
    }

    @Override
    public void showMusicTalkData(List<MusicTalk> songAlbumList) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_home_musictalk_item, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView mDraweeView;

        public SampleViewHolder(View itemView) {
            super(itemView);
            int itemWidth = DensityUtil.getScreenW(context)-DensityUtil.dip2px(context,20);
            int itemHeight = itemWidth * 191 / 330;
            mDraweeView.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        }

        @Override
        public void onBindViewHolder(int position) {
            String pic = mDataList.get(position).pic;
            loadImage(pic, mDraweeView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            toPlayPos(position);
        }
    }
    public void toPlayPos(int position){
        if (mDataList.size() > 0) {
            MusicTalk musicTalk = mDataList.get(position);
            if(musicTalk.type==1) {
                String playFrom = PrefsUtil.getString("playFrom", context);
                if (!playFrom.equals(MyCommon.MUSICTALK) || MyApplication.ids.size() == 0) {
                    if (MyApplication.ids.size() > 0)
                        MyApplication.ids.clear();
                    for (MusicTalk worksData : mDataList) {
                        MyApplication.ids.add(worksData.itemid);
                    }
                }

                PlayAudioActivity.toPlayAudioActivity(context, musicTalk.itemid, "", MyCommon.MUSICTALK);
            }else{
                if(StringUtil.isNotBlank(musicTalk.url)){
                    BrowserActivity.toBrowserActivity(context,musicTalk.url);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(musicTalkMorePresenter!=null)
            musicTalkMorePresenter.cancelUrl();
    }
}
