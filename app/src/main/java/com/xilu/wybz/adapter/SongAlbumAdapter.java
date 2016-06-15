package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SongAlbumAdapter extends RecyclerView.Adapter<SongAlbumAdapter.SongAlbumViewHolder> {
    private List<SongAlbum> mDatas;
    private Context context;
    private LayoutInflater mInflater;
    private int itemWidth, itemHeight;
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }
    private OnItemClickLitener mOnItemClickListener;
    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public SongAlbumAdapter(Context context, List<SongAlbum> datas) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 30)) / 2;
        itemHeight = itemWidth * 220 / 334;
    }

    @Override
    public SongAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongAlbumViewHolder holder = new SongAlbumViewHolder(mInflater.inflate(
                R.layout.view_home_songalbum_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(final SongAlbumViewHolder holder, final int position) {
        SongAlbum songAlbum = mDatas.get(position);
        holder.ivCover.setTag(songAlbum);
        String url = MyCommon.getImageUrl(songAlbum.getPic(), itemWidth, itemHeight);
        ImageLoadUtil.loadImage(url, holder.ivCover);
        if(StringUtil.isNotBlank(songAlbum.name)) {
            holder.tvDesc.setText("『" + songAlbum.name + "』" + songAlbum.detail);
        }else if(StringUtil.isNotBlank(songAlbum.detail)){
            holder.tvDesc.setText(songAlbum.detail);
        }
        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class SongAlbumViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.tv_desc)
        TextView tvDesc;
        public SongAlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        }
    }
}
