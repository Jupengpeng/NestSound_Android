package com.xilu.wybz.ui.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.WorksAdapter;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SongPresenter;
import com.xilu.wybz.ui.IView.ISongView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.find.MoreWorkActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/5/7.
 */
public class SongFragment extends BaseFragment implements ISongView {
    @Bind(R.id.recycler_view_hot)
    RecyclerView recyclerViewHot;
    @Bind(R.id.recycler_view_new)
    RecyclerView recyclerViewNew;
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private WorksAdapter newWorksAdapter;
    private WorksAdapter hotWorksAdapter;
    private SongPresenter songPresenter;
    private List<WorksData> newSongDatas;
    private List<WorksData> hotSongDatas;
    private int column = 3;
    private boolean isFirst;
    private long time;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_song;
    }

    @Override
    protected void initPresenter() {
        songPresenter = new SongPresenter(context, this);
        showLoading(ll_loading);
    }
    public void loadData(){
        if (isFirst) return;
        else isFirst = true;
        newSongDatas = new ArrayList<>();
        hotSongDatas = new ArrayList<>();
        newWorksAdapter = new WorksAdapter(context, newSongDatas, column, MyCommon.NEWS);
        recyclerViewNew.setAdapter(newWorksAdapter);
        hotWorksAdapter = new WorksAdapter(context, hotSongDatas, column, MyCommon.RED);
        recyclerViewHot.setAdapter(hotWorksAdapter);
        newWorksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toPlayPos(newSongDatas,position,MyCommon.NEWS);
            }
        });
        hotWorksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toPlayPos(hotSongDatas,position,MyCommon.RED);
            }
        });
        songPresenter.init();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                songPresenter.getWorkList(1);
            }
        });
    }
    @Override
    public void showNewSong(List<WorksData> newWorksDatas) {
        if(isDestroy)return;
        if(newSongDatas==null){
            newSongDatas = new ArrayList<>();
        }else{
            if(newWorksDatas.size()>0)newSongDatas.clear();
        }
        for(WorksData worksData:newWorksDatas){
            worksData.status = 1;
            newSongDatas.add(worksData);
        }
        newWorksAdapter.notifyDataSetChanged();
    }

    @Override
    public void showHotSong(List<WorksData> hotWorksDatas) {
        if(isDestroy)return;
        if(hotSongDatas==null){
            hotSongDatas = new ArrayList<>();
        }else{
            if(hotSongDatas.size()>0)hotSongDatas.clear();
        }
        for(WorksData worksData:hotWorksDatas){
            worksData.status = 1;
            hotSongDatas.add(worksData);
        }
        hotWorksAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorView() {
    }

    @Override
    public void loadingFinish() {
        if(isDestroy)return;
        if(swipeRefreshLayout!=null)
            swipeRefreshLayout.setRefreshing(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disMissLoading(ll_loading);
            }
        },1000-(System.currentTimeMillis()-time));
    }

    @Override
    public void initView() {
        int space10 = DensityUtil.dip2px(context, 10);
        recyclerViewHot.setNestedScrollingEnabled(false);
        recyclerViewHot.setLayoutManager(new GridLayoutManager(context, column));
        recyclerViewHot.addItemDecoration(new GridSpacingItemDecoration(column, space10, false));
        recyclerViewNew.setNestedScrollingEnabled(false);
        recyclerViewNew.setLayoutManager(new GridLayoutManager(context, column));
        recyclerViewNew.addItemDecoration(new GridSpacingItemDecoration(column, space10, false));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                time = System.currentTimeMillis();
                songPresenter.getWorkList(1);
            }
        },200);
    }

    @OnClick({R.id.tv_hot_more, R.id.tv_new_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hot_more:
                MoreWorkActivity.toMoreSongActivity(context, 2, 1);
                break;
            case R.id.tv_new_more:
                MoreWorkActivity.toMoreSongActivity(context, 1, 1);
                break;
        }
    }
    public void toPlayPos(List<WorksData> worksDataList,int position,String COME){
        if (worksDataList.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom",context);
            if(!playFrom.equals(COME)|| MyApplication.ids.size()==0){
                if (MyApplication.ids.size() > 0)
                    MyApplication.ids.clear();
                for (WorksData worksData : worksDataList) {
                    MyApplication.ids.add(worksData.getItemid());
                }
            }
            WorksData worksData = worksDataList.get(position);
            PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), "", COME);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(songPresenter!=null)
            songPresenter.cancelRequest();
    }
}
