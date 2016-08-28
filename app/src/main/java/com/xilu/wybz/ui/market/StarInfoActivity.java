package com.xilu.wybz.ui.market;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.StarBean;
import com.xilu.wybz.bean.StarInfoBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.StarInfoPresenter;
import com.xilu.wybz.ui.IView.IStarInfoView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseSectionListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.section.SectionData;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/8/12.
 */
public class StarInfoActivity extends BaseSectionListActivity<WorksData> implements IStarInfoView {

    private StarBean starBean;
    private StarInfoPresenter starInfoPresenter;
    private int uid;
    private int playPos = -1;
    private String type = "musician";

    public static void toStarInfoActivity(Context context, int uid) {
        Intent intent = new Intent(context, StarInfoActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    @Override
    protected void initPresenter() {
        starInfoPresenter = new StarInfoPresenter(context, this);
        starInfoPresenter.init();
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void initView() {
        if (MyApplication.getInstance().mMainService == null) {
            MyApplication.getInstance().bindMainService();
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uid = bundle.getInt("uid");
        } else {
            finish();
        }
        recycler.enablePullToRefresh(false);
        setTitle("");
        hideRight();
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        starInfoPresenter.getStarInfo(uid, page++);
    }


    @Override
    public void showData(StarInfoBean starInfoBean) {
        if (isDestroy) return;
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if (mDataList.size() == 0) {
            starBean = starInfoBean.musician;
            mDataList.add(new SectionData<>(true, 0, "用户个人资料"));
        }
        if (starInfoBean.worklist != null) {
            if (starInfoBean.worklist.size() == 0) {
                loadNoMore();
            } else {
                for (WorksData worksData : starInfoBean.worklist) {
                    mDataList.add(new SectionData<>(worksData));
                }
            }
        }
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
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    protected BaseViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_startinfo_header, parent, false);
        return new SectionHeaderViewHolder(view);

    }

    public class SectionHeaderViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_intro)
        TextView tvIntro;
        @Bind(R.id.tv_tag1)
        TextView tvTag1;
        @Bind(R.id.tv_tag2)
        TextView tvTag2;
        @Bind(R.id.tv_tag3)
        TextView tvTag3;
        @Bind(R.id.ll_tag)
        LinearLayout llTag;
        @Bind(R.id.tv_desc)
        TextView tvDesc;
        int dip60;
        public SectionHeaderViewHolder(View view) {
            super(view);
            dip60 = DensityUtil.dip2px(context, 60);
        }

        @Override
        public void onBindViewHolder(int position) {
            if (starBean != null) {
                if (StringUtils.isNotBlank(starBean.pic))
                    ImageLoadUtil.loadImage(MyCommon.getImageUrl(starBean.pic,dip60,dip60), ivHead);
                if (StringUtils.isNotBlank(starBean.name))
                    tvName.setText(starBean.name);
                if (StringUtils.isNotBlank(starBean.introduction))
                    tvIntro.setText(starBean.introduction);
                if (StringUtils.isNotBlank(starBean.description))
                    tvDesc.setText(starBean.description);
                if (StringUtils.isNotBlank(starBean.ability)) {
                    String[] tags = starBean.ability.split("/");
                    llTag.setVisibility(tags.length > 0 ? View.VISIBLE : View.GONE);
                    if (tags.length == 1) {
                        tvTag1.setVisibility(View.VISIBLE);
                        tvTag2.setVisibility(View.GONE);
                        tvTag3.setVisibility(View.GONE);
                        tvTag1.setText(tags[0]);
                    } else if (tags.length == 2) {
                        tvTag2.setText(tags[0]);
                        tvTag3.setText(tags[1]);
                        tvTag1.setVisibility(View.GONE);
                        tvTag2.setVisibility(View.VISIBLE);
                        tvTag3.setVisibility(View.VISIBLE);
                    } else if (tags.length >= 3) {
                        tvTag1.setText("全能");
                        tvTag1.setVisibility(View.VISIBLE);
                        tvTag2.setVisibility(View.GONE);
                        tvTag3.setVisibility(View.GONE);
                    }

                } else {
                    llTag.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    @Override
    protected BaseViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_star_info, parent, false);
        return new SimpleViewHolder(view);
    }

    public class SimpleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_play)
        ImageView ivPlay;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position).t;
            if (worksData != null) {
                if (StringUtils.isNotBlank(worksData.pic))
                    ImageLoadUtil.loadImage(worksData.pic, ivCover, DensityUtil.dip2px(context, 80), DensityUtil.dip2px(context, 80));
                if (StringUtils.isNotBlank(worksData.title))
                    tvName.setText("歌曲名：" + worksData.title);
                if (StringUtils.isNotBlank(worksData.name))
                    tvAuthor.setText("作者：" + worksData.name);
                ivPlay.setImageResource(worksData.isPlay?R.drawable.ic_match_pause:R.drawable.ic_match_play);
                ivPlay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MyApplication.getInstance().getMainService() != null) {
                            if (playPos >= 0 && playPos != position) {
                                MyApplication.getInstance().mMainService.doStop();
                                MyApplication.getInstance().mMainService.playOneMusic(mDataList.get(position).t.mp3, type);
                                mDataList.get(position).t.isPlay = true;
                                ivPlay.setImageResource(R.drawable.ic_match_pause);
                                playPos = position;//重新赋值当前播放的位置
                            } else if (playPos >= 0 && playPos == position) {//播放当前
                                mDataList.get(playPos).t.isPlay = !mDataList.get(playPos).t.isPlay;
                                ivPlay.setImageResource(mDataList.get(playPos).t.isPlay
                                        ? R.drawable.ic_match_pause : R.drawable.ic_match_play);
                                MyApplication.getInstance().mMainService.doPP(mDataList.get(playPos).t.isPlay);
                            } else {//初此播放
                                MyApplication.getInstance().mMainService.playOneMusic(mDataList.get(position).t.mp3, type);
                                mDataList.get(position).t.isPlay = true;
                                ivPlay.setImageResource(R.drawable.ic_match_pause);
                                playPos = position;//重新赋值当前播放的位置
                            }
                        }
                    }
                });
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.PPStatusEvent event) {
        String from = event.getFrom();
        if (StringUtils.isBlank(from)) return;
        if (from.equals(type)) {
            switch (event.getStatus()) {
                case MyCommon.STARTED:
                    if(!mDataList.get(playPos).t.isPlay) {
                        mDataList.get(playPos).t.isPlay = true;
                        adapter.notifyItemChanged(playPos);
                        recycler.getRecyclerView().requestLayout();
                    }
                    break;
                case MyCommon.END:
                case MyCommon.STOPPED://停止
                    doStop();
                    break;
            }
        }
    }

    public void doStop() {
        if (playPos >= 0) {
            mDataList.get(playPos).t.isPlay = false;
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyApplication.getInstance().getMainService() != null)
            MyApplication.getInstance().mMainService.doRelease();
    }
}
