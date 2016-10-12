//package com.xilu.wybz.view.viewholder;
//
//import android.content.Context;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.xilu.wybz.R;
//import com.xilu.wybz.adapter.WorksAdapter;
//import com.xilu.wybz.bean.HomeItem;
//import com.xilu.wybz.bean.WorksData;
//import com.xilu.wybz.util.DensityUtil;
//import com.xilu.wybz.view.GridSpacingItemDecoration;
//
//import java.util.List;
//
///**
// * Created by cfanr on 2015/12/5.
// */
//public class WorksHolder {
//    Context content;
//    RecyclerView mRecyclerView;
//    WorksAdapter mAdapter;
//
//    public WorksHolder(Context content, View convertView) {
//        this.content = content;
//        if (convertView != null) {
//            mRecyclerView = (RecyclerView) convertView.findViewById(R.id.recycler_view_works);
//        }
//    }
//
//    public void initView(HomeItem homeItem, int type) {
//        List<WorksData> workList = type == 0 ? homeItem.recommendWorkList : homeItem.newWorkList;
//        mAdapter = new WorksAdapter(content, workList);
//        LinearLayoutManager mLayoutManager = new GridLayoutManager(content, 3);
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, DensityUtil.dip2px(content, 10), false));
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new WorksAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });
//
//    }
//}
