//package com.xilu.wybz.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//
//import com.xilu.wybz.R;
//import com.xilu.wybz.bean.HomeItem;
//import com.xilu.wybz.bean.MusicTalk;
//import com.xilu.wybz.view.viewholder.MusicTalkHolder;
//import com.xilu.wybz.view.viewholder.SongAblumHolder;
//import com.xilu.wybz.view.viewholder.WorksHolder;
//
//import java.util.List;
//
///**
// * Created by June on 16/5/3.
// */
//public class HomeAdapter extends BaseAdapter {
//    private Context context;
//    private List<HomeItem> homeItemList;
//
//    public HomeAdapter(Context context, List<HomeItem> homeItemList) {
//        this.context = context;
//        this.homeItemList = homeItemList;
//    }
//
//    @Override
//    public int getCount() {
//        return homeItemList.size(); //4个
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return homeItemList == null ? null : homeItemList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        HomeItem homeItem = homeItemList.get(position);
//        LayoutInflater inflater = LayoutInflater.from(context);
//        WorksHolder worksHolder;
//        SongAblumHolder songAblumHolder;
//        MusicTalkHolder musicTalkHolder;
//        int type = homeItem.itemType.getValue();
//        switch (type) {
//            case 0:
//                if (convertView == null) {
//                    convertView = inflater.inflate(R.layout.view_home_work, null);
//                    worksHolder = new WorksHolder(context, convertView);
//                    convertView.setTag(worksHolder);
//                } else {
//                    worksHolder = (WorksHolder) convertView.getTag();
//                }
//                worksHolder.initView(homeItem,0);
//                break;
//            case 1:
//                if (convertView == null) {
//                    convertView = inflater.inflate(R.layout.view_home_work, null);
//                    songAblumHolder = new SongAblumHolder(context, convertView);
//                    convertView.setTag(songAblumHolder);
//                } else {
//                    songAblumHolder = (SongAblumHolder) convertView.getTag();
//                }
//                songAblumHolder.initView(homeItem);
//                break;
//            case 2:
//                if (convertView == null) {
//                    convertView = inflater.inflate(R.layout.view_home_work, null);
//                    worksHolder = new WorksHolder(context, convertView);
//                    convertView.setTag(worksHolder);
//                } else {
//                    worksHolder = (WorksHolder) convertView.getTag();
//                }
//                worksHolder.initView(homeItem,2);
//                break;
//            case 3:
//                if (convertView == null) {
//                    convertView = inflater.inflate(R.layout.view_home_work, null);
//                    musicTalkHolder = new MusicTalkHolder(context, convertView);
//                    convertView.setTag(musicTalkHolder);
//                } else {
//                    musicTalkHolder = (MusicTalkHolder) convertView.getTag();
//                }
//                musicTalkHolder.initView(homeItem);
//                break;
//        }
//        return convertView;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (homeItemList != null && position < homeItemList.size()) {
//            return homeItemList.get(position).itemType.getValue();
//        }
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 4;
//    }
//
//}
