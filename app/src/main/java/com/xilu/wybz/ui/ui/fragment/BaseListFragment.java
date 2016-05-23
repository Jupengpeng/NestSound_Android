package com.xilu.wybz.ui.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.fragment.*;
import com.xilu.wybz.view.pull.BaseListAdapter;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyLinearLayoutManager;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hujunwei on 16/5/22.
 */
public abstract class BaseListFragment<T> extends com.xilu.wybz.ui.fragment.BaseFragment implements PullRecycler.OnRecyclerRefreshListener{
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
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_base_list;
    }
    protected abstract void initPresenter();
    protected void setUpData() {
        setUpAdapter();
        recycler.setOnRefreshListener(this);
        recycler.setLayoutManager(getLayoutManager());
//        recycler.addItemDecoration(getItemDecoration());
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
        return new MyLinearLayoutManager(getActivity().getApplicationContext());
    }
    //    protected RecyclerView.ItemDecoration getItemDecoration() {
//        return new DividerItemDecoration(getActivity().getApplicationContext(), R.drawable.list_divider);
//    }
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
    public void clearData(){
        if(mDataList!=null) {
            mDataList.clear();
            keyWord = null;
            adapter.notifyDataSetChanged();
            page = 1;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    protected void loadImage(String url, SimpleDraweeView mDraweeView) {
        ImageRequest request =
                ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                        .setResizeOptions(
                                new ResizeOptions(mDraweeView.getLayoutParams().width, mDraweeView.getLayoutParams().height))
                        .setProgressiveRenderingEnabled(true)
                        .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(mDraweeView.getController())
                .setImageRequest(request)
                .setAutoPlayAnimations(true)
                .build();
        mDraweeView.setController(controller);
    }
}
