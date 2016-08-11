package com.xilu.wybz.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.song.MakeSongActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FormatHelper;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class HotFragment extends BaseListFragment<TemplateBean> implements IHotView {
    public static final String TYPE = "type";
    public static final String CID = "cid";
    private String type;
    private int cid;
    private HotPresenter hotPresenter;
    private SampleViewHolder sampleViewHolder;
    private int itemWidth;
    private int itemHeight;
    private int playPos = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
            cid = getArguments().getInt(CID);
        }
    }

    public void loadData(String name) {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
            adapter.notifyDataSetChanged();
        }
        page = 1;
        llNoData.setVisibility(View.GONE);
        recycler.setRefreshing();
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        hotPresenter = new HotPresenter(context, this);
        hotPresenter.init();
    }

    @Override
    public void initView() {
//        recycler.enablePullToRefresh(false);
    }

    public static HotFragment newInstance(String type, int cid) {
        HotFragment tabFragment = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        bundle.putInt(CID, cid);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        hotPresenter.loadHotData(cid, type, page++);
    }

    @Override
    public void showHotData(List<TemplateBean> templateBeens) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                if (mDataList == null)
                    mDataList = new ArrayList<>();
                if (mDataList.size() == 0) {
                    EventBus.getDefault().post(new Event.HideKeyboardEvent());
                }
                if (recycler == null) {
                    return;
                }
                llNoData.setVisibility(View.GONE);
                recycler.enableLoadMore(true);
                mDataList.addAll(templateBeens);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
    }

    @Override
    public void loadFail() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void downloadSuccess() {

    }

    @Override
    public void loadNoData() {
        if (isDestroy) return;
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bz_list_item, parent, false);
        sampleViewHolder = new SampleViewHolder(view);
        return sampleViewHolder;
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_play)
        ImageView ivPlay;
        @Bind(R.id.rl_play)
        RelativeLayout rlPlay;
        @Bind(R.id.rl_cover)
        RelativeLayout rlCover;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.tv_times)
        TextView tvTimes;
        public SampleViewHolder(View itemView) {
            super(itemView);
            itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 40)) / 2;
            itemHeight = itemWidth * 172 / 326;
            rlCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        }


        @Override
        public void onBindViewHolder(int position) {
            TemplateBean templateBean = mDataList.get(position);
            itemView.setTag(templateBean);
            tvTitle.setText(templateBean.title);
            tvAuthor.setText(templateBean.author);
            tvTimes.setText(" "+FormatHelper.formatDuration(templateBean.mp3times));
            tvCount.setText("使用次数:"+ NumberUtil.format(templateBean.usenum));
            if (StringUtil.isNotBlank(templateBean.pic)) {
                String imageUrl = MyCommon.getImageUrl(templateBean.pic, itemWidth, itemHeight);
                loadImage(imageUrl, ivCover);
            }
            ivPlay.setImageResource(templateBean.isPlay ? R.drawable.ic_bz_pause : R.drawable.ic_bz_play);
            rlPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (playPos >= 0 && playPos != position) {//切换伴奏 重置上一首的状态
                        MyApplication.mMainService.doRelease();
                        MyApplication.mMainService.playOneMusic(mDataList.get(position).mp3,type+"_hot");
                        mDataList.get(position).isPlay = true;
                        ivPlay.setImageResource(R.drawable.ic_bz_pause);
                        playPos = position;//重新赋值当前播放的位置
                    }else if(playPos >= 0&&playPos==position){//播放当前
                        mDataList.get(playPos).isPlay = !mDataList.get(playPos).isPlay;
                        ivPlay.setImageResource(mDataList.get(playPos).isPlay
                                ? R.drawable.ic_bz_pause : R.drawable.ic_bz_play);
                        MyApplication.mMainService.doPP(mDataList.get(playPos).isPlay);
                    }else{//初此播放
                        MyApplication.mMainService.playOneMusic(mDataList.get(position).mp3,type+"_hot");
                        mDataList.get(position).isPlay = true;
                        ivPlay.setImageResource(R.drawable.ic_bz_pause);
                        playPos = position;//重新赋值当前播放的位置
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            doStop();
            TemplateBean bean = mDataList.get(position);
            MakeSongActivity.toMakeSongActivity(context, bean);
        }

    }
    public void doStop(){
        if (playPos >= 0) {
            mDataList.get(playPos).isPlay = false;
            updateItem(playPos);
            playPos = -1;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (hotPresenter != null)
            hotPresenter.cancelRequest();
    }
}
