package com.xilu.wybz.ui.msg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.message.PreserveMessageBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.DefaultListPresenter;
import com.xilu.wybz.ui.IView.IDefaultListView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.preserve.PreserveInfoActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/10.
 */

public class MsgPreserveActivity extends BaseListActivity<PreserveMessageBean> implements IDefaultListView<PreserveMessageBean> {


    /**
     * defaultListPresenter.
     */
    DefaultListPresenter<PreserveMessageBean> defaultListPresenter;

    Map<String, String> params;

    @Override
    protected void initPresenter() {

        defaultListPresenter = new DefaultListPresenter<>(context, this);
        defaultListPresenter.setUrl(MyHttpClient.getMsgPreserveList());
        defaultListPresenter.method = "get";
        defaultListPresenter.resultType = new TypeToken<List<PreserveMessageBean>>() {
        }.getType();
        params = new HashMap<>();
        params.put("uid", "" + PrefsUtil.getUserId(context));
        defaultListPresenter.setParams(params);
    }

    @Override
    public void initView() {

    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("保全消息");
        hideRight();
        clearMsg();

        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void clearMsg() {
        EventBus.getDefault().post(new Event.ClearMsgEvent(MyCommon.PUSH_TYPE_COPYRIGH));
        Intent mIntent = new Intent("com.xilu.wybz.intent.CLEARNOTICE");
        mIntent.putExtra("type", MyCommon.PUSH_TYPE_COPYRIGH);
        sendBroadcast(mIntent);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);

        defaultListPresenter.getData(page++);
//        mock();
    }

    private void mock() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PreserveMessageBean bean = new PreserveMessageBean();
                bean.pushtype = MyCommon.PUSH_TYPE_COPYRIGHSUCCESS;
                bean.content = "PUSH_TYPE_COPYRIGHSUCCESS";
                bean.createtime = System.currentTimeMillis();

                mDataList.add(bean);
                mDataList.add(bean);
                mDataList.add(bean);
                mDataList.add(bean);

                bean = new PreserveMessageBean();
                bean.pushtype = MyCommon.PUSH_TYPE_COPYRIGHFAIL;
                bean.content = "PUSH_TYPE_COPYRIGHFAIL";
                bean.createtime = System.currentTimeMillis();

                mDataList.add(bean);
                mDataList.add(bean);
                mDataList.add(bean);
                mDataList.add(bean);
                onSuccess(mDataList);
            }
        }, 300);
    }

    @Override
    public void onSuccess(List<PreserveMessageBean> list) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        mDataList.addAll(list);
        recycler.onRefreshCompleted();
        adapter.notifyDataSetChanged();
        checkData();
    }

    @Override
    public void onError(String message) {

        recycler.onRefreshCompleted();
        checkData();
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

        PreserveMessageBean bean;

        /**
         * @param itemView
         */
        public SampleViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreserveInfoActivity.start(context, bean.orderNo);
                }
            });
        }

        @Override
        public void onBindViewHolder(int position) {

            bean = mDataList.get(position);

            switch (bean.pushtype) {
                case MyCommon.PUSH_TYPE_COPYRIGHSUCCESS:
                    tvTitle.setText("保全成功");
                    ivType.setImageResource(R.drawable.ic_message_pre_ok);
                    break;
                case MyCommon.PUSH_TYPE_COPYRIGHFAIL:
                    tvTitle.setText("保全失败");
                    ivType.setImageResource(R.drawable.ic_message_pre_error);
                    break;
            }

            tvCreateTime.setText(DateFormatUtils.formatX1(bean.createtime));
            tvContent.setText(bean.content);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
