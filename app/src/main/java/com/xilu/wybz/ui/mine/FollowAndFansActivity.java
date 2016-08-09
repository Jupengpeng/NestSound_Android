package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.FollowPresenter;
import com.xilu.wybz.presenter.OnlyFollowPresenter;
import com.xilu.wybz.ui.IView.IFollowAndFansView;
import com.xilu.wybz.ui.IView.IOnlyFollowView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/5/24.
 */
public class FollowAndFansActivity extends BaseListActivity<FansBean> implements IFollowAndFansView ,IOnlyFollowView{

    private FollowPresenter mFollowPresenter;
    private OnlyFollowPresenter mOnlyFollowPresenter;
    private FollowAndFansViewHolder followAndFansViewHolder;
    private int type;
    private int uid;
    private int fromType;
    private int currentPos;//当前关注的pos
    private TextView tvFollow;
    private ImageView ivFollow;
    private int ivfollowStates[] = new int[]{R.drawable.ic_user_follow, R.drawable.ic_user_followed, R.drawable.ic_user_each_follow};
    private int followColors[] = new int[]{R.color.main_theme_color, R.color.main_text_color3, R.color.follow_blue};
    private String tvfollowStates[] = new String[]{"关注", "已关注", "互相关注"};

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
        mOnlyFollowPresenter = new OnlyFollowPresenter(context, this);
        mFollowPresenter = new FollowPresenter(context, this);
        mFollowPresenter.init();
    }

    @Override
    public void initView() {
        hideRight();
        getIntentData();

        if (KeySet.TYPE_FANS_ACT == type) {
            setTitle("粉丝");
            fromType = 1;
        } else if (KeySet.TYPE_FOLLOW_ACT == type) {
            setTitle("关注");
            fromType = 0;
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
        super.onRefresh(action);
        mFollowPresenter.loadData(uid, type, page++);
    }

    @Override
    public void showFansData(List<FansBean> fansBeanList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) {
                    return;
                }
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                recycler.enableLoadMore(true);
                mDataList.addAll(fansBeanList);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
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
    public void followSuccess(String message) {
        cancelPd();
        int status = OnlyFollowPresenter.paraseStatuByString(message);
        EventBus.getDefault().post(new Event.UpdateFollowNumEvent(status, 0));

        Log.d("fans", "status:" + status);
        mDataList.get(currentPos).status = status;
        followAndFansViewHolder.notifyItemByPos(currentPos);
    }

    @Override
    public void followFailed(String message) {
        cancelPd();
    }

    public void followUser(int userId){
        setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mOnlyFollowPresenter != null){
                    mOnlyFollowPresenter.cancel();
                }
            }
        });
        showPd("正在请求网络，请稍候");
        mOnlyFollowPresenter.follow(userId);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follow_fans, parent, false);
        followAndFansViewHolder = new FollowAndFansViewHolder(view);
        return followAndFansViewHolder;
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

        public void notifyItemByPos(int position) {
            FansBean fansBean = mDataList.get(position);
            ivFollow.setImageResource(ivfollowStates[fansBean.status]);
            tvFollow.setText(tvfollowStates[fansBean.status]);
            tvFollow.setTextColor(getResources().getColor(followColors[fansBean.status]));
        }

        @Override
        public void onBindViewHolder(final int position) {
            final FansBean fansBean = mDataList.get(position);
            loadImage(fansBean.headurl, ivHead);
            userName.setText(fansBean.fansname);
            userSign.setText(fansBean.fansign);
            ivFollowState.setImageResource(ivfollowStates[fansBean.status]);
            tvFollowState.setText(tvfollowStates[fansBean.status]);
            tvFollowState.setTextColor(getResources().getColor(followColors[fansBean.status]));
            llFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentPos = position;
                    Log.d("url","p:"+position+fansBean.fansname);
                    ivFollow = ivFollowState;
                    tvFollow = tvFollowState;
                    if (type == KeySet.TYPE_FANS_ACT) {
                        followUser(fansBean.fansid);
                    } else {
                        followUser(fansBean.userid);
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
            int authorid = fromType == 0 ? mDataList.get(position).userid : mDataList.get(position).fansid;
            boolean isMe = PrefsUtil.getUserId(context) != authorid;
            if (isMe) {
                UserInfoActivity.ToNewUserInfoActivity(context, authorid, mDataList.get(position).fansname);
            }
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        if (mFollowPresenter != null)
            mFollowPresenter.cancelRequest();
    }
}
