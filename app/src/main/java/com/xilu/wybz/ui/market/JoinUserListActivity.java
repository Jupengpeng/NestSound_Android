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
import com.xilu.wybz.bean.JoinUserBean;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.JoinUserPresenter;
import com.xilu.wybz.ui.IView.IJoinUserView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/8/18.
 */
public class JoinUserListActivity extends BaseListActivity<JoinUserBean> implements IJoinUserView {
    JoinUserPresenter joinUserPresenter;
    String aid;

    public static void toJoinUserListActivity(Context context, String aid) {
        Intent intent = new Intent();
        intent.setClass(context, JoinUserListActivity.class);
        intent.putExtra(KeySet.KEY_ID, aid);
        context.startActivity(intent);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        joinUserPresenter = new JoinUserPresenter(context, this);
        joinUserPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("参加人");
        hideRight();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            aid = bundle.getString(KeySet.KEY_ID);
        }
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        joinUserPresenter.loadData(aid, page++);
    }


    @Override
    public void showData(List<JoinUserBean> list) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(list);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
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
        @Bind(R.id.ll_follow)
        LinearLayout llFollow;

        public FollowAndFansViewHolder(View itemView) {
            super(itemView);

        }

        @Override
        public void onBindViewHolder(final int position) {
            JoinUserBean joinUserBean = mDataList.get(position);
            llFollow.setVisibility(View.GONE);
            if (StringUtils.isNotBlank(joinUserBean.headurl))
                ImageLoadUtil.loadImage(joinUserBean.headurl, ivHead);
            if (StringUtils.isNotBlank(joinUserBean.nickname))
                userName.setText(joinUserBean.nickname);
            if (StringUtils.isNotBlank(joinUserBean.descr))
                userSign.setText(joinUserBean.descr);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }
        @Override
        public void onItemClick(View view, int position) {
            if (PrefsUtil.getUserId(context) != mDataList.get(position).id) {
                UserInfoActivity.toUserInfoActivity(context, mDataList.get(position).id,
                        mDataList.get(position).nickname);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (joinUserPresenter != null)
            joinUserPresenter.cancelRequest();
    }
}
