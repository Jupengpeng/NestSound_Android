package com.xilu.wybz.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.service.PlayService;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.ToastUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorksAdapter extends RecyclerView.Adapter<WorksAdapter.WorksViewHolder> {
    private List<WorksData> mList;
    private Context context;
    private int itemWidth;
    public WorksAdapter(Context context, List<WorksData> worksDataList, int column, String come) {
        this.context = context;
        this.mList = worksDataList;
        itemWidth = (DensityUtil.getScreenW(context) - DensityUtil.dip2px(context, (column + 1) * 10)) / column;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public WorksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WorksViewHolder holder = new WorksViewHolder(LayoutInflater.from(context).inflate(R.layout.view_home_work_item, parent, false));
        return holder;
    }
    @Override
    public void onBindViewHolder(WorksViewHolder holder, int position) {
        WorksData worksData = mList.get(position);
        String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
        ImageLoadUtil.loadImage(url, holder.ivCover);
        if(StringUtil.isNotBlank(worksData.name)) {
            holder.tvName.setText(worksData.name);
        }else{
            holder.tvName.setText(worksData.title);
        }
        holder.tvAuthor.setText(worksData.author);
        holder.tvCount.setText(worksData.looknum + "");
        holder.ivType.setImageResource(worksData.status == 1 ? R.drawable.ic_song_type : R.drawable.ic_lyric_type);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
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
        @Bind(R.id.tv_count)
        TextView tvCount;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.rl_cover)
        RelativeLayout rlCover;

        public WorksViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivCover.setLayoutParams(new RelativeLayout.LayoutParams(itemWidth, itemWidth));
            rlCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
        }
    }
}
