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
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.bean.PreinfoBean;
import com.xilu.wybz.presenter.CooperationPresenter;
import com.xilu.wybz.ui.IView.ICooperationView;
import com.xilu.wybz.ui.fragment.BaseFragment;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.mine.OtherUserCenterActivity;
import com.xilu.wybz.ui.mine.UserCenterActivity;
import com.xilu.wybz.ui.song.HotCatalogActivity;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CooperationFragment extends BaseFragment implements ICooperationView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.cooperationrecyclerview)
    RecyclerView cooperationrecyclerview;
    @Bind(R.id.more_iv)
    ImageView more_iv;
    private int page = 1;
    CooperationAdapter cooperationAdapter;
    private CooperationPresenter cooperationPresenter;
    private List<CooperationBean> cooperationList = new ArrayList<>();
    AlertDialog dialog;

    private int currentScrollState;
    private int lastVisibleItemPosition;

    private boolean isRefreshing;
    private int did;//合作需求ID

    private boolean ishasData = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_cooperation;

    }

    @Override
    protected void initPresenter() {
        cooperationPresenter = new CooperationPresenter(context, this);
        showLoading(ll_loading);
        cooperationPresenter.init();

    }

    private void initDialog(int pos) {

        dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.hezuotishidialig, null);
        Button positive_bt = (Button) layout.findViewById(R.id.cancle_bt);
        Button cancle_bt = (Button) layout.findViewById(R.id.positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                did = cooperationList.get(pos).getDemandInfo().getId();
                cooperationPresenter.getPreinfo(did);

            }
        });
        positive_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startLyricDetailsActivity(cooperaLyricBean);//发送post请求将作品至为公开
                dialog.dismiss();


            }
        });
    }

    @Override
    public void showCooperation(List<CooperationBean> cooperationBeanList) {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        cooperationList.addAll(cooperationBeanList);
        cooperationAdapter.notifyDataSetChanged();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void noMoreData() {
//        showMsg("没有更多数据");
        ishasData = false;
        cooperationAdapter.onLoadMoreStateChanged(false);
    }

    @Override
    public void showpreinfoBean(PreinfoBean preinfoBean) {
        Intent intent = new Intent(getActivity(), HotCatalogActivity.class);
        intent.putExtra("preinfoBean", preinfoBean);
        intent.putExtra("coopera", 1);
        intent.putExtra("did", did);
        startActivity(intent);
        dialog.dismiss();
        getActivity().finish();
    }

    @Override
    public void noData() {
        ll_nodata.setVisibility(View.VISIBLE);
    }

    public void isLogin() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivityForResult(intent, 100);
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
        cooperationrecyclerview.setLayoutManager(linearLayoutManager);
        cooperationAdapter = new CooperationAdapter(cooperationList, context);
        cooperationrecyclerview.setAdapter(cooperationAdapter);
        cooperationPresenter.getCooperationList(page);
        refreshLayout.setOnRefreshListener(this);
        cooperationrecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                currentScrollState = newState;

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                if ((visibleItemCount > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE &&
                        (lastVisibleItemPosition) >= totalItemCount - 1) && !refreshLayout.isRefreshing() && ishasData) {
                    cooperationAdapter.onLoadMoreStateChanged(true);
                    page++;
                    cooperationPresenter.getCooperationList(page);
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = (linearLayoutManager)
                        .findLastVisibleItemPosition() + 1;
            }
        });
        more_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrefsUtil.getUserId(context) == 0) {
                    isLogin();
                } else {
//                startActivity(new Intent(getActivity(), CooperaPublish.class));
                    Intent intent = new Intent(getActivity(), CooperaPublishActivity.class);
//                getActivity().startActivityForResult(intent, 150);
                    startActivity(intent);
                }
            }
        });


        cooperationAdapter.setOnItemClickListener(new CooperationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type) {

                switch (type) {
                    case 1:
                        if (PrefsUtil.getUserId(context) == 0) {
                            isLogin();
                        } else {
                            Intent intent = new Intent(getActivity(), CooperaDetailesActivity.class);
                            if (cooperationList.get(position).getUserInfo().getUid() == PrefsUtil.getUserId(context)) {
                                intent.putExtra("type", 2);
                            } else {
                                intent.putExtra("type", 1);
                            }
                            intent.putExtra("did", cooperationList.get(position).getId());
                            startActivity(intent);
                        }
                        break;
                    case 2:
                        if (PrefsUtil.getUserId(context) == 0) {
                            isLogin();
                        } else {
                            int uid = cooperationList.get(position).getUserInfo().getUid();
                            String username = cooperationList.get(position).getUserInfo().getNickname();
                            if (uid == PrefsUtil.getUserId(context)) {
                                Intent intent1 = new Intent(getActivity(), UserCenterActivity.class);
                                startActivity(intent1);
                            } else {
                                OtherUserCenterActivity.toUserInfoActivity(context, uid, username);
                            }
                        }
                        break;
                    case 3:
                        if (PrefsUtil.getUserId(context) == 0) {
                            isLogin();
                        } else {
                            initDialog(position);
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cooperationList.size() > 0) {
            cooperationList.clear();
            page = 1;
        }
        cooperationPresenter.getCooperationList(page);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cooperationPresenter != null)
            cooperationPresenter.cancelRequest();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 150 && resultCode == 160) {
//
//            onRefresh();
//        }
//    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cooperationList.size() > 0) {
                    cooperationList.clear();
                }
                page = 1;
                cooperationPresenter.getCooperationList(page);
                ishasData = true;
            }
        }, 2000);
//        isRefreshing = refreshLayout.isRefreshing();
//        if (isRefreshing) {
//            if (cooperationList.size() > 0) {
//                cooperationList.clear();
//                page = 1;
//            }
//            refreshLayout.setRefreshing(false);
//            cooperationPresenter.getCooperationList(page);
//        }
//        ishasData = true;

    }
}
