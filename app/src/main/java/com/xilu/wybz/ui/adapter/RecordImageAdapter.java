package com.xilu.wybz.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class RecordImageAdapter extends RecyclerView.Adapter<RecordImageAdapter.RecordImageViewHolder> {
    private List<PhotoBean> mList;
    private Context context;
    private int itemWidth, itemHeight;
    public RecordImageAdapter(Context context, List<PhotoBean> imagePaths, int column) {
        this.context = context;
        this.mList = imagePaths;
        itemWidth = (DensityUtil.getScreenW(context)-DensityUtil.dip2px(context,(column+1)*10))/column;
        itemHeight = itemWidth;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
        void onDelClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public RecordImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecordImageViewHolder holder = new RecordImageViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_delpic_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecordImageViewHolder holder, final int position) {
        if(mList.get(position).isAddPic){
            ImageLoadUtil.loadImage("res://yinchao/"+R.drawable.ic_record_add_pic, holder.ivCover);
            holder.ivDel.setVisibility(View.GONE);
        }else{
            String imgPath = mList.get(position).path;
            ImageLoadUtil.loadImage("file:///"+imgPath, holder.ivCover);
            holder.ivDel.setVisibility(View.VISIBLE);
        }
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
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onDelClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class RecordImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.rl_cover)
        RelativeLayout rlCover;
        @Bind(R.id.iv_del)
        ImageView ivDel;
        public RecordImageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivCover.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemHeight));
            rlCover.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        }
    }
}
