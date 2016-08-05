package com.xilu.wybz.ui.lyrics;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateListItemBean;
import com.xilu.wybz.ui.IView.ITempleateListLrcView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class LyricsTemplateListActivity extends BaseListActivity<TemplateListItemBean> implements ITempleateListLrcView {


    @Override
    protected void initPresenter() {
        initView();
    }

    @Override
    public void initView() {
        setTitle("模板");
        hideRight();
        tvNoData.setText("没有数据");


        recycler.enablePullToRefresh(false);
        recycler.enableLoadMore(true);
    }

    @Override
    protected void setUpData() {
        super.setUpData();


        mDataList = new ArrayList<>();

        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
        mDataList.add(new TemplateListItemBean());
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.onRefreshCompleted();
            }
        },800);
    }

    @Override
    public boolean hasPadding() {
        return false;
    }


    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lrc_template, parent, false);
        return new SampleViewHolder(view);
    }



    class SampleViewHolder extends BaseViewHolder {

        public SampleViewHolder(View itemView) {
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
