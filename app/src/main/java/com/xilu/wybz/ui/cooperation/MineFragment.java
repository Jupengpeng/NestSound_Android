package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.presenter.MinePresenter;
import com.xilu.wybz.ui.IView.IMineView;
import com.xilu.wybz.ui.fragment.BaseFragment;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;

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
    @Bind(R.id.llnologin)
    LinearLayout llnologin;
    @Bind(R.id.login_bt)
    Button login_bt;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.mine_recyclerview)
    RecyclerView mine_recyclerview;
    MinePresenter minePresenter;
    private List<MineBean> mineList = new ArrayList<>();
    private MineAdapter mineAdapter;
    private int page = 1;
    private AlertDialog dialog;
    private AlertDialog dialog2;

    private int currentScrollState;
    private int lastVisibleItemPosition;


    private boolean ishasData = true;

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
        if (PrefsUtil.getUserId(context) == 0) {
            llnologin.setVisibility(View.VISIBLE);
        }
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        showLoading(ll_loading);
        if (!StringUtils.isBlank(PrefsUtil.getUserInfo(context).loginToken)) {
            minePresenter.init();
        }
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

    private void initDialog2(int pos) {

        dialog2 = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.deletehezuoxuqiutwo, null);
        Button cancle_bto = (Button) layout.findViewById(cancle_bt);
        Button positive_bto = (Button) layout.findViewById(positive_bt);
        dialog2.show();
        dialog2.getWindow().setContentView(layout);
        cancle_bto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            getActivity().finish();
        }
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
                Intent intent = new Intent(context, CooperaDetailesActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra("did", mineList.get(position).getId());
                startActivity(intent);
            }
        });
        mineAdapter.setOnItemLongClickListener(new MineAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position, int status) {
                if (status == 1) {
                    initDialog(position);
                } else {
                    initDialog2(position);
                }

            }
        });
        refreshLayout.setOnRefreshListener(this);
        mine_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                currentScrollState = newState;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 1) && !refreshLayout.isRefreshing() && ishasData) {
                    mineAdapter.onLoadMoreStateChanged(true);
                    page++;
                    minePresenter.getMineList(page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = (linearLayoutManager)
                        .findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public void showMineList(List<MineBean> mineBeanList) {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        mineList.addAll(mineBeanList);
        mineAdapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void deleteSuccess(int position) {
        mineAdapter.removeItem(position);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (dialog2 != null && dialog2.isShowing()) {
            dialog2.dismiss();
        }
        if (mineList.size() == 0) {
            ll_nodata.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void noData() {
        if(ll_nodata!=null){
        ll_nodata.setVisibility(View.VISIBLE);}
    }

    @Override
    public void noMoreData() {
        ishasData = false;
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mineList.clear();
                page = 1;
                minePresenter.getMineList(page);
                ishasData = true;
            }
        }, 200);
    }


}
