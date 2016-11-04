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
    private int page = 1;

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
        minePresenter.getMineList(page);
        mineAdapter.setOnItemClickListener(new MineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context, CooperaDetailsActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("did", mineList.get(position).getId());
                startActivity(intent);
            }
        });
        mineAdapter.setOnItemLongClickListener(new MineAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                minePresenter.deleteCoopera(mineList.get(position).getId(), position);
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                minePresenter.getMineList(1);
                refreshLayout.setRefreshing(false);
            }
        });
        mine_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastPosition = -1;

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                }
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                int post = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
//                int total = linearLayoutManager.getItemCount();
//                int visible = linearLayoutManager.getChildCount();
//                if ((post + visible) >= total) {
//                    minePresenter.getMineList(page++);
//                }

            }
        });
    }

    @Override
    public void showMineList(List<MineBean> mineBeanList) {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        if (mineList == null) mineBeanList = new ArrayList<>();
        if (page == 1) mineList.clear();
        mineList.addAll(mineBeanList);
        mineAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteSuccess(int position) {
        mineAdapter.removeItem(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (minePresenter != null)
            minePresenter.cancelRequest();
    }


}
