package com.xilu.wybz.ui.msg;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MsgZanPresenter;
import com.xilu.wybz.ui.IView.IZanView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgZambiaActivity extends BaseListActivity<ZambiaBean> implements IZanView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无点赞";
    int nodatares = R.drawable.ic_nozan;
    private MsgZanPresenter zanPresenter;

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        zanPresenter = new MsgZanPresenter(this, this);
        zanPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("点赞");
        hideRight();
        tvNoData.setText(nodata);
        ivNoData.setImageResource(nodatares);
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
        zanPresenter.loadData(page++);
    }

    @Override
    public void showZambiaData(List<ZambiaBean> zambiaBeanList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(zambiaBeanList);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_zambia, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_username)
        TextView tvUserName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.tv_music_name)
        TextView tvMusicName;

        @OnClick(R.id.ll_works)
        void toWorks() {
            ZambiaBean zambiaBean = (ZambiaBean) itemView.getTag();
            if (zambiaBean.type == 1) {
                if (zambiaBean.workid > 0)
                    PlayAudioActivity.toPlayAudioActivity(context, zambiaBean.workid, "", MyCommon.MSG_COMMENT);
            } else {
                if (zambiaBean.workid > 0)
                    LyricsdisplayActivity.toLyricsdisplayActivity(context, zambiaBean.workid, 0, zambiaBean.title);
            }
        }

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            ZambiaBean zanbiaBean = mDataList.get(position);
            tvTime.setText(DateTimeUtil.timestamp2DateTime(zanbiaBean.add_time));
            tvUserName.setText(zanbiaBean.nickname);
            tvAuthor.setText(zanbiaBean.author);
            if (StringUtil.isEmpty(zanbiaBean.title)) zanbiaBean.title = "未命名";
            tvMusicName.setText(zanbiaBean.title);
            loadImage(zanbiaBean.pic, ivCover);
            String headUrl = zanbiaBean.headerurl;
            loadImage(headUrl, ivHead);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }
        @Override
        public void onItemClick(View view, int position) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(zanPresenter!=null)
            zanPresenter.cancelUrl();
    }
}
