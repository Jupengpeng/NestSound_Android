package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.FollowPresenter;
import com.xilu.wybz.ui.IView.IFollowAndFansView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/24.
 */
public class FollowAndFansActivity extends BaseListActivity<FansBean> implements IFollowAndFansView {

    FollowPresenter mFollowPresenter;
    private int type;
    private int uid;
    private int ivfollowStates[] = new int[]{R.drawable.ic_user_follow,R.drawable.ic_user_followed,R.drawable.ic_user_each_follow};
    private int followColors[] = new int[]{R.color.main_text_color,R.color.main_text_color2,R.color.lightblue};
    private String tvfollowStates[] = new String []{"关注","已关注","互相关注"};
    public static void toFollowAndFansActivity(Context context, int type, int uid) {
        Intent intent = new Intent();
        intent.setClass(context, FollowAndFansActivity.class);
        intent.putExtra(KeySet.KEY_TYPE, type);
        intent.putExtra(KeySet.KEY_UID, uid);
        context.startActivity(intent);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        mFollowPresenter = new FollowPresenter(context, this);
        mFollowPresenter.init();
    }

    @Override
    public void initView() {
        hideRight();
        getIntentData();

        if (KeySet.TYPE_FANS_ACT == type) {
            setTitle("粉丝");
        } else if (KeySet.TYPE_FOLLOW_ACT == type) {
            setTitle("关注");
        }

    }

    public void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(KeySet.KEY_TYPE, 0);
            uid = bundle.getInt(KeySet.KEY_UID, 0);
        }
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
        mFollowPresenter.loadData(uid, type, page++);
    }

    @Override
    public void showFansData(List<FansBean> fansBeanList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(fansBeanList);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow_fans, parent, false);
        return new FollowAndFansViewHolder(view);
    }

    class FollowAndFansViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.user_name)
        TextView userName;
        @Bind(R.id.user_sign)
        TextView userSign;
        @Bind(R.id.iv_follow_state)
        ImageView ivFollowState;
        @Bind(R.id.tv_follow_state)
        TextView tvFollowState;
        @OnClick(R.id.ll_follow)
        public void onClick() {

        }
        public FollowAndFansViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBindViewHolder(int position) {
            FansBean fansBean = mDataList.get(position);
            loadImage(fansBean.headurl,ivHead);
            userName.setText(fansBean.fansname);
            userSign.setText(fansBean.fansign);
            ivFollowState.setImageResource(ivfollowStates[fansBean.status]);
            tvFollowState.setText(tvfollowStates[fansBean.status]);
            tvFollowState.setTextColor(getResources().getColor(followColors[fansBean.status]));
        }
        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
