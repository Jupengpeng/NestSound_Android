package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongAlbumViewHolder> {
    private List<WorksData> mDatas;
    private LayoutInflater mInflater;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public SongListAdapter(Context context, List<WorksData> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mContext = context;
    }

    @Override
    public SongAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongAlbumViewHolder holder = new SongAlbumViewHolder(mInflater.inflate(
                R.layout.activity_songalbum_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final SongAlbumViewHolder holder, final int position) {
        WorksData worksData = mDatas.get(position);
        holder.tvAuthor.setTag(worksData);
        holder.tvName.setText(worksData.title);
        holder.tvAuthor.setText(worksData.getAuthor());
        holder.tvNum.setText("" + (position + 1));
        if(worksData.isPlay){
            holder.tvNum.setVisibility(View.GONE);
            holder.ivFlag.setVisibility(View.VISIBLE);
        }else{
            holder.tvNum.setVisibility(View.VISIBLE);
            holder.ivFlag.setVisibility(View.GONE);
        }
        if (mOnItemClickListener != null) {
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

    public void addData(int position, WorksData worksData) {
        mDatas.add(position, worksData);
        notifyItemInserted(position);
    }

    public void removeData(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    class SongAlbumViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.iv_flag)
        ImageView ivFlag;

        public SongAlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
