package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.presenter.MinePresenter;
import com.xilu.wybz.ui.IView.IMineView;
import com.xilu.wybz.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/19.
 */

public class MineFragment extends BaseFragment implements IMineView {
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.mine_recyclerview)
    RecyclerView mine_recyclerview;
    MinePresenter minePresenter;
    private List<MineBean> mineList;
    private MineAdapter mineAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initPresenter() {
        minePresenter = new MinePresenter(context, this);
        showLoading(ll_loading);
        minePresenter.init();
    }

    @Override
    public void initView() {
        mineList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mine_recyclerview.setLayoutManager(linearLayoutManager);
        mineAdapter = new MineAdapter(mineList, context);
        mine_recyclerview.setAdapter(mineAdapter);
        minePresenter.getMineList();
        mineAdapter.setOnItemClickListener(new MineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context,CooperaDetailsActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                minePresenter.getMineList();
                refreshLayout.setRefreshing(false);
            }
        });
        mine_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
               int post =  linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int total =linearLayoutManager.getItemCount();
                int visible = linearLayoutManager.getChildCount();
                if((post+visible)>=total){
                    minePresenter.getMineList();
                }

            }
        });
    }
    @Override
    public void showMineList(List<MineBean> mineBeanList) {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        if (mineList == null) mineBeanList = new ArrayList<>();
        if (mineList.size() > 0) mineList.clear();
        mineList.addAll(mineBeanList);
        mineAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (minePresenter != null)
            minePresenter.cancelRequest();
    }


}
