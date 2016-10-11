package com.xilu.wybz.ui.msg;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.message.PreserveMessageBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.DefaultListPresenter;
import com.xilu.wybz.ui.IView.IDefaultListView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/10.
 */

public class MsgPreserveActivity extends BaseListActivity<PreserveMessageBean> implements IDefaultListView<PreserveMessageBean>{




    DefaultListPresenter<PreserveMessageBean> defaultListPresenter;

    Map<String,String> params;
    @Override
    protected void initPresenter() {

        defaultListPresenter = new DefaultListPresenter<>(context,this);
        defaultListPresenter.setUrl(MyHttpClient.getMsgPreserveList());
        params = new HashMap<>();
        params.put("uid", ""+PrefsUtil.getUserId(context));
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
        mock();
    }

    private void mock(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDataList.add(new PreserveMessageBean());
                mDataList.add(new PreserveMessageBean());
                mDataList.add(new PreserveMessageBean());
                mDataList.add(new PreserveMessageBean());
                mDataList.add(new PreserveMessageBean());
                mDataList.add(new PreserveMessageBean());
                mDataList.add(new PreserveMessageBean());
                mDataList.add(new PreserveMessageBean());
                onSuccess(mDataList);
            }
        },300);
    }

    @Override
    public void onSuccess(List<PreserveMessageBean> list) {

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
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_create_time)
        TextView tvCreateTime;

        View root;

        /**
         *
         * @param itemView
         */
        public SampleViewHolder(View itemView) {
            super(itemView);
            root = itemView;
            int itemWidth = DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 30);
            int itemHeight = itemWidth * 101 / 330;

        }


        @Override
        public void onBindViewHolder(int position) {
//            SystemBean systemBean = mDataList.get(position);
//            tvCreateTime.setText(DateTimeUtil.timestamp2DateTime(systemBean.createdate));
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onItemClick(v, position);
//                }
//            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
