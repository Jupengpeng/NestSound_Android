package com.xilu.wybz.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MusicTalkMorePresenter;
import com.xilu.wybz.service.MainService;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.ui.IView.IMusicTalkMoreView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/5/20.
 *
 *
 */
public class MusicTalkMoreActivity extends BaseListActivity<MusicTalk> implements IMusicTalkMoreView {

    private String nodata = "暂无更多乐说";
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
        super.onRefresh(action);
        musicTalkMorePresenter.loadData(page++);
    }

//    protected ILayoutManager getLayoutManager() {
//        return new MyLinearLayoutManager(getApplicationContext());
//    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpacesItemDecoration(dip10);
    }

    @Override
    public void showMusicTalkData(List<MusicTalk> songAlbumList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH){
            mDataList.clear();
        }

        recycler.enableLoadMore(true);
        mDataList.addAll(songAlbumList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
        checkData();
    }

    @Override
    public void loadFail() {
        if (recycler == null) {
            return;
        }
        recycler.onRefreshCompleted();
    }


    @Override
    public void loadNoMore() {
        if (recycler == null) {
            return;
        }
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (recycler == null) {
            return;
        }
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
            int itemWidth = DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 20);
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
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            toPlayPos(position);
        }
    }

    public void toPlayPos(int position) {
        if (mDataList.size() > 0) {
            MusicTalk musicTalk = mDataList.get(position);
            if (StringUtils.isBlank(musicTalk.url) && StringUtils.isNotBlank(musicTalk.itemid)) {
                String playFrom = PrefsUtil.getString("playFrom", context);
                if (!playFrom.equals(MyCommon.MUSICTALK) || MainService.ids.size() == 0) {
                    if (MainService.ids.size() > 0)
                        MainService.ids.clear();
                    for (MusicTalk worksData : mDataList) {
                        MainService.ids.add(worksData.itemid);
                    }
                }
                PlayAudioActivity.toPlayAudioActivity(context, musicTalk.itemid, "", MyCommon.MUSICTALK);
            } else if (StringUtils.isNotBlank(musicTalk.url)) {
                BrowserActivity.toBrowserActivity(context, musicTalk);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (musicTalkMorePresenter != null)
            musicTalkMorePresenter.cancelRequest();
    }
}
