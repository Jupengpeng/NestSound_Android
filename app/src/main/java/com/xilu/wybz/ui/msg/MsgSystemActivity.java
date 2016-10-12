package com.xilu.wybz.ui.msg;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.message.SystemMessageBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MsgSystemPresenter;
import com.xilu.wybz.ui.IView.ISystemMsgView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/1/27.
 */
public class MsgSystemActivity extends BaseListActivity<SystemMessageBean> implements ISystemMsgView {

    private String nodata = "暂无系统消息";

    private MsgSystemPresenter systemPresenter;

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void initPresenter() {
        systemPresenter = new MsgSystemPresenter(this, this);
        systemPresenter.init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        clearMsg();
        onRefresh(PullRecycler.ACTION_PULL_TO_REFRESH);
    }

    public void clearMsg() {
        EventBus.getDefault().post(new Event.ClearMsgEvent(MyCommon.PUSH_TYPE_SYSTEMMSG));
        Intent mIntent = new Intent("com.xilu.wybz.intent.CLEARNOTICE");
        mIntent.putExtra("type", MyCommon.PUSH_TYPE_SYSTEMMSG);
        sendBroadcast(mIntent);
    }

    @Override
    public void initView() {
        setTitle("系统消息");
        hideRight();
        clearMsg();
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
        systemPresenter.loadData(page++);
    }

    @Override
    public void showSystemData(List<SystemMessageBean> systemBeans) {
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

//        SystemMessageBean  bean = new SystemMessageBean();
//        bean.pushtype = MyCommon.PUSH_TYPE_ACTIVITYFINISH;
//        bean.content = "angudsuinvowie";
//        bean.createtime = System.currentTimeMillis();
//
//        mDataList.add(bean);
//        mDataList.add(bean);
//
//        bean = new SystemMessageBean();
//        bean.pushtype = MyCommon.PUSH_TYPE_RECOMENDTOINDEX;
//        bean.content = "angudsuinvowie";
//        bean.createtime = System.currentTimeMillis();
//
//        mDataList.add(bean);
//        mDataList.add(bean);
//
//        recycler.enableLoadMore(true);
//        adapter.notifyDataSetChanged();

        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg_system, parent, false);
        return new SampleViewHolder(view);
    }


    /**
     * SampleViewHolder.
     */
    class SampleViewHolder extends BaseViewHolder {

        @Bind(R.id.iv_type)
        ImageView ivType;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_create_time)
        TextView tvCreateTime;

        View root;


        SystemMessageBean bean;

        /**
         * @param itemView
         */
        public SampleViewHolder(View itemView) {
            super(itemView);
            root = itemView;
        }


        @Override
        public void onBindViewHolder(int position) {
            bean = mDataList.get(position);

            switch (bean.pushtype) {
                case MyCommon.PUSH_TYPE_ACTIVITYFINISH:
                    tvTitle.setText("活动通知");
                    ivType.setImageResource(R.drawable.ic_message_ac);
                    break;
                case MyCommon.PUSH_TYPE_NEWACTIVITY:
                    tvTitle.setText("活动通知");
                    ivType.setImageResource(R.drawable.ic_message_ac);
                    break;
                case MyCommon.PUSH_TYPE_RECOMENDTOINDEX:
                    tvTitle.setText("推荐通知");
                    ivType.setImageResource(R.drawable.ic_message_tu);
                    break;
                case MyCommon.PUSH_TYPE_ADDTOSONGLIST:
                    tvTitle.setText("推荐通知");
                    ivType.setImageResource(R.drawable.ic_message_tu);
                    break;
            }

            tvCreateTime.setText(DateFormatUtils.formatX1(bean.createtime));
            tvContent.setText(bean.content);

        }

        @Override
        public void onItemClick(View view, int position) {

        }

    }

    @Override
    protected void onDestroy() {
        if (systemPresenter != null)
            systemPresenter.cancelRequest();
        super.onDestroy();

    }

}
