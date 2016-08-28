package com.xilu.wybz.ui.lyrics;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.ImportWordPresenter;
import com.xilu.wybz.ui.IView.IImportWordView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;

/**
 * Created by June on 16/5/13.
 */
public class ImportWordActivity extends BaseListActivity<WorksData> implements IImportWordView {
    private int page = 1;
    private int action = 0;
    String nodata = "暂无歌词";
    private ImportWordPresenter importWordPresenter;


    @Override
    protected void initPresenter() {
        importWordPresenter = new ImportWordPresenter(context, this);
        importWordPresenter.init();
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void initView() {
        setTitle("我的歌词");
        hideRight();
        tvNoData.setText(nodata);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        importWordPresenter.loadData(page++,1);
    }

    @Override
    public void showLyricsData(List<WorksData> worksDataList) {

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
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_importword_item, parent, false);
        return new SampleViewHolder(view);
    }

    class SampleViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;

        public SampleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = mDataList.get(position);
            tvName.setText(worksData.title);
            tvAuthor.setText(worksData.author);
            String pic = worksData.getPic();
            loadImage(pic, ivCover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v,position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            if (position == -1) {
                return;
            }
            EventBus.getDefault().post(new Event.ImportWordEvent(mDataList.get(position)));
            finish();
        }
    }


    public void onDestroy() {
        super.onDestroy();
        if(importWordPresenter!=null)
        importWordPresenter.cancelRequest();
    }
}
