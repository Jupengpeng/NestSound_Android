package com.xilu.wybz.ui.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.FindMoreWorkPresenter;
import com.xilu.wybz.ui.IView.IFindMoreWorkView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by June on 16/5/7.
 */
public class MoreWorkActivity extends BaseListActivity<WorksData> implements IFindMoreWorkView {
    int orderType;
    int workType;
    FindMoreWorkPresenter findMoreWorkPresenter;

    public static void toMoreSongActivity(Context context, int orderType, int workType) {
        Intent intent = new Intent(context, MoreWorkActivity.class);
        intent.putExtra("orderType", orderType);
        intent.putExtra("workType", workType);
        context.startActivity(intent);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    protected void initPresenter() {
        findMoreWorkPresenter = new FindMoreWorkPresenter(context, this);
        findMoreWorkPresenter.init();
    }

    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            workType = bundle.getInt("workType");
            orderType = bundle.getInt("orderType");
        }
        if (orderType == 1) {
            setTitle("最新歌曲");
        } else if (orderType == 2) {
            setTitle("热门歌曲");
        }
        int dpi10 = DensityUtil.dip2px(context, 10);
        recycler.setPadding(dpi10, dpi10, dpi10, dpi10);
    }

    @Override
    public void onRefresh(int action) {
        this.action = action;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        findMoreWorkPresenter.findMoreWork(workType, orderType, page++);
    }

    @Override
    public void showWorkData(List<WorksData> worksDataList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(worksDataList);
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadFail() {
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        WorksViewHolder holder = new WorksViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false));
        return holder;
    }
    class WorksViewHolder extends BaseViewHolder {
        int itemWidth;
        SimpleDraweeView ivCover;
        @Bind(R.id.tv_look_num)
        TextView tvLookNum;
        @Bind(R.id.tv_fov_num)
        TextView tvFovNum;
        @Bind(R.id.tv_zan_num)
        TextView tvZanNum;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        public WorksViewHolder(View view) {
            super(view);
            itemWidth = DensityUtil.dip2px(context, 66);
            ivCover.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
            ImageLoadUtil.loadImage(url, ivCover);
            tvName.setText(worksData.name);
            tvAuthor.setText(worksData.author);
            tvLookNum.setText(worksData.looknum+"");
            tvZanNum.setText(worksData.zannum+"");
            tvFovNum.setText(worksData.fovnum+"");
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
