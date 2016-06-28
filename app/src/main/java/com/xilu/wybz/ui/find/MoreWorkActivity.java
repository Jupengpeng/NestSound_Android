package com.xilu.wybz.ui.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.WorksViewHolder;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.FindMoreWorkPresenter;
import com.xilu.wybz.ui.IView.IFindMoreWorkView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by June on 16/5/7.
 */
public class MoreWorkActivity extends BaseListActivity<WorksData> implements IFindMoreWorkView {
    int orderType;
    int workType;
    String COME;
    FindMoreWorkPresenter findMoreWorkPresenter;
    public boolean hasPadding() {return false;}
    public static void toMoreSongActivity(Context context, int orderType, int workType) {
        Intent intent = new Intent(context, MoreWorkActivity.class);
        intent.putExtra("orderType", orderType);
        intent.putExtra("workType", workType);
        context.startActivity(intent);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    protected void initPresenter() {
        findMoreWorkPresenter = new FindMoreWorkPresenter(context, this);
        findMoreWorkPresenter.init();
    }

    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            workType = bundle.getInt("workType");
            orderType = bundle.getInt("orderType");
        }
        if (workType == 1) {
            if (orderType == 1) {
                setTitle("最新歌曲");
                COME = MyCommon.MORENEWS;
            } else if (orderType == 2) {
                setTitle("热门歌曲");
                COME = MyCommon.MORERED;
            }
        } else {
            if (orderType == 1) {
                COME = MyCommon.MORENEWS;
                setTitle("最新歌词");
            } else if (orderType == 2) {
                COME = MyCommon.MORERED;
                setTitle("热门歌词");
            }
        }
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        findMoreWorkPresenter.findMoreWork(workType, orderType, page++);
    }

    @Override
    public void showWorkData(List<WorksData> worksDataList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isDestroy)return;
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                recycler.enableLoadMore(true);
                for(WorksData worksData : worksDataList){
                    worksData.status = workType;
                    mDataList.add(worksData);
                }
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        },600);
    }

    @Override
    public void loadFail() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if(isDestroy)return;
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

//    protected RecyclerView.ItemDecoration getItemDecoration() {
//        return new SpacesItemDecoration(dip10);
//    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false);
        WorksViewHolder holder = new WorksViewHolder(view, context, mDataList, COME, null);
        return holder;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(findMoreWorkPresenter!=null){
            findMoreWorkPresenter.cancelRequest();
        }
    }
}
