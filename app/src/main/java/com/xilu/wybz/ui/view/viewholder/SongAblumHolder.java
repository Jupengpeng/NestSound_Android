package com.xilu.wybz.ui.view.viewholder;//package com.xilu.wybz.view.viewholder;
//
//import android.content.Context;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.xilu.wybz.R;
//import com.xilu.wybz.adapter.SongAlbumAdapter;
//import com.xilu.wybz.adapter.WorksAdapter;
//import com.xilu.wybz.bean.HomeItem;
//import com.xilu.wybz.bean.SongAlbum;
//import com.xilu.wybz.util.DensityUtil;
//import com.xilu.wybz.view.GridSpacingItemDecoration;
//
//import java.util.List;
//
///**
// * Created by cfanr on 2015/12/5.
// */
//public class SongAblumHolder {
//    Context context;
//    RecyclerView mRecyclerView;
//    SongAlbumAdapter mAdapter;
//
//    public SongAblumHolder(Context context, View convertView) {
//        this.context = context;
//        if (convertView != null) {
//            mRecyclerView = (RecyclerView) convertView.findViewById(R.id.recycler_view_works);
//        }
//    }
//
//    public void initView(HomeItem homeItem) {
//        List<SongAlbum> songAlbums = homeItem.songAlbumList;
//        mAdapter = new SongAlbumAdapter(context, songAlbums);
//        LinearLayoutManager mLayoutManager = new GridLayoutManager(context, 2);
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, DensityUtil.dip2px(context, 10), false));
//        // 设置item动画
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickLitener(new SongAlbumAdapter.OnItemClickLitener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        });
//    }
//}
