package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaLyricBean;
import com.xilu.wybz.utils.DateFormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CooperaLyricAdapter extends RecyclerView.Adapter<CooperaLyricAdapter.CooperaLyricViewHolder> {
    private List<CooperaLyricBean> cooperaLyricBeenList;
    private Context context;


    public CooperaLyricAdapter(List<CooperaLyricBean> cooperaLyricBeen, Context context) {
        this.cooperaLyricBeenList = cooperaLyricBeen;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, CooperaLyricBean cooperaLyricBean);
    }

    private CooperaLyricAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CooperaLyricAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CooperaLyricViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CooperaLyricAdapter.CooperaLyricViewHolder holder = new CooperaLyricAdapter.CooperaLyricViewHolder(LayoutInflater.from(context).inflate(R.layout.lyric_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CooperaLyricViewHolder holder, int position) {
        CooperaLyricBean cooperaLyricBean = cooperaLyricBeenList.get(position);
        holder.chooselyric_tv_title.setText(cooperaLyricBean.getTitle());
        holder.chooselyric_tv_time.setText(DateFormatUtils.formatX1(cooperaLyricBean.getCreatetime()));
        if(cooperaLyricBean.getStatus()==1){
            holder.chooselyric_iv_isprivate.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView,pos,cooperaLyricBean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cooperaLyricBeenList.size();
    }

    class CooperaLyricViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.chooselyric_tv_title)
        TextView chooselyric_tv_title;
        @Bind(R.id.chooselyric_tv_time)
        TextView chooselyric_tv_time;
        @Bind(R.id.chooselyric_iv_isprivate)
        ImageView chooselyric_iv_isprivate;


        public CooperaLyricViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
