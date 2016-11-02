package com.xilu.wybz.ui.cooperation;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CollectBean;
import com.xilu.wybz.presenter.CollectPresenter;
import com.xilu.wybz.ui.IView.ICollectView;
import com.xilu.wybz.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CollectFragment extends BaseFragment implements ICollectView {
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.collect_recyclerview)
    RecyclerView collect_recyclerview;
    CollectPresenter collectPresenter;
    CollectAdapter collectAdapter;
    private List<CollectBean> beanList;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void initPresenter() {
        collectPresenter = new CollectPresenter(context, this);
        showLoading(ll_loading);
        collectPresenter.init();

    }

    @Override
    public void showCollectList(List<CollectBean> collectBeanList) {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        if (beanList == null) collectBeanList = new ArrayList<>();
        if (beanList.size() > 0) beanList.clear();
        beanList.addAll(collectBeanList);
        collectAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        beanList = new ArrayList<>();
        collectAdapter = new CollectAdapter(beanList, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        collect_recyclerview.setLayoutManager(linearLayoutManager);
        collect_recyclerview.setAdapter(collectAdapter);
        collectPresenter.getCollectList();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                collectPresenter.getCollectList();
                refreshLayout.setRefreshing(false);
            }
        });
        collect_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int post =  linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int total =linearLayoutManager.getItemCount();
                int visible = linearLayoutManager.getChildCount();
                if((post+visible)>=total){
                    collectAdapter.onLoadMoreStateChanged(true);
//                    cooperationPresenter.getCooperationList(2);
                }else{
                    collectAdapter.onLoadMoreStateChanged(false);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (collectPresenter != null)
            collectPresenter.cancelRequest();
    }
}
