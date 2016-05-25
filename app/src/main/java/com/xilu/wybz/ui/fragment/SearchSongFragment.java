package com.xilu.wybz.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.SearchPresenter;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/22.
 */
public class SearchSongFragment extends BaseListFragment<WorksData> implements ISearchView{
    SearchPresenter searchPresenter;
    @Override
    protected void initPresenter() {
        searchPresenter = new SearchPresenter(context,this);
        searchPresenter.init();
    }
    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
    }
    public void loadData(String keyWord){
        if(keyWord==null) {
            this.keyWord = keyWord;
            setUpData();
            recycler.setRefreshing();
        }
    }
    @Override
    public void onRefresh(int action) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        searchPresenter.searchData(userId, keyWord, 1, page++);
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {
        if(mDataList.size()==0){
            EventBus.getDefault().post(new Event.ShowSearchTabEvent());
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(worksDataList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void showUserData(List<UserBean> userBeenList) {

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
        return null;
    }
}
