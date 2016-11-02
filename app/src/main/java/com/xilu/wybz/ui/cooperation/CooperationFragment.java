package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
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
import com.xilu.wybz.presenter.CooperationPresenter;
import com.xilu.wybz.ui.IView.ICooperationView;
import com.xilu.wybz.ui.fragment.BaseFragment;
import com.xilu.wybz.ui.mine.UserCenterActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/10/19.
 */

public class CooperationFragment extends BaseFragment implements ICooperationView {

    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.cooperationrecyclerview)
    RecyclerView cooperationrecyclerview;
    @Bind(R.id.more_iv)
    ImageView more_iv;
    private int page =1;
    CooperationAdapter cooperationAdapter;
    private CooperationPresenter cooperationPresenter;
    private List<CooperationBean> cooperationList;


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

    private void initDialog() {

        AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.hezuotishidialig, null);
        Button positive_bt = (Button) layout.findViewById(R.id.cancle_bt);
        Button cancle_bt = (Button) layout.findViewById(R.id.positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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
        if (cooperationList == null) cooperationBeanList = new ArrayList<>();
        if (cooperationList.size() > 0) cooperationList.clear();
        cooperationList.addAll(cooperationBeanList);

        if(cooperationAdapter.isLoadMoreFooterShown==true){
            cooperationAdapter.onLoadMoreStateChanged(false);
        }
        cooperationAdapter.notifyDataSetChanged();
    }

    @Override
    public void initView() {
        cooperationList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        cooperationrecyclerview.setLayoutManager(linearLayoutManager);
        cooperationAdapter = new CooperationAdapter(cooperationList, context);
        cooperationrecyclerview.setAdapter(cooperationAdapter);
        cooperationPresenter.getCooperationList(1);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshLayout.isRefreshing()) {
                    cooperationPresenter.getCooperationList(1);
                    refreshLayout.setRefreshing(false);

                }
            }
        });
        cooperationrecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int post =  linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int total =linearLayoutManager.getItemCount();
                int visible = linearLayoutManager.getChildCount();
                if((post+visible)>=total){
                    cooperationAdapter.onLoadMoreStateChanged(true);
//                    cooperationPresenter.getCooperationList(2);
                }else{
                    cooperationAdapter.onLoadMoreStateChanged(false);
                }
            }
        });
        more_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CooperaPublish.class));

            }
        });
        cooperationAdapter.setOnItemClickListener(new CooperationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int type) {
                switch (type) {
                    case 1:
                        Intent intent = new Intent(getActivity(), CooperaDetailsActivity.class);

                        intent.putExtra("type", 1);
                        startActivity(intent);

                        break;
                    case 2:
//                    Toast.makeText(context,"jump",Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getActivity(), UserCenterActivity.class);
                        startActivity(intent1);

                        break;
                    case 3:
                        initDialog();
                        break;
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (cooperationPresenter != null)
            cooperationPresenter.cancelRequest();
    }


}
