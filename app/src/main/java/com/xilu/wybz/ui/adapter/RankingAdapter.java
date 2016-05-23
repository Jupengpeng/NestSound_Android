package com.xilu.wybz.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.WorksViewHolder> {
    private List<WorksData> mList;
    private Context context;
    private int itemWidth;
    private int type;
    public RankingAdapter(Context context, List<WorksData> worksDataList,int type) {
        this.context = context;
        this.mList = worksDataList;
        this.type = type;
        itemWidth = DensityUtil.dip2px(context, 86);
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
    public WorksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WorksViewHolder holder = new WorksViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_ranking_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final WorksViewHolder holder, final int position) {
        WorksData worksData = mList.get(position);
//        String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
//        ImageLoadUtil.loadImage(url, holder.ivCover);
//        holder.tvName.setText(worksData.name);
//        holder.tvAuthor.setText(worksData.author);
//        holder.tvLookNum.setText(worksData.looknum);
//        holder.tvUpNum.setText(worksData.upnum);
//        holder.tvFavNum.setText(worksData.fovnum);
        holder.ivType.setImageResource(type == 1 ? R.drawable.ic_raning_play : R.drawable.ic_raning_lyrics);
        holder.tvRank.setText("0"+(position+1));
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

    class WorksViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        @Bind(R.id.iv_type)
        ImageView ivType;
        @Bind(R.id.tv_rank)
        TextView tvRank;
        @Bind(R.id.tv_look_num)
        TextView tvLookNum;
        @Bind(R.id.tv_fov_num)
        TextView tvFovNum;
        @Bind(R.id.tv_zan_num)
        TextView tvZanNum;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        public WorksViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivCover.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
        }
    }
}
