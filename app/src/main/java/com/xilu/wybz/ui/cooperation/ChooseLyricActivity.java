package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaLyricBean;
import com.xilu.wybz.presenter.CooperaLyricPresenter;
import com.xilu.wybz.ui.IView.ICooperaLyricView;
import com.xilu.wybz.ui.base.ToolbarActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ChooseLyricActivity extends ToolbarActivity implements ICooperaLyricView {
    @Bind(R.id.refreshlayout)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.chooselyric_recyclerview)
    RecyclerView chooselyric_recyclerview;
    AlertDialog dialog;

    private CooperaLyricPresenter cooperaLyricPresenter;
    private List<CooperaLyricBean> lyricbeanList;

    private CooperaLyricAdapter cooperaLyricadapter;
    private int page = 1;
    private CooperaLyricBean lyricBean;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_choose_lyric;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("选择歌词");
        initPresenter();
    }

    private void initDialog(int position, CooperaLyricBean cooperaLyricBean) {
        dialog = new AlertDialog.Builder(ChooseLyricActivity.this).create();
        LayoutInflater layoutInflater = LayoutInflater.from(ChooseLyricActivity.this);
        LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.simidialig, null);
        Button positive_bt = (Button) layout.findViewById(R.id.cancle_bt);
        Button cancle_bt = (Button) layout.findViewById(R.id.positive_bt);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        positive_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancle_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cooperaLyricPresenter.updateLyricStatu(cooperaLyricBean.getItemid(), cooperaLyricBean.getStatus());
            }
        });

    }

    private void initPresenter() {
        cooperaLyricPresenter = new CooperaLyricPresenter(this, this);
        cooperaLyricPresenter.init();
    }

    @Override
    public void showCooperaLyricList(List<CooperaLyricBean> lyricBeanList) {
        if (isDestroy) return;
        if (lyricbeanList == null) lyricBeanList = new ArrayList<>();
        if (lyricbeanList.size() > 0) lyricbeanList.clear();
        lyricbeanList.addAll(lyricBeanList);
        cooperaLyricadapter.notifyDataSetChanged();
    }

    @Override
    public void updatesuccess() {
        startLyricDetailsActivity(lyricBean);//发送post请求将作品至为公开
        dialog.dismiss();
    }

    @Override
    public void initView() {
        lyricbeanList = new ArrayList<>();
        chooselyric_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        cooperaLyricadapter = new CooperaLyricAdapter(lyricbeanList, this);
        chooselyric_recyclerview.setAdapter(cooperaLyricadapter);
        cooperaLyricPresenter.getCooperaLyricList(page);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cooperaLyricPresenter.getCooperaLyricList(page);
                refreshLayout.setRefreshing(false);
            }
        });
        cooperaLyricadapter.setOnItemClickListener(new CooperaLyricAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, CooperaLyricBean cooperaLyricBean) {
                lyricBean = cooperaLyricBean;
                if (cooperaLyricBean.getStatus() == 0) {
                    initDialog(position, lyricBean);

                } else {
                    startLyricDetailsActivity(cooperaLyricBean);
                }

            }
        });

    }

    public void startLyricDetailsActivity(CooperaLyricBean cooperaLyricBean) {
        Intent intent = new Intent(ChooseLyricActivity.this, LyricDetailsActivity.class);
        intent.putExtra("cooperaLyricBean", cooperaLyricBean);
        startActivity(intent);
    }
}
