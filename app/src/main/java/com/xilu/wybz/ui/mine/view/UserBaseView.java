package com.xilu.wybz.ui.mine.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.view.pull.BaseListAdapter;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class UserBaseView extends LinearLayout {

//    public static final int PAGE_ERROR   = 0;
//    public static final int PAGE_CONTENT = 1;


    protected Context mContext;
    protected LinearLayout mContentView;
    protected LinearLayout mFooterView;
    protected RecyclerView mPullRecycler;
    protected LinearLayout mNoData;
    protected LinearLayout mNoNet;
    protected UserBaseAdapter mBaseListAdapter;


    public UserBaseView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    /**
     * 构造view必要参数.
     * @param context
     * @param adapter
     */
    public UserBaseView(Context context,UserBaseAdapter adapter) {
        super(context);
        this.mContext = context;
        this.mBaseListAdapter = adapter;
        init();
    }


    public void init(){

        View rootView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.view_user_base, null);

        mContentView = (LinearLayout) rootView.findViewById(R.id.ll_content);
        mPullRecycler = (RecyclerView) rootView.findViewById(R.id.pullRecycler);
        mFooterView = (LinearLayout) rootView.findViewById(R.id.ll_pull_refresh_footer);
        mNoData = (LinearLayout) rootView.findViewById(R.id.ll_nodata);
        mNoNet = (LinearLayout) rootView.findViewById(R.id.ll_nonet);

        mPullRecycler.setLayoutManager(new MyLinearLayoutManager(mContext));
        mPullRecycler.setNestedScrollingEnabled(false);
        if (mBaseListAdapter != null){
            mPullRecycler.setAdapter(mBaseListAdapter);
        }
        showContentView();
        showLoadMoreView();
        addView(rootView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    public void onScrollBottom(){

        Log.d("test","onScrollBottom:"+this.getClass().getSimpleName());

    }

    public void showLoadMoreView(){

        mFooterView.setVisibility(View.VISIBLE);
    }

    public void cancelLoadMoreView(){

        mFooterView.setVisibility(View.GONE);
    }


    public void showContentView() {
        mContentView.setVisibility(View.VISIBLE);
        mNoData.setVisibility(View.GONE);
        mNoNet.setVisibility(GONE);
    }

    public void showNoDataView() {
        mContentView.setVisibility(View.GONE);
        mNoData.setVisibility(View.VISIBLE);
        mNoNet.setVisibility(GONE);
    }

    public void showNoNetView() {
        mContentView.setVisibility(View.GONE);
        mNoData.setVisibility(GONE);
        mNoNet.setVisibility(VISIBLE);
    }


    public void setAdapter(UserBaseAdapter adapter){
        this.mBaseListAdapter = adapter;
        if (mPullRecycler != null){
            mPullRecycler.setAdapter(mBaseListAdapter);
        }
    }

    public UserBaseAdapter getAdapter(){
        return mBaseListAdapter;
    }


    public void notifyUpdateData(List<WorksData> data){

        if (mBaseListAdapter != null){
            mBaseListAdapter.updateDatas(data);
        }
    }

    public void notifyAddData(List<WorksData> data){

        if (mBaseListAdapter != null){
            mBaseListAdapter.addDatas(data);
        }
    }





}
