package com.xilu.wybz.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.InspireRecordViewHolder;
import com.xilu.wybz.adapter.WorksViewHolder;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.UserPresenter;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.SpacesItemDecoration;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/6/3.
 */
public class WorksDataFragment extends BaseListFragment<WorksData> implements IUserView {
    UserPresenter userPresenter;
    public static String TYPE = "type";
    public static String UID = "uid";
    public static String AUTHOR = "author";
    private int type;
    private int userId;
    private String COME;
    private String author;
    private String[] COMES = new String[]{"myrecord", "mysong", "mylyrics", "myfav"};

    @Override
    protected void initPresenter() {
        userPresenter = new UserPresenter(context, this);
        userPresenter.init();
    }
    @Override
    public boolean hasPadding() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE);
            userId = getArguments().getInt(UID);
            if(userId!= PrefsUtil.getUserId(context)){
                type = type+1;
            }
            author = getArguments().getString(AUTHOR);
        }
        COME = COMES[type];
    }

    public static WorksDataFragment newInstance(int type, int userId, String author) {
        WorksDataFragment tabFragment = new WorksDataFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE, type);
        bundle.putInt(UID, userId);
        bundle.putString(AUTHOR, author);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new SpacesItemDecoration(dip10);
    }

    @Override
    public void initView() {
        recycler.enablePullToRefresh(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
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
        userPresenter.loadData(userId, type, page++);
    }

    @Override
    public void setUserInfo(UserBean userBean) {
        EventBus.getDefault().post(new Event.UpdataUserBean(userBean,PrefsUtil.getUserId(context)==userId?1:2));
    }

    @Override
    public void showWorksData(List<WorksData> worksDataList) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }

        for (WorksData worksData : worksDataList) {
            if(type<3)
                worksData.setAuthor(author);
            if(type==0){
                worksData.status = 4;
            }else if(type==1){
                worksData.status = 1;
            }else if(type==2){
                worksData.status = 2;
            }else if(type==3){
                worksData.status = worksData.type;
            }
        }
        recycler.enableLoadMore(true);
        mDataList.addAll(worksDataList);
        adapter.notifyDataSetChanged();
        recycler.setAdapter(adapter);
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
    public void deleteWorksData(int pos){
        mDataList.remove(pos);
        adapter.notifyDataSetChanged();
        recycler.getRecyclerView().requestLayout();
    }
    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        if(type==0){
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_inspirerecord_item, parent, false);
            InspireRecordViewHolder holder = new InspireRecordViewHolder(view, context, mDataList, COME, new InspireRecordViewHolder.OnDeleteListener() {
                @Override
                public void deletePos(int pos) {
                    deleteWorksData(pos);
                }
            });
            return holder;
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.activity_work_list_item, parent, false);
            WorksViewHolder holder = new WorksViewHolder(view, context, mDataList, COME, new WorksViewHolder.OnDeleteListener() {
                @Override
                public void deletePos(int pos) {
                    deleteWorksData(pos);
                }
            });
            return holder;
        }
    }
}
