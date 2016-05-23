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
//import com.xilu.wybz.adapter.MusicTalkAdapter;
//import com.xilu.wybz.adapter.WorksAdapter;
//import com.xilu.wybz.bean.HomeItem;
//import com.xilu.wybz.bean.MusicTalk;
//import com.xilu.wybz.bean.WorksData;
//import com.xilu.wybz.util.DensityUtil;
//import com.xilu.wybz.util.ToastUtils;
//import com.xilu.wybz.view.GridSpacingItemDecoration;
//
//import java.util.List;
//
///**
// * Created by cfanr on 2015/12/5.
// */
//public class MusicTalkHolder {
//    Context context;
//    RecyclerView mRecyclerView;
//    MusicTalkAdapter mAdapter;
//
//    public MusicTalkHolder(Context context, View convertView){
//        this.context=context;
//        if(convertView!=null){
//            mRecyclerView=(RecyclerView)convertView.findViewById(R.id.recycler_view_works);
//        }
//    }
//    public void initView(HomeItem homeItem){
//        List<MusicTalk> musicTalkList=homeItem.musicTalkList;
//        mAdapter=new MusicTalkAdapter(context,musicTalkList);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new MusicTalkAdapter.OnItemClickListener() {
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
