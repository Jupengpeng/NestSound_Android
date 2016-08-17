package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.ImportWordPresenter;
import com.xilu.wybz.presenter.MyWorkPresenter;
import com.xilu.wybz.ui.IView.IImportWordView;
import com.xilu.wybz.ui.IView.IMyWorkView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/8/16.
 */
public class MyWorkActivity extends BaseListActivity<WorksData> implements IMyWorkView {
    private int page = 1;
    private int action = 0;
    private int type = 0;
    private String nodata = "暂无作品";
    private MyWorkPresenter myWorkPresenter;
    // type 1 歌曲 2歌词
    public static void toMyWorkActivity(Context context, int type) {
        Intent intent = new Intent();
        intent.setClass(context, MyWorkActivity.class);
        intent.putExtra(KeySet.KEY_TYPE, type);
        context.startActivity(intent);
    }
    @Override
    protected void initPresenter() {
        myWorkPresenter = new MyWorkPresenter(context, this);
        myWorkPresenter.init();
    }

    @Override
    public void initView() {
        setTitle("我的作品");
        hideRight();
        tvNoData.setText(nodata);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)type=bundle.getInt(KeySet.KEY_TYPE);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }


    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        myWorkPresenter.loadData(page++, type);
    }

    @Override
    public void showData(List<WorksData> worksDataList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                if (isDestroy){
                    return;
                }
                recycler.enableLoadMore(true);
                mDataList.addAll(worksDataList);
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        },600);
    }

    @Override
    public void loadFail() {
        if (recycler != null){
            recycler.onRefreshCompleted();
        }
    }

    @Override
    public void loadNoMore() {
        if (recycler != null){
            recycler.onRefreshCompleted();
            recycler.enableLoadMore(false);
        }
    }

    @Override
    public void loadNoData() {
        if (recycler != null){
            llNoData.setVisibility(View.VISIBLE);
            recycler.onRefreshCompleted();
            recycler.enableLoadMore(false);
        }
    }

    @Override
    public void attendSuccess() {

    }

    @Override
    public void attendFail() {

    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_match_work, parent, false);
        return new SampleViewHolder(view);
    }



    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.iv_status)
        ImageView ivStatus;
        public SampleViewHolder(View itemView) {
            super(itemView);
        }
        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            if(StringUtils.isNotBlank(worksData.title))
                tvName.setText(worksData.title);
            tvTime.setText(DateTimeUtil.timestamp2Date(worksData.createDate));
            ivStatus.setVisibility(worksData.status==1?View.GONE:View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }
        @Override
        public void onItemClick(View view, int position) {
            new MaterialDialog.Builder(context)
                    .title(getString(R.string.dialog_title))
                    .content("请确认是否继续操作？")
                    .positiveText("发布")
                    .positiveColor(getResources().getColor(R.color.red))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    }).negativeText("取消")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    }).show();
        }
    }
}
