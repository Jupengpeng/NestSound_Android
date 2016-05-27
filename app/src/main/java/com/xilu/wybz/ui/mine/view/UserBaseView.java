package com.xilu.wybz.ui.mine.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.presenter.UserListPresenter;
import com.xilu.wybz.ui.IView.IUserListView;
import com.xilu.wybz.ui.mine.Adapter.UserBaseAdapter;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;

import java.util.List;

/**
 * Created by Administrator on 2016/5/21.
 */
public class UserBaseView extends LinearLayout implements IUserListView {

//    public static final int PAGE_ERROR   = 0;
//    public static final int PAGE_CONTENT = 1;


    protected Context mContext;
    protected LinearLayout mContentView;
    protected LinearLayout mFooterView;
    protected RecyclerView mPullRecycler;
    protected LinearLayout mNoData;
    protected LinearLayout mNoNet;
    protected TextView mNoDataText;
    protected TextView mNoNetText;
    protected UserBaseAdapter mBaseListAdapter;

    private UserListPresenter userListPresenter;
    private OnReLoadListener onReLoadListener;


    public UserBaseView(Context context) {
        super(context);
        this.mContext = context;
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
    }

    @Override
    public void initView() {

        View rootView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.view_user_base, null);

        mContentView = (LinearLayout) rootView.findViewById(R.id.ll_content);
        mPullRecycler = (RecyclerView) rootView.findViewById(R.id.pullRecycler);
        mFooterView = (LinearLayout) rootView.findViewById(R.id.ll_pull_refresh_footer);
        mNoData = (LinearLayout) rootView.findViewById(R.id.ll_nodata);
        mNoNet = (LinearLayout) rootView.findViewById(R.id.ll_nonet);
        mNoDataText = (TextView) mNoData.findViewById(R.id.tv_nodata);
        mNoNetText = (TextView) mNoData.findViewById(R.id.tv_nonet);

        ImageView ivNoNet = (ImageView) mNoNet.findViewById(R.id.iv_nonet);
        ivNoNet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onReLoadListener != null){
                    onReLoadListener.onReload();
                }
            }
        });

        mPullRecycler.setLayoutManager(new MyLinearLayoutManager(mContext));
        mPullRecycler.setNestedScrollingEnabled(false);
        if (mBaseListAdapter != null){
            mPullRecycler.setAdapter(mBaseListAdapter);
        }
        showContentView();
        showLoadMore();
        addView(rootView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    @Override
    public void addListDatas(List<WorksData> datas) {
        if (mBaseListAdapter != null){
            mBaseListAdapter.addDatas(datas);
        }
    }

    @Override
    public void refreshListDatas(List<WorksData> datas) {
        if (mBaseListAdapter != null){
            mBaseListAdapter.updateDatas(datas);
        }
    }

    @Override
    public void showNoNet() {
        showNoNetView();
    }

    @Override
    public void showNoData() {
        showNoDataView();
    }

    @Override
    public void showContent() {
        showContentView();
    }

    @Override
    public void showLoadMore() {
        mFooterView.setVisibility(View.VISIBLE);
    }

    @Override
    public void cancelLoadMore() {
        mFooterView.setVisibility(View.GONE);
    }

    public void onScrollBottom(){

        Log.d("test","onScrollBottom:"+this.getClass().getSimpleName());

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

    public void setNoDataText(String text){

        mNoDataText.setText(text);
    }
    public void setNoNetText(String text){

        mNoNetText.setText(text);
    }
    public UserListPresenter getUserListPresenter() {
        return userListPresenter;
    }

    public void setUserListPresenter(UserListPresenter userListPresenter) {
        this.userListPresenter = userListPresenter;
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


    public void setOnReLoadListener(OnReLoadListener listener){
        this.onReLoadListener = listener;
    }

    interface OnReLoadListener{
        void onReload();
    }


}
