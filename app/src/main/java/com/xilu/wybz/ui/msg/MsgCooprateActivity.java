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
import com.xilu.wybz.ui.cooperation.CooperaDetailesActivity;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

import static com.xilu.wybz.common.MyCommon.PUSH_TYPE_COO;
import static com.xilu.wybz.common.MyCommon.PUSH_TYPE_COO_ACCESS;
import static com.xilu.wybz.common.MyCommon.PUSH_TYPE_COO_COMPLETE;
import static com.xilu.wybz.common.MyCommon.PUSH_TYPE_COO_EXPIRE;
import static com.xilu.wybz.common.MyCommon.PUSH_TYPE_COO_INVITE;
import static com.xilu.wybz.common.MyCommon.PUSH_TYPE_COO_LEAVE;
import static com.xilu.wybz.common.MyCommon.PUSH_TYPE_COO_LEAVEREPLY;

/**
 * Created by Administrator on 2016/10/10.
 */

public class MsgCooprateActivity extends BaseListActivity<PreserveMessageBean> implements IDefaultListView<PreserveMessageBean> {


    /**
     * defaultListPresenter.
     */
    DefaultListPresenter<PreserveMessageBean> defaultListPresenter;

    Map<String, String> params;

    @Override
    protected void initPresenter() {

        defaultListPresenter = new DefaultListPresenter<>(context, this);
        defaultListPresenter.setUrl(MyHttpClient.getMsgCooperatelist());
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
        setTitle("合作消息");
        hideRight();
        clearMsg();

        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    public void clearMsg() {
        EventBus.getDefault().post(new Event.ClearMsgEvent(PUSH_TYPE_COO));
        Intent mIntent = new Intent("com.xilu.wybz.intent.CLEARNOTICE");
        mIntent.putExtra("type", PUSH_TYPE_COO);
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
                bean.pushtype = PUSH_TYPE_COO_COMPLETE;
                bean.content = "PUSH_TYPE_COO_COMPLETE";
                bean.createtime = System.currentTimeMillis();

                mDataList.add(bean);
                bean = new PreserveMessageBean();
                bean.pushtype = PUSH_TYPE_COO_INVITE;
                bean.content = "PUSH_TYPE_COO_COMPLETE";
                bean.createtime = System.currentTimeMillis();
                mDataList.add(bean);
                bean = new PreserveMessageBean();
                bean.pushtype = PUSH_TYPE_COO_ACCESS;
                bean.content = "PUSH_TYPE_COO_COMPLETE";
                bean.createtime = System.currentTimeMillis();
                mDataList.add(bean);
                bean = new PreserveMessageBean();
                bean.pushtype = MyCommon.PUSH_TYPE_COO_EXPIRE;
                bean.content = "PUSH_TYPE_COO_COMPLETE";
                bean.createtime = System.currentTimeMillis();
                mDataList.add(bean);

                bean = new PreserveMessageBean();
                bean.pushtype = MyCommon.PUSH_TYPE_COO_EXPIRE;
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
                    if (StringUtils.isNotBlank(bean.headerUrl)) {

                    }
                    if (bean.did == 0) {
                        ToastUtils.toast(context, "错误合作ID");
                        return;
                    }

                    switch (bean.pushtype) {
                        case PUSH_TYPE_COO_COMPLETE:
                            CooperaDetailesActivity.start(context, bean.did, 3, 2);
                            break;
                        case PUSH_TYPE_COO_INVITE:
                            CooperaDetailesActivity.start(context, bean.did, 0, 2);
                            break;
                        case PUSH_TYPE_COO_ACCESS:
                            CooperaDetailesActivity.start(context, bean.did, 3, 2);
                            break;
                        case PUSH_TYPE_COO_EXPIRE:
                            CooperaDetailesActivity.start(context, bean.did, 3, 2);
                            break;
                        case PUSH_TYPE_COO_LEAVE:
                            CooperaDetailesActivity.start(context, bean.did, 0, 2);
                            break;
                        case PUSH_TYPE_COO_LEAVEREPLY:
                            CooperaDetailesActivity.start(context, bean.did, 0, 2);
                            break;
                    }
                }
            });
        }

        @Override
        public void onBindViewHolder(int position) {

            bean = mDataList.get(position);

            switch (bean.pushtype) {
                case PUSH_TYPE_COO_COMPLETE:
                    tvTitle.setText("合作完成");
                    ivType.setImageResource(R.drawable.ic_hezuowancheng);
                    break;
                case PUSH_TYPE_COO_INVITE:
                    tvTitle.setText("合作邀请");
                    ivType.setImageResource(R.drawable.ic_hezuoyaoqing);
                    break;
                case PUSH_TYPE_COO_ACCESS:
                    tvTitle.setText("合作采纳");
                    ivType.setImageResource(R.drawable.ic_hezuocaina);
                    break;
                case MyCommon.PUSH_TYPE_COO_EXPIRE:
                    tvTitle.setText("合作到期");
                    ivType.setImageResource(R.drawable.ic_hezuodaoqi);
                    break;
                case MyCommon.PUSH_TYPE_COO_LEAVE:
                    tvTitle.setText("合作消息");
                    ivType.setImageResource(R.drawable.ic_hezuo_liuyan);
//                    loadImage(bean.headerUrl,ivType);
                    break;
                case MyCommon.PUSH_TYPE_COO_LEAVEREPLY:
                    tvTitle.setText("合作消息");
                    ivType.setImageResource(R.drawable.ic_hezuo_liuyan);
//                    loadImage(bean.headerUrl,ivType);
                    break;
            }

            tvCreateTime.setText(DateFormatUtils.formatX1(bean.createtime));
//            if (StringUtils.isNotBlank(bean.title)) {
//                tvTitle.setText(bean.title);
//            }
            tvContent.setText(bean.content);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
