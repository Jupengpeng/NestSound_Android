package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.StringUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MusicTalkAdapter extends RecyclerView.Adapter<MusicTalkAdapter.MusicTalkViewHolder> {
    private List<MusicTalk> mList;
    private Context context;
    private int itemWidth, itemHeight;
    public MusicTalkAdapter(Context context, List<MusicTalk> worksDataList) {
        this.context = context;
        this.mList = worksDataList;
        itemWidth = DensityUtil.getScreenW(context)-DensityUtil.dip2px(context,20);
        itemHeight = itemWidth * 191 / 330;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MusicTalkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MusicTalkViewHolder holder = new MusicTalkViewHolder(LayoutInflater.from(context).inflate(R.layout.view_home_musictalk_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MusicTalkViewHolder holder, final int position) {
        MusicTalk musicTalk = mList.get(position);
        if(StringUtil.isNotBlank(musicTalk.pic))
        ImageLoadUtil.loadImage(MyCommon.getImageUrl(musicTalk.pic,itemWidth,itemHeight), holder.ivCover);
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

    class MusicTalkViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_cover)
        SimpleDraweeView ivCover;
        public MusicTalkViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivCover.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemHeight));
        }
    }
}
