package com.xilu.wybz.ui.ui.msg;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.SystemBean;
import com.xilu.wybz.presenter.MsgSystemPresenter;
import com.xilu.wybz.ui.IView.ISystemMsgView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.BrowserActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgSystemActivity extends BaseListActivity<SystemBean> implements ISystemMsgView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无系统消息";
    private MsgSystemPresenter systemPresenter;

    @Override
    protected void initPresenter() {
        systemPresenter = new MsgSystemPresenter(this, this);
        systemPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("系统消息");
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
        systemPresenter.loadData(userId, page++);
    }

    @Override
    public void showSystemData(List<SystemBean> systemBeans) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(systemBeans);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_system, parent, false);
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
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_info)
        TextView tvInfo;
        @Bind(R.id.ll_detail)
        LinearLayout llDetail;

        @OnClick(R.id.ll_detail)
        void detailClick() {
            toWebView((SystemBean) card.getTag());
        }

        public SampleViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            int itemWidth = DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 30);
            int itemHeight = itemWidth * 101 / 330;
            ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth * 141 / 330, itemHeight));
        }

        View card;

        @Override
        public void onBindViewHolder(int position) {
            SystemBean systemBean = mDataList.get(position);
            if (TextUtils.isEmpty(systemBean.getDetailurl())) {
                tvContent.setText(systemBean.getContent());
                tvContent.setVisibility(View.VISIBLE);
                llDetail.setVisibility(View.GONE);
            } else {
                tvInfo.setText(systemBean.getContent());
                tvContent.setVisibility(View.GONE);
                llDetail.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(systemBean.getPicurl())){
                    loadImage(systemBean.getPicurl(), ivCover);
                }
            }
            tvUserName.setText(systemBean.getSysname().replace("无乐不作", "音巢音乐"));
            tvTime.setText(systemBean.getSendtime());
            loadImage(systemBean.getSysheadurl(), ivHead);
        }

        @Override
        public void onItemClick(View view, int position) {

        }

    }

    void toWebView(SystemBean systemBean) {
        BrowserActivity.toBrowserActivity(context, systemBean.getDetailurl());
    }
}
