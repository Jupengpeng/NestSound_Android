package com.xilu.wybz.ui.preservation;

import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.PreservationInfo;
import com.xilu.wybz.ui.IView.IDefaultListView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ProductPreservListActivity extends BaseListActivity<PreservationInfo>
        implements IDefaultListView<PreservationInfo> {

    String nodata = "你还未保过全作品";
    int nodatares = R.drawable.ic_nocomment;


    @Override
    protected void initPresenter() {


        initView();
    }


    public void initView() {
        setTitle("保全列表");
        hideRight();
        recycler.getRecyclerView().setBackgroundColor(Color.parseColor("#ffffffff"));
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
                checkData();
            }
        }, 400);
    }

    public void add() {

        recycler.onRefreshCompleted();

        mDataList.add(new PreservationInfo());
        mDataList.add(new PreservationInfo());
        mDataList.add(new PreservationInfo());
        mDataList.add(new PreservationInfo());


        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(List<PreservationInfo> list) {

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startActivity(ProductAllActivity.class);
            return true;
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

        int position;

        public SampleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(PreservInfoActivity.class);
                }
            });
        }

        @Override
        public void onBindViewHolder(int position) {
            this.position = position;

        }

        @Override
        public void onItemClick(View view, int position) {
        }
    }

}
