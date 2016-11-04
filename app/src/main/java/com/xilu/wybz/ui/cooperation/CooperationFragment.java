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

import com.umeng.socialize.utils.Log;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.presenter.CooperationPresenter;
import com.xilu.wybz.ui.IView.ICooperationView;
import com.xilu.wybz.ui.fragment.BaseFragment;
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

public class CooperationFragment extends BaseFragment implements ICooperationView {

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
    private List<CooperationBean> cooperationList;

    int mLastVisibleItem;
    int mFirstVisibleItem;

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
                Intent intent = new Intent(getActivity(), HotCatalogActivity.class);
                intent.putExtra("lyric", cooperationList.get(pos).getDemandInfo().getLyrics());
                intent.putExtra("title", cooperationList.get(pos).getDemandInfo().getTitle());
                intent.putExtra("iusername", cooperationList.get(pos).getUserInfo().getNickname());
                intent.putExtra("coopera", 1);
                intent.putExtra("did", cooperationList.get(pos).getDemandInfo().getId());
                intent.putExtra("iuid", cooperationList.get(pos).getDemandInfo().getUid());
                startActivity(intent);
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
        if (cooperationList == null) cooperationList = new ArrayList<>();
        if (page == 1) {
            cooperationList.clear();
        }
        cooperationList.addAll(cooperationBeanList);
        Log.e("AAA", cooperationList.size() + "");
        cooperationAdapter.notifyDataSetChanged();
    }

    @Override
    public void noMoreData() {
        showMsg("没有更多数据");
        cooperationAdapter.onLoadMoreStateChanged(false);
    }

    @Override
    public void initView() {
        cooperationList = new ArrayList<>();
//        if (cooperationList.size() > 0) {
//            cooperationList.clear();
//        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        cooperationrecyclerview.setLayoutManager(linearLayoutManager);
        cooperationAdapter = new CooperationAdapter(cooperationList, context);
        cooperationrecyclerview.setAdapter(cooperationAdapter);
        cooperationPresenter.getCooperationList(page);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (refreshLayout.isRefreshing()) {
//                    cooperationList.clear();
//                    cooperationPresenter.getCooperationList(page);
                    refreshLayout.setRefreshing(false);

                }
            }
        });
        cooperationrecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = -1;

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                }
                if (lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    cooperationAdapter.onLoadMoreStateChanged(true);
                    cooperationPresenter.getCooperationList(++page);

                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                mFirstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition();

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
                        intent.putExtra("did", cooperationList.get(position).getId());
                        intent.putExtra("lyric", cooperationList.get(position).getDemandInfo().getLyrics());
                        intent.putExtra("title", cooperationList.get(position).getDemandInfo().getTitle());
                        intent.putExtra("iusername", cooperationList.get(position).getUserInfo().getNickname());
                        intent.putExtra("iuid", cooperationList.get(position).getDemandInfo().getUid());
                        startActivity(intent);
                        break;
                    case 2:
//                    Toast.makeText(context,"jump",Toast.LENGTH_SHORT).show();
                        int uid = cooperationList.get(position).getUserInfo().getUid();
                        String username = cooperationList.get(position).getUserInfo().getNickname();
                        if (uid == PrefsUtil.getUserId(context)) {
                            Intent intent1 = new Intent(getActivity(), UserCenterActivity.class);
                            startActivity(intent1);
                        } else {
                            OtherUserCenterActivity.toUserInfoActivity(context, uid, username);
                        }
                        break;
                    case 3:
                        initDialog(position);
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
