package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.presenter.MinePresenter;
import com.xilu.wybz.ui.IView.IMineView;
import com.xilu.wybz.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.xilu.wybz.R.id.cancle_bt;
import static com.xilu.wybz.R.id.positive_bt;

/**
 * Created by Administrator on 2016/10/19.
 */

public class MineFragment extends BaseFragment implements IMineView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @Bind(R.id.tv_nodata)
    TextView tvnodata;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.mine_recyclerview)
    RecyclerView mine_recyclerview;
    MinePresenter minePresenter;
    private List<MineBean> mineList = new ArrayList<>();
    private MineAdapter mineAdapter;
    private int page = 1;
    private AlertDialog dialog;

    private int currentScrollState;
    private int lastVisibleItemPosition;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initPresenter() {
        if (mineList.size() > 0) {
            disMissLoading(ll_loading);
            return;
        }
        minePresenter = new MinePresenter(context, this);
        showLoading(ll_loading);
        minePresenter.init();
    }

    private void initDialog(int pos) {

        dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.deletehezuoxuqiu, null);
        Button cancle_bto = (Button) layout.findViewById(cancle_bt);
        Button positive_bto = (Button) layout.findViewById(positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        cancle_bto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        positive_bto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minePresenter.deleteCoopera(mineList.get(pos).getId(), pos);

            }
        });
    }

    @Override
    public void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mine_recyclerview.setLayoutManager(linearLayoutManager);
        mineAdapter = new MineAdapter(mineList, context);
        mine_recyclerview.setAdapter(mineAdapter);
        if (mineList.size() > 0) {
            mineList.clear();
            page = 1;
        }
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

                initDialog(position);

            }
        });
        refreshLayout.setOnRefreshListener(this);
        mine_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

//                int lastPosition = 0;
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    lastPosition = linearLayoutManager.findLastVisibleItemPosition();
//                    Log.e("AAA", lastPosition + "");
//                }
//                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1 && !refreshLayout.isRefreshing()) {
//                    Log.e("AAA", "日了狗");
//                    mineAdapter.onLoadMoreStateChanged(true);
//                    page++;
//                    minePresenter.getMineList(page);
//                }
                currentScrollState = newState;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                Log.e("AAA1", lastVisibleItemPosition + "");
                Log.e("AAA2", (lastVisibleItemPosition) % 10 + "");
                if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 1) && !refreshLayout.isRefreshing() && ((lastVisibleItemPosition) % 10 == 0)) {
                    mineAdapter.onLoadMoreStateChanged(true);
                    page++;
                    lastVisibleItemPosition = lastVisibleItemPosition - 1;
                    minePresenter.getMineList(page);
                    Log.e("AAA3", (lastVisibleItemPosition) % 10+ "");
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = (linearLayoutManager)
                        .findLastVisibleItemPosition() + 1;
            }
        });
    }

    @Override
    public void showMineList(List<MineBean> mineBeanList) {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        mineList.addAll(mineBeanList);
        mineAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteSuccess(int position) {
        mineAdapter.removeItem(position);
        dialog.dismiss();
        if (mineList.size() ==0) {
            tvnodata.setText("暂无发布作品");
            ll_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noData() {
        tvnodata.setText("暂无发布作品");
        ll_nodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        mineAdapter.onLoadMoreStateChanged(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (minePresenter != null)
            minePresenter.cancelRequest();
    }


    @Override
    public void onRefresh() {
        if (mineList.size() > 0) {
            mineList.clear();
            page = 1;
        }
        minePresenter.getMineList(page);
        refreshLayout.setRefreshing(false);
    }
}
