package com.xilu.wybz.ui.ui.fragment;

import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.SearchPresenter;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunwei on 16/5/22.
 */
public class SearchUserFragment extends BaseListFragment<UserBean> implements ISearchView{
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
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        return null;
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
        searchPresenter.searchData(userId, keyWord, 3, page++);
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {

    }

    @Override
    public void showUserData(List<UserBean> userBeenList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(userBeenList);
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
}
