package com.xilu.wybz.ui.preservation;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.presenter.ActPresenter;
import com.xilu.wybz.ui.IView.IActView;
import com.xilu.wybz.ui.fragment.BaseListFragment;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.DividerItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by June on 16/5/7.
 */
public class ProductsFragment extends BaseListFragment<ActBean> implements IActView {

    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    long time;
    int itemWidth, itemHeight;
    private ActPresenter findPresenter;

    @Override
    protected void initPresenter() {
//        findPresenter = new ActPresenter(context, this);
//        showLoading(ll_loading);
//        findPresenter.init();
        initView();
    }

    @Override
    public void initView() {
//        time = System.currentTimeMillis();
//        findPresenter.getActList(page++);
        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#ffffffff"));
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }


    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void showActList(List<ActBean> actBeanList) {
        if(isDestroy)return;
        if (mDataList == null) mDataList = new ArrayList<>();
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(actBeanList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }
    @Override
    public void showNoData() {
        if(isDestroy)return;
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void showNoMore() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadOver() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isDestroy)return;
                disMissLoading(ll_loading);
            }
        }, 1000 - (System.currentTimeMillis() - time));
    }

    @Override
    public void showErrorView() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
    }

    @Override
    public void onRefresh(int action) {
//        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
//        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
//            page = 1;
//        }
//        findPresenter.getActList(page++);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                add();
            }
        },400);
    }

    public void add(){

        recycler.onRefreshCompleted();

        mDataList.add(new ActBean());
        mDataList.add(new ActBean());
        mDataList.add(new ActBean());
        mDataList.add(new ActBean());
        mDataList.add(new ActBean());
        mDataList.add(new ActBean());
        mDataList.add(new ActBean());
        mDataList.add(new ActBean());
        mDataList.add(new ActBean());

        adapter.notifyDataSetChanged();
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_all, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
        }

        @Override
        public void onItemClick(View view, int position) {
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {

        int split = DensityUtil.dp2px(context,0.6f);
        return new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST,split,Color.parseColor("#f0f0f0"),0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (findPresenter != null)
            findPresenter.cancelRequest();
    }
}
