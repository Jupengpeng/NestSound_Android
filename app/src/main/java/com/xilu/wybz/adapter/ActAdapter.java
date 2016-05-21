package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ActAdapter extends RecyclerView.Adapter<ActAdapter.SongAlbumViewHolder> {
    private List<ActBean> mDatas;
    private Context context;
    private LayoutInflater mInflater;
    private int itemWidth, itemHeight;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
    public ActAdapter(Context context, List<ActBean> datas) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        itemWidth = DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, 20);
        itemHeight = itemWidth * 173 / 330 ;
    }

    @Override
    public SongAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongAlbumViewHolder holder = new SongAlbumViewHolder(mInflater.inflate(
                R.layout.activity_find_actlist_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final SongAlbumViewHolder holder, final int position) {
        ActBean actBean = mDatas.get(position);
        holder.mDraweeView.setTag(actBean);
        String url = actBean.getPic();
        ImageLoadUtil.loadImage(url, holder.mDraweeView);
        if(mOnItemClickListener!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(int position, ActBean actBean) {
        mDatas.add(position, actBean);
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
            mDraweeView.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemHeight));
        }
    }
}
