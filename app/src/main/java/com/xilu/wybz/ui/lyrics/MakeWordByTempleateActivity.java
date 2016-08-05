package com.xilu.wybz.ui.lyrics;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateLrcBean;
import com.xilu.wybz.ui.IView.ITempleateMakeLrcView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class MakeWordByTempleateActivity extends BaseListActivity<TemplateLrcBean> implements ITempleateMakeLrcView {

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
        recycler.enableLoadMore(false);
    }

    @Override
    protected void setUpData() {
        super.setUpData();

        mDataList = new ArrayList<>();

        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
        mDataList.add(new TemplateLrcBean());
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);

        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recycler.onRefreshCompleted();
            }
        }, 800);
    }


    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lrc_template_make, parent, false);
        return new SampleViewHolder(view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.menu.menu_next){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class SampleViewHolder extends BaseViewHolder {

        TextView templateWord;
        TextView lrcWord;
        public SampleViewHolder(View itemView) {
            super(itemView);
            templateWord = (TextView) itemView.findViewById(R.id.lrc_template_temp);
            lrcWord = (TextView) itemView.findViewById(R.id.lrc_template_make);
        }

        @Override
        public void onBindViewHolder(int position) {
            TemplateLrcBean template = mDataList.get(position);
            templateWord.setText(template.template);
            lrcWord.setText(template.lrcWord);
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }
}
