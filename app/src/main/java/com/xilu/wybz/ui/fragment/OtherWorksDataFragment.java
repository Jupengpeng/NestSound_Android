package com.xilu.wybz.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.WorksViewHolder;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.OtherCenterListPresenter;
import com.xilu.wybz.ui.IView.IOtherCenterListView;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;

import java.util.List;


/**
 * Created by hujunwei on 16/6/3.
 */
public class OtherWorksDataFragment extends BaseListFragment<WorksData> implements IOtherCenterListView {
    OtherCenterListPresenter userPresenter;
    public static String TYPE = "type";
    public static String UID = "uid";
    public static String AUTHOR = "author";
    private int type;//1=歌曲，2=歌词，3=收藏,4=合作
    private int userId;
    private String COME;
    private String author;
    private String[] OTHERCOMES = new String[]{"usersong", "userlyrics", "userfav","userRRR"};//他人主页
    private boolean isFirstTab;
    private boolean isFirst;
    @Override
    protected void initPresenter() {
        userPresenter = new OtherCenterListPresenter(context, this);
        userPresenter.init();
    }

    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            type = getArguments().getInt(TYPE);
            userId = getArguments().getInt(UID);
            COME = OTHERCOMES[type-1];
            if (type == 0) isFirstTab = true;
            author = getArguments().getString(AUTHOR);
        }

    }

    /**
     *
     * @return
     */
    protected ILayoutManager getLayoutManager() {
        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager(getActivity().getApplicationContext(), OrientationHelper.VERTICAL, false);
        return myLinearLayoutManager;
    }

    public static OtherWorksDataFragment newInstance(int type, int userId, String author) {
        OtherWorksDataFragment tabFragment = new OtherWorksDataFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type+1);
        bundle.putInt(UID, userId);
        bundle.putString(AUTHOR, author);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }
    @Override
    public void initView() {

    }

    public void loadData() {
        if (isFirst) return;
        else isFirst = true;
        recycler.setRefreshing();
    }
    @Override
    protected void setUpData() {
        super.setUpData();
        if (recycler == null) {
            return;
        }
        if (isFirstTab) {
            recycler.setRefreshing();
            isFirst = true;
        }
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        int questType = type;
        if (type == 4){
            questType = 5;
        }
        userPresenter.loadData(userId, questType, page++);
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isDestroy) return;
                if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
                    mDataList.clear();
                }
                for (WorksData worksData : worksDataList) {
                    if (type < 3){
                        worksData.setAuthor(author);
                    }
                    worksData.type = type;
                    if (type == 4){
                        worksData.type = 5;
                        worksData.author = worksData.getComAuthor();
                    }
                    mDataList.add(worksData);
                }
                llNoData.setVisibility(View.GONE);
                recycler.enableLoadMore(true);
                recycler.getRecyclerView().requestLayout();
                adapter.notifyDataSetChanged();
                recycler.onRefreshCompleted();
            }
        }, 600);
    }


    @Override
    public void loadFail() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadNoMore() {
        if (isDestroy) return;
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if (isDestroy) return;
        if (type == 4){
            mDataList.add(new WorksData());
            mDataList.add(new WorksData());
            mDataList.add(new WorksData());
            mDataList.add(new WorksData());


            for (WorksData worksData : mDataList) {
                if (type < 3){
                    worksData.setAuthor(author);
                }
                worksData.type = type;
                if (type == 4){
                    worksData.type = 5;
                    worksData.author = worksData.getComAuthor();
                }
            }

            adapter.notifyDataSetChanged();
            recycler.onRefreshCompleted();

            return;
        }

        llNoData.setVisibility(View.VISIBLE);
        recycler.onRefreshCompleted();
        recycler.enableLoadMore(false);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {

        View view ;
        if (type == 4){
            view = LayoutInflater.from(context).inflate(R.layout.activity_work_list_item2, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false);
        }

        WorksViewHolder holder = new WorksViewHolder(view, context, mDataList, COME, null);
        return holder;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (userPresenter != null)
            userPresenter.cancelRequest();
    }


    public class CooperationViewHolder extends BaseViewHolder {


        public CooperationViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
