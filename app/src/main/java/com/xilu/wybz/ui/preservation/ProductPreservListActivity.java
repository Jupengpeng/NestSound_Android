package com.xilu.wybz.ui.preservation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ProductPreservListActivity extends BaseListActivity<Object>{

    String nodata = "没有数据";
    int nodatares = R.drawable.ic_nozan;


    @Override
    protected void initPresenter() {
        initView();
    }


    public void initView() {
        setTitle("保全列表");
        hideRight();

        recycler.enablePullToRefresh(true);
        recycler.enableLoadMore(true);

        tvNoData.setText(nodata);
        ivNoData.setImageResource(nodatares);
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        recycler.setRefreshing();
    }

    @Override
    public void onRefresh(int action) {
        super.onRefresh(action);
//        collectionPresenter.loadData(page++);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                add();
            }
        },400);

    }

    public void add(){

        recycler.onRefreshCompleted();

        mDataList.add("11");
        mDataList.add("11");
        mDataList.add("11");
        mDataList.add("11");
        mDataList.add("11");

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add){

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_preservation_all, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public boolean hasPadding() {
        return false;
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
