package com.xilu.wybz.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.HomeWorksViewHolder;
import com.xilu.wybz.bean.FindSongBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SongPresenter;
import com.xilu.wybz.ui.IView.ISongView;
import com.xilu.wybz.ui.find.MoreWorkActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;
import com.xilu.wybz.view.pull.section.SectionData;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by June on 16/5/7.
 */
public class SongFragment extends BaseSectionListFragment<WorksData> implements ISongView{
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    private SongPresenter songPresenter;
    private int column = 3;
    private int type = 1;
    private boolean isFirst = true;
    @Override
    protected void initPresenter() {
        songPresenter = new SongPresenter(context, this);
    }

    public void loadData(){
//        if (!isFirst){
//            return;
//        }
//        else {
//            isFirst = false;
//        }
        if (mDataList != null && mDataList.size()>0){
            disMissLoading(ll_loading);
            return;
        }
        showLoading(ll_loading);
        songPresenter.init();
    }

    @Override
    protected BaseViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.find_work_item, parent, false);
        return new HomeWorksViewHolder(view, context, mDataList, column);
    }

    @Override
    public void initView() {
        songPresenter.getWorkList(1);
    }
    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        songPresenter.getWorkList(1);
    }

    @Override
    public void showFindSong(FindSongBean findSongBean) {
        if (isDestroy) return;
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        if (mDataList.size()>0) mDataList.clear();
        if(findSongBean.redList!=null) {
            mDataList.add(new SectionData(true, 1, "热门歌曲"));
            for (WorksData worksData : findSongBean.redList) {
                worksData.type = 1;
                worksData.come = MyCommon.RED;
                mDataList.add(new SectionData(worksData));
            }
        }
        if(findSongBean.newList!=null) {
            mDataList.add(new SectionData(true, mDataList.size(), "最新歌曲"));
            for (WorksData worksData : findSongBean.newList) {
                worksData.type = 1;
                worksData.come = MyCommon.NEWS;
                mDataList.add(new SectionData(worksData));
            }
        }
        adapter.notifyDataSetChanged();
        disMissLoading(ll_loading);
    }

    @Override
    public void showErrorView() {
        if (isDestroy) return;
        disMissLoading(ll_loading);
    }

    @Override
    public void loadingFinish() {
        disMissLoading(ll_loading);
        if (isDestroy) return;
        recycler.enableLoadMore(false);
        recycler.onRefreshCompleted();
    }
//
    protected ILayoutManager getLayoutManager() {
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(getActivity().getApplicationContext(),column);
        return myGridLayoutManager;
    }
    public BaseViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.find_song_item_header, parent, false);
        return new SectionHeaderViewHolder(view);
    }
    public class SectionHeaderViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_more)
        TextView tvMore;
        @Bind(R.id.tv_song)
        TextView tvSong;

        public SectionHeaderViewHolder(View view) {
            super(view);
        }
        @Override
        public void onBindViewHolder(int position) {
            tvSong.setText(mDataList.get(position).header);
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(position==0){
                        MoreWorkActivity.toMoreSongActivity(context, 2, type);
                    }else{
                        MoreWorkActivity.toMoreSongActivity(context, 1, type);
                    }
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (songPresenter != null)
            songPresenter.cancelRequest();
    }
}
