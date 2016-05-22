package com.xilu.wybz.ui.mine.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.view.pull.BaseListAdapter;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class UserBaseView extends LinearLayout {

    public static final int PAGE_ERROR   = 0;
    public static final int PAGE_CONTENT = 1;


    protected Context mContext;
    protected View mNoData;
    protected RecyclerView mPullRecycler;
    protected BaseListAdapter mBaseListAdapter;


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
    public UserBaseView(Context context,BaseListAdapter adapter) {
        super(context);
        this.mContext = context;
        this.mBaseListAdapter = adapter;
        init();
    }


    public void init(){

        View rootView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.view_user_base, null);

        mNoData = rootView.findViewById(R.id.ll_nodata);
        mPullRecycler = (RecyclerView) rootView.findViewById(R.id.pullRecycler);

        mPullRecycler.setLayoutManager(new MyLinearLayoutManager(mContext));
        mPullRecycler.setNestedScrollingEnabled(false);
        if (mBaseListAdapter != null){
            mPullRecycler.setAdapter(mBaseListAdapter);
        }

        show(PAGE_CONTENT);
        addView(rootView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }




    public void show(int id){
        if (id == PAGE_CONTENT){
            mPullRecycler.setVisibility(View.VISIBLE);
            mNoData.setVisibility(View.GONE);

        } else if (id == PAGE_ERROR){
            mPullRecycler.setVisibility(View.GONE);
            mNoData.setVisibility(View.VISIBLE);
        }
    }


    public void setAdapter(BaseListAdapter adapter){
        this.mBaseListAdapter = adapter;
        if (mPullRecycler != null){
            mPullRecycler.setAdapter(mBaseListAdapter);
        }
    }

    public BaseListAdapter getAdapter(){
        return mBaseListAdapter;
    }





}
