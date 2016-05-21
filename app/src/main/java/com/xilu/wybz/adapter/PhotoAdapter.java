package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<PhotoBean> mList;
    private Context context;
    private int itemWidth;
    public PhotoAdapter(Context context, List<PhotoBean> worksDataList) {
        this.context = context;
        this.mList = worksDataList;
        itemWidth = (DensityUtil.getScreenW(context)-5*DensityUtil.dip2px(context,10))/4;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder holder = new PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_selectpic_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        PhotoBean photoBean = mList.get(position);
        ImageLoadUtil.loadImage("file:///"+photoBean.path, holder.ivCover);
        holder.ivChecked.setImageResource(photoBean.isCheched?R.drawable.ic_checked:R.drawable.ic_check);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
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
        return mList.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.rl_cover)
        RelativeLayout rlCover;
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_check)
        ImageView ivChecked;
        public PhotoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rlCover.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemWidth));
            ivCover.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
        }
    }
}
