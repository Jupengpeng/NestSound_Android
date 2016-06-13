package com.xilu.wybz.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
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
import butterknife.ButterKnife;

/**
 * Created by hujunwei on 16/5/22.
 */
public abstract class BaseListFragment<T> extends BaseFragment implements PullRecycler.OnRecyclerRefreshListener {
    protected Context context;
    protected BaseListAdapter adapter;
    protected ArrayList<T> mDataList;
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
    String keyWord;
    int action;
    int page = 1;
    int dip10;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        initPresenter();
        setUpData();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
        dip10 = DensityUtil.dip2px(context,10);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_base_list;
    }

    protected abstract void initPresenter();
    public boolean hasPadding() {return true;}
    protected void setUpData() {
        setUpAdapter();
        if(hasPadding()){
//            if(this instanceof WorksDataFragment){
//                recycler.setReclylerPaddiing(dip10, dip10, dip10, dip10+DensityUtil.dip2px(context,48));
//            }else {
                recycler.setReclylerPaddiing(dip10, dip10, dip10, dip10);
//            }
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

    protected void setUpAdapter() {
        adapter = new ListAdapter();
    }

    protected ILayoutManager getLayoutManager() {
        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager(getActivity().getApplicationContext());
        return myLinearLayoutManager;
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getActivity().getApplicationContext(), R.drawable.transparent);
    }
    public void moveToFirst(){
        if(mDataList!=null&&mDataList.size()>0&&recycler!=null){
            recycler.setSelection(0);
        }
    }
    public void removeItem(int position){
        mDataList.remove(position);
        adapter.notifyItemRemoved(position);
        recycler.getRecyclerView().requestLayout();
        if(position != mDataList.size()){
            adapter.notifyItemRangeChanged(position, mDataList.size() - position);
        }
        if(mDataList.size()==0)llNoData.setVisibility(View.VISIBLE);
    }
    public void addItem(T t){
        mDataList.add(0,t);
        recycler.setSelection(0);
        adapter.notifyItemInserted(0);
        adapter.notifyItemRangeChanged(0, mDataList.size());
        llNoData.setVisibility(View.GONE);
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
            return BaseListFragment.this.isSectionHeader(position);
        }
    }

    protected boolean isSectionHeader(int position) {
        return false;
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void clearData() {
        llNoData.setVisibility(View.GONE);
        if (mDataList != null) {
            adapter.notifyItemRangeRemoved(0, mDataList.size());
            recycler.getRecyclerView().requestLayout();
            recycler.enableLoadMore(false);
            mDataList.clear();
            keyWord = null;
            page = 1;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected void loadImage(String url, SimpleDraweeView mDraweeView) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setCallerContext(null)
                .setUri(Uri.parse(url))
                .setOldController(mDraweeView.getController())
                .build();
        mDraweeView.setController(controller);
    }
}
