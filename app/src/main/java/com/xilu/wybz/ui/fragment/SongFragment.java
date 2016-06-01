package com.xilu.wybz.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
    private WorksAdapter newWorksAdapter;
    private WorksAdapter hotWorksAdapter;
    private SongPresenter songPresenter;
    int column = 3;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_song;
    }

    @Override
    protected void initPresenter() {
        songPresenter = new SongPresenter(context, this);
        songPresenter.init();
    }

    @Override
    public void showNewSong(List<WorksData> newWorksDatas) {
        for(WorksData worksData:newWorksDatas){
            worksData.status = 1;
        }
        newWorksAdapter = new WorksAdapter(context, newWorksDatas, column, MyCommon.NEWS);
        recyclerViewNew.setAdapter(newWorksAdapter);
        newWorksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toPlayPos(newWorksDatas,position,MyCommon.NEWS);
            }
        });
    }

    @Override
    public void showHotSong(List<WorksData> hotWorksDatas) {
        for(WorksData worksData:hotWorksDatas){
            worksData.status = 1;
        }
        hotWorksAdapter = new WorksAdapter(context, hotWorksDatas, column, MyCommon.RED);
        recyclerViewHot.setAdapter(hotWorksAdapter);
        hotWorksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                toPlayPos(hotWorksDatas,position,MyCommon.RED);
            }
        });
    }

    @Override
    public void showErrorView() {

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
        songPresenter.getWorkList(1);
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
            PlayAudioActivity.toPlayAudioActivity(context, worksData.getItemid(), "", COME, position);
        }
    }
}
