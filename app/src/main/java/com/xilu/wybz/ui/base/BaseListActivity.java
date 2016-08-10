package com.xilu.wybz.ui.base;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xilu.wybz.R;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.pull.BaseListAdapter;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.DividerItemDecoration;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;
import java.util.ArrayList;
import butterknife.Bind;
/**
 * Created by June on 16/5/8.
 */
public abstract class BaseListActivity<T> extends BasePlayMenuActivity implements PullRecycler.OnRecyclerRefreshListener {
    protected BaseListAdapter adapter;
    protected ArrayList<T> mDataList;
    @Bind(R.id.ll_main)
    protected LinearLayout llMain;
    @Bind(R.id.pullRecycler)
    protected PullRecycler recycler;
    @Bind(R.id.tv_nodata)
    protected TextView tvNoData;
    @Bind(R.id.iv_nodata)
    protected ImageView ivNoData;
    @Bind(R.id.ll_nodata)
    protected LinearLayout llNoData;
    @Bind(R.id.iv_nonet)
    ImageView ivNoNet;
    protected int action;
    protected int page;
    protected int dip10;
    protected int dip5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dip10 = DensityUtil.dip2px(context, 10);
        dip5 = DensityUtil.dip2px(context, 5);
        initPresenter();
        setUpData();
    }
    protected abstract void initPresenter();
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_list;
    }
    protected void setUpData() {
        setUpAdapter();
        if(hasPadding()){
            if(this instanceof BaseSectionListActivity){
                recycler.setReclylerPaddiing(dip5, dip5, dip5, dip5);
            }else{
                recycler.setReclylerPaddiing(dip10, dip10, dip10, dip10);
            }

        }
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        recycler.setAdapter(adapter);
        ivNoNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler.setRefreshing();
            }
        });
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
    }
    public boolean hasPadding() {return true;}
    protected void setUpAdapter() {
        adapter = new ListAdapter();
    }

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getApplicationContext());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    public class ListAdapter extends BaseListAdapter {

        @Override
        protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent, viewType);
        }

        @Override
        protected int getDataCount() {
            return mDataList != null ? mDataList.size() : 0;
        }

        @Override
        protected int getDataViewType(int position) {
            return getItemType(position);
        }

        @Override
        public boolean isSectionHeader(int position) {
            return BaseListActivity.this.isSectionHeader(position);
        }
    }
    public void removeItem(int position){
        mDataList.remove(position);
        adapter.notifyItemRemoved(position);
        if(position != mDataList.size()){
            adapter.notifyItemRangeChanged(position, mDataList.size() - position);
        }
    }
    public void addItem(T t){
        mDataList.add(0,t);
        recycler.setSelection(0);
        adapter.notifyItemInserted(0);
        adapter.notifyItemRangeChanged(0, mDataList.size());
    }
    protected boolean isSectionHeader(int position) {
        return false;
    }
    protected int getItemType(int position) {
        return 0;
    }
    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);

}