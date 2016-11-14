package com.xilu.wybz.ui.cooperation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CollectBean;
import com.xilu.wybz.presenter.CollectPresenter;
import com.xilu.wybz.ui.IView.ICollectView;
import com.xilu.wybz.ui.fragment.BaseFragment;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CollectFragment extends BaseFragment implements ICollectView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.ll_nodata)
    LinearLayout llnoda;
    @Bind(R.id.llnologin)
    LinearLayout llnologin;
    @Bind(R.id.login_bt)
    Button login_bt;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.collect_recyclerview)
    RecyclerView collect_recyclerview;
    CollectPresenter collectPresenter;
    CollectAdapter collectAdapter;
    AlertDialog dialog;
    private List<CollectBean> beanList = new ArrayList<>();
    private int page = 1;
    private boolean isFirst;
    private int currentScrollState;
    private int lastVisibleItemPosition;
    private boolean ishasData = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_collect;
    }

    @Override
    protected void initPresenter() {
        if (isFirst) return;
        else isFirst = true;
        collectPresenter = new CollectPresenter(context, this);

        collectPresenter.init();

    }

    @Override
    public void showCollectList(List<CollectBean> collectBeanList) {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        beanList.addAll(collectBeanList);
        collectAdapter.notifyDataSetChanged();
        if(refreshLayout.isRefreshing()){
            refreshLayout.setRefreshing(false);

        }
    }

    @Override
    public void noData() {
        llnoda.setVisibility(View.VISIBLE);
    }

    @Override
    public void noMoreData() {
        ishasData = false;
        collectAdapter.onLoadMoreStateChanged(false);
    }

    @Override
    public void cancleCollectSuccess(int pos) {
        collectAdapter.removeItem(pos);
        dialog.dismiss();
        if (beanList.size() == 0) {
            llnoda.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            getActivity().finish();
        }
    }
    @Override
    public void initView() {

        if (PrefsUtil.getUserId(context) == 0) {
            llnologin.setVisibility(View.VISIBLE);
        }
        login_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(), LoginActivity.class);
                startActivityForResult(intent,100);
            }
        });
        showLoading(ll_loading);
        collectAdapter = new CollectAdapter(beanList, context);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        collect_recyclerview.setLayoutManager(linearLayoutManager);
        collect_recyclerview.setAdapter(collectAdapter);
        collectPresenter.getCollectList(page);
        refreshLayout.setOnRefreshListener(this);
        collect_recyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                currentScrollState = newState;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 1) && !refreshLayout.isRefreshing() && ishasData) {
                    collectAdapter.onLoadMoreStateChanged(true);
                    page++;
                    collectPresenter.getCollectList(page);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = (linearLayoutManager)
                        .findLastVisibleItemPosition() + 1;
            }
        });
        collectAdapter.setOnItemClickListener(new CollectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int status = beanList.get(position).getStatus();
//                Intent intent = new Intent(getActivity(), CooperaDetailesActivity.class);
                switch (status){
                    case 1:
                        CooperaDetailesActivity.start(context,beanList.get(position).getId(), 0, 1);
                        break;
                    case 3:
                        CooperaDetailesActivity.start(context,beanList.get(position).getId(), 3, 2);
                        break;
                    case 4:
                        CooperaDetailesActivity.start(context,beanList.get(position).getId(), 3, 2);
                        break;
                    case 8:
                        CooperaDetailesActivity.start(context,beanList.get(position).getId(), 3, 2);
                        break;
                }

//                intent.putExtra("did", );
//                startActivity(intent);
            }
        });
        collectAdapter.setOnItemLongClickListener(new CollectAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                dialog = new AlertDialog.Builder(context).setTitle("取消收藏")
                        .setMessage("您确认取消收藏吗？").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                collectPresenter.collect(beanList.get(position).getId(), 0, position);
                            }
                        }).create();
                dialog.show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (collectPresenter != null)
            collectPresenter.cancelRequest();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                beanList.clear();
                page = 1;
                collectPresenter.getCollectList(page);
                ishasData = true;
            }
        }, 2000);
    }
}
