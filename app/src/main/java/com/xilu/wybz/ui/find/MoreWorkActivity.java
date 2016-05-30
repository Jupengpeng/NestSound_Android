package com.xilu.wybz.ui.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.FindMoreWorkPresenter;
import com.xilu.wybz.ui.IView.IFindMoreWorkView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.DividerItemDecoration;
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
    String COME;
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
    public boolean hasPadding() {return true;}
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            workType = bundle.getInt("workType");
            orderType = bundle.getInt("orderType");
        }
        if(workType==1) {
            if (orderType == 1) {
                setTitle("最新歌曲");
                COME = MyCommon.MORENEWS;
            } else if (orderType == 2) {
                setTitle("热门歌曲");
                COME = MyCommon.MORERED;
            }
        }else{
            if (orderType == 1) {
                setTitle("最新歌词");
            } else if (orderType == 2) {
                setTitle("热门歌词");
            }
        }
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
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpacesItemDecoration(dip10);
    }
    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        WorksViewHolder holder = new WorksViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false));
        return holder;
    }
    class WorksViewHolder extends BaseViewHolder {
        int itemWidth;
        @Bind(R.id.iv_cover)
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
            ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
            ImageLoadUtil.loadImage(url, ivCover);
            tvName.setText(worksData.name);
            tvAuthor.setText(worksData.author);
            tvLookNum.setText(NumberUtil.format(worksData.looknum));
            tvZanNum.setText(NumberUtil.format(worksData.zannum));
            tvFovNum.setText(NumberUtil.format(worksData.fovnum));
        }

        @Override
        public void onItemClick(View view, int position) {
            if(workType==1){
                toPlayPos(position);
            }else{
                LyricsdisplayActivity.toLyricsdisplayActivity(context,mDataList.get(position).getItemid(),0,mDataList.get(position).name);
            }
        }
    }
    public void toPlayPos(int position){
        if (mDataList.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom",context);
            if(!playFrom.equals(COME)|| MyApplication.ids.size()==0){
                if (MyApplication.ids.size() > 0)
                    MyApplication.ids.clear();
                for (WorksData worksData : mDataList) {
                    MyApplication.ids.add(worksData.getItemid());
                }
            }
            WorksData worksData = mDataList.get(position);
            PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), "", COME, position);
        }
    }
}
