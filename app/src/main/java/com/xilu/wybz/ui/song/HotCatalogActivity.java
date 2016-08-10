package com.xilu.wybz.ui.song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.HomeWorksViewHolder;
import com.xilu.wybz.adapter.HotCatalogViewHolder;
import com.xilu.wybz.bean.HotBean;
import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.presenter.HotCatalogPresenter;
import com.xilu.wybz.ui.IView.IHotCatalogView;
import com.xilu.wybz.ui.base.BaseSectionListActivity;
import com.xilu.wybz.ui.find.MoreWorkActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;
import com.xilu.wybz.view.pull.section.SectionData;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/8/8.
 */
public class HotCatalogActivity extends BaseSectionListActivity<HotCatalog> implements IHotCatalogView{
    private int page = 1;
    private int action = 0;
    private int column = 3;
    HotCatalogPresenter hotCatalogPresenter;
    TemplateBean templateBean;
    @Override
    protected void initPresenter() {
        hotCatalogPresenter = new HotCatalogPresenter(this,this);
        hotCatalogPresenter.init();
    }
    @Override
    public void initView() {
        setTitle("伴奏分类");
        hideRight();
        recycler.enablePullToRefresh(false);
        HotBean hotBean = PrefsUtil.getHotBean(context);
        if(hotBean!=null){
            templateBean = hotBean.simplesing;
            showHotCatalog(hotBean);
        }
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    protected BaseViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotcatalog_item, parent, false);
        return new HotCatalogViewHolder(view, context, mDataList, column);
    }

    public BaseViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.hot_headview, parent, false);
        return new SectionHeaderViewHolder(view);
    }
    public class SectionHeaderViewHolder extends BaseViewHolder {
        @Bind(R.id.iv_qc)
        SimpleDraweeView ivQc;
        public SectionHeaderViewHolder(View view) {
            super(view);
        }
        @Override
        public void onBindViewHolder(int position) {
            if(templateBean!=null){
                loadImage(templateBean.pic,ivQc);
            }else{
                loadImage("res:///"+R.drawable.ic_qc_bg,ivQc);
            }
            ivQc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(templateBean!=null) {
                        MakeSongActivity.ToMakeSongActivity(context, templateBean);
                    }
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    protected ILayoutManager getLayoutManager() {
        MyGridLayoutManager myGridLayoutManager = new MyGridLayoutManager(context, column);
        return myGridLayoutManager;
    }
    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
        hotCatalogPresenter.loadData(page++);
    }

    @Override
    public void showHotCatalog(HotBean hotBean) {
        if(isDestroy)return;
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            mDataList.clear();
        }
        if(page==1){
            PrefsUtil.saveHotBean(context,hotBean);
        }
        if(mDataList.size()==0) {
            templateBean = hotBean.simplesing;
            mDataList.add(new SectionData<>(true, 0, "清唱"));
        }
        if(hotBean.list.size()<12){
            recycler.enableLoadMore(false);
        }else{
            recycler.enableLoadMore(true);
        }
        for(HotCatalog hotCatalog:hotBean.list){
            mDataList.add(new SectionData<>(hotCatalog));
        }
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
    }

    @Override
    public void loadFail() {
        if(isDestroy)return;
        recycler.onRefreshCompleted();
        showNetErrorMsg();
    }

    @Override
    public void loadNoMore() {
        if(isDestroy)return;
        recycler.enableLoadMore(false);
    }

    @Override
    public void loadNoData() {
        if(isDestroy)return;
        llNoData.setVisibility(View.VISIBLE);
    }
}
