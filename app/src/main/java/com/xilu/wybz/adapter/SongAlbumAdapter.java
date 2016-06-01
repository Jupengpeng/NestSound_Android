package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;

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
        itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 25)) / 2;
        itemHeight = itemWidth * 21 / 32;
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
        holder.mDraweeView.setTag(songAlbum);
        String url = songAlbum.pic;
        ImageLoadUtil.loadImage(url, holder.mDraweeView);
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

    public void addData(int position, SongAlbum songAlbum) {
        mDatas.add(position, songAlbum);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    class SongAlbumViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView mDraweeView;
        public SongAlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mDraweeView.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        }
    }
}
