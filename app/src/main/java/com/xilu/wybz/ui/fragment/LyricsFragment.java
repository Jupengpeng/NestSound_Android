package com.xilu.wybz.ui.fragment;

import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.WorksAdapter;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SongPresenter;
import com.xilu.wybz.ui.IView.ISongView;
import com.xilu.wybz.ui.find.MoreWorkActivity;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.GridSpacingItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/5/7.
 */
public class LyricsFragment extends BaseFragment implements ISongView {
    @Bind(R.id.recycler_view_hot)
    RecyclerView recyclerViewHot;
    @Bind(R.id.recycler_view_new)
    RecyclerView recyclerViewNew;
    @Bind(R.id.tv_hotsong)
    TextView tvHotsong;
    @Bind(R.id.tv_newsong)
    TextView tvNewsong;
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    private WorksAdapter newWorksAdapter;
    private WorksAdapter hotWorksAdapter;
    private SongPresenter songPresenter;
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
        songPresenter.init();
    }
    @Override
    public void showNewSong(List<WorksData> newWorksDatas) {
        for (WorksData worksData : newWorksDatas) {
            worksData.status = 2;
        }
        if (newWorksDatas.size() > 0) {
            newWorksAdapter = new WorksAdapter(context, newWorksDatas, column, "");
            recyclerViewNew.setAdapter(newWorksAdapter);
        } else {
            tvNewsong.setVisibility(View.GONE);
        }
        newWorksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = StringUtil.isBlank(newWorksDatas.get(position).name)?newWorksDatas.get(position).title:newWorksDatas.get(position).name;
                LyricsdisplayActivity.toLyricsdisplayActivity(context,newWorksDatas.get(position).itemid, 0, title);
            }
        });
    }

    @Override
    public void showHotSong(List<WorksData> hotWorksDatas) {
        for (WorksData worksData : hotWorksDatas) {
            worksData.status = 2;
        }
        if (hotWorksDatas.size() > 0) {
            hotWorksAdapter = new WorksAdapter(context, hotWorksDatas, column,"");
            recyclerViewHot.setAdapter(hotWorksAdapter);
        } else {
            tvHotsong.setVisibility(View.GONE);
        }
        hotWorksAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String title = StringUtil.isBlank(hotWorksDatas.get(position).name)?hotWorksDatas.get(position).title:hotWorksDatas.get(position).name;
                LyricsdisplayActivity.toLyricsdisplayActivity(context,hotWorksDatas.get(position).itemid, 0, title);
            }
        });
    }

    @Override
    public void showErrorView() {

    }

    @Override
    public void loadingFinish() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disMissLoading(ll_loading);
            }
        },1000-(System.currentTimeMillis()-time));
    }

    @Override
    public void initView() {
        tvNewsong.setText("最新歌词");
        tvHotsong.setText("热门歌词");
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
                songPresenter.getWorkList(2);
            }
        },200);
    }

    @OnClick({R.id.tv_hot_more, R.id.tv_new_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hot_more:
                MoreWorkActivity.toMoreSongActivity(context, 2, 2);
                break;
            case R.id.tv_new_more:
                MoreWorkActivity.toMoreSongActivity(context, 1, 2);
                break;
        }
    }
}
