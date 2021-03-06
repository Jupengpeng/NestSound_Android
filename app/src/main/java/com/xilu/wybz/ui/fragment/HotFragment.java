package com.xilu.wybz.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.song.MakeSongActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FormatHelper;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

public class HotFragment extends BaseListFragment<TemplateBean> implements IHotView {
    public static final String TYPE = "type";
    public static final String CID = "cid";
    public static final String FLASH_TAG = "FLASH_TAG";
    private String type;
    private int cid;
    private HotPresenter hotPresenter;
    private SampleViewHolder sampleViewHolder;
    private int itemWidth;
    private int itemHeight;
    private int playPos = -1;
    public boolean flash = false;
    public String aid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.getInstance().mMainService == null) {
            MyApplication.getInstance().bindMainService();
        }
        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
            cid = getArguments().getInt(CID);
            flash = getArguments().getBoolean(FLASH_TAG, false);
            aid = getArguments().getString(KeySet.KEY_ID);
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

    public static HotFragment newInstance(String type, int cid, boolean flash, String aid) {
        HotFragment tabFragment = new HotFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        bundle.putString(KeySet.KEY_ID, aid);
        bundle.putInt(CID, cid);
        bundle.putBoolean(FLASH_TAG, flash);
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
        super.onRefresh(action);
        hotPresenter.loadHotData(cid, type, page++);
    }

    @Override
    public void showHotData(List<TemplateBean> templateBeens) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                if(action== PullRecycler.ACTION_PULL_TO_REFRESH){
                    mDataList.clear();
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
            tvTimes.setText(" " + FormatHelper.formatDuration(templateBean.mp3times));
            tvCount.setText("使用次数:" + NumberUtil.format(templateBean.usenum));
            if (StringUtils.isNotBlank(templateBean.pic)) {
                String imageUrl = MyCommon.getImageUrl(templateBean.pic, itemWidth, itemHeight);
                loadImage(imageUrl, ivCover);
            }
            ivPlay.setImageResource(templateBean.isPlay ? R.drawable.ic_bz_pause : R.drawable.ic_bz_play);
            rlPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyApplication.getInstance().getMainService() != null) {
                        if (playPos >= 0 && playPos != position) {//切换伴奏 重置上一首的状态
                            MyApplication.getInstance().mMainService.doRelease();
                            MyApplication.getInstance().mMainService.playOneMusic(mDataList.get(position).mp3, type + "_hot");
                            mDataList.get(position).isPlay = true;
                            ivPlay.setImageResource(R.drawable.ic_bz_pause);
                            playPos = position;//重新赋值当前播放的位置
                        } else if (playPos >= 0 && playPos == position) {//播放当前
                            mDataList.get(playPos).isPlay = !mDataList.get(playPos).isPlay;
                            ivPlay.setImageResource(mDataList.get(playPos).isPlay
                                    ? R.drawable.ic_bz_pause : R.drawable.ic_bz_play);
                            MyApplication.getInstance().mMainService.doPP(mDataList.get(playPos).isPlay);
                        } else {//初此播放
                            MyApplication.getInstance().mMainService.playOneMusic(mDataList.get(position).mp3, type + "_hot");
                            mDataList.get(position).isPlay = true;
                            ivPlay.setImageResource(R.drawable.ic_bz_pause);
                            playPos = position;//重新赋值当前播放的位置
                        }
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
            bean.aid = aid;
            if (flash) {
                EventBus.getDefault().post(new Event.ImportHotEvent(bean));
                if (getActivity() != null) {
                    getActivity().finish();
                }
            } else {
                MakeSongActivity.toMakeSongActivity(context, bean);
            }
        }
    }

    public void doStop() {
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
