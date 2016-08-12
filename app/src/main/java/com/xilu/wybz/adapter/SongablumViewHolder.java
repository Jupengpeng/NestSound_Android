package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.song.SongAblumActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.*;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/6/15.
 */
public class SongablumViewHolder extends com.xilu.wybz.view.pull.BaseViewHolder {
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    int itemWidth,itemHeight;
    List<SongAlbum> mDataList;
    Context mContext;
    public SongablumViewHolder(View itemView, Context context, List<SongAlbum> songAlbumList) {
        super(itemView);
        mContext = context;
        mDataList = songAlbumList;
        itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 30)) / 2;
        itemHeight = itemWidth * 220 / 334;
        ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
    }
    @Override
    public void onBindViewHolder(int position) {
        SongAlbum songAlbum = mDataList.get(position);
        String url = MyCommon.getImageUrl(songAlbum.getPic(), itemWidth, itemHeight);
        ImageLoadUtil.loadImage(url, ivCover);
        if(StringUtil.isNotBlank(songAlbum.name)) {
            tvDesc.setText("『" + songAlbum.name + "』" + songAlbum.detail);
        }else if(StringUtil.isNotBlank(songAlbum.detail)){
            tvDesc.setText(songAlbum.detail);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,position);
            }
        });
    }
    @Override
    public void onItemClick(View view, int position) {
        SongAblumActivity.toSongAblumActivity(mContext,mDataList.get(position));
    }
}
