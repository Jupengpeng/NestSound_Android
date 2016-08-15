package com.xilu.wybz.ui.market;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.connect.UserInfo;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.StartBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.StarPresenter;
import com.xilu.wybz.ui.IView.IStarView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.base.BaseSectionListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.List;

/**
 * Created by hujunwei on 16/8/12.
 */
public class StarListActivity extends BaseListActivity<StartBean> implements IStarView {
    StarPresenter starPresenter;
    public static void toStarListActivity(Context context){
        Intent intent = new Intent(context,StarListActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void initPresenter() {
        starPresenter = new StarPresenter(context,this);
        starPresenter.init();
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void initView() {
        setTitle("明星音乐人");
        hideRight();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        starPresenter.getStarList(page++);
    }

    @Override
    public void showData(List<StartBean> startBeanList) {
        mDataList.addAll(startBeanList);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(true);
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
        llNoData.setVisibility(View.VISIBLE);
    }
    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_start_list,parent,false);
        return new SimpleViewHolder(view);
    }
    public class SimpleViewHolder extends BaseViewHolder{

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
