package com.xilu.wybz.ui.song;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.HotCatalogViewHolder;
import com.xilu.wybz.bean.HotBean;
import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.presenter.HotCatalogPresenter;
import com.xilu.wybz.ui.IView.IHotCatalogView;
import com.xilu.wybz.ui.base.BaseSectionListActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;
import com.xilu.wybz.view.pull.layoutmanager.ILayoutManager;
import com.xilu.wybz.view.pull.layoutmanager.MyGridLayoutManager;
import com.xilu.wybz.view.pull.section.SectionData;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/8/8.
 */
public class HotCatalogActivity extends BaseSectionListActivity<HotCatalog> implements IHotCatalogView{
    public static final String FLASH_TAG = "FLASH_TAG";
    private int column = 3;
    HotCatalogPresenter hotCatalogPresenter;
    TemplateBean templateBean;
    private boolean flash = false;

    public static void toHotCatalogActivity(Context context, boolean flash){
        Intent intent = new Intent(context,HotCatalogActivity.class);
        intent.putExtra(FLASH_TAG,flash);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null){
            flash = intent.getBooleanExtra(FLASH_TAG,false);
        }
    }

    @Override
    protected void initPresenter() {
        hotCatalogPresenter = new HotCatalogPresenter(this,this);
        hotCatalogPresenter.init();
    }
    @Override
    public void initView() {
        setTitle("伴奏分类");
        hideRight();
        setUpData();
        recycler.enablePullToRefresh(false);
        HotBean hotBean = PrefsUtil.getHotBean(context);
        if(hotBean!=null&&hotBean.simplesing!=null){
            templateBean = hotBean.simplesing;
            mDataList = new ArrayList<>();
            showHotCatalog(hotBean);
        }else{
            recycler.setRefreshing();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ImportHotEvent event) {
        if (flash){
            finish();
        }
    }

    @Override
    protected BaseViewHolder onCreateSectionViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotcatalog_item, parent, false);
        HotCatalogViewHolder holder = new HotCatalogViewHolder(view, context, mDataList, column);
        holder.flash =flash;
        return holder;
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
                        if (flash){
                            EventBus.getDefault().post(new Event.ImportHotEvent(templateBean));
                            finish();
                        } else {
                            MakeSongActivity.toMakeSongActivity(context, templateBean);
                        }
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
        hotCatalogPresenter.loadData(page);
    }

    @Override
    public void showHotCatalog(HotBean hotBean) {
        if(isDestroy)return;
        PrefsUtil.saveHotBean(context,hotBean);
        if(mDataList.size()==0) {
            templateBean = hotBean.simplesing;
            mDataList.add(new SectionData<>(true, 0, "清唱"));
        }
        recycler.enableLoadMore(false);
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
