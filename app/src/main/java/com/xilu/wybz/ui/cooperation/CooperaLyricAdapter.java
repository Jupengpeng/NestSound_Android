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

public class CooperaLyricAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CooperaLyricBean> cooperaLyricBeenList;
    private Context context;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected boolean isLoadMoreFooterShown = false;

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
    public int getItemViewType(int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void onLoadMoreStateChanged(boolean isShown) {
        this.isLoadMoreFooterShown = isShown;
        if (isShown) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        CooperaLyricAdapter.CooperaLyricViewHolder holder = new CooperaLyricAdapter.CooperaLyricViewHolder(LayoutInflater.from(context).inflate(R.layout.lyric_item, parent, false));
//        return holder;
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.lyric_item, null);
            return new CooperaLyricViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CooperaLyricViewHolder) {
            CooperaLyricBean cooperaLyricBean = cooperaLyricBeenList.get(position);
            ((CooperaLyricViewHolder) holder).chooselyric_tv_title.setText(cooperaLyricBean.getTitle());
            ((CooperaLyricViewHolder) holder).chooselyric_tv_time.setText(DateFormatUtils.formatX1(cooperaLyricBean.getCreatetime()));
            if (cooperaLyricBean.getStatus() == 1) {
                ((CooperaLyricViewHolder) holder).chooselyric_iv_isprivate.setVisibility(View.GONE);
            }
            ((CooperaLyricViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = ((CooperaLyricViewHolder) holder).getLayoutPosition();
                    mOnItemClickListener.onItemClick(((CooperaLyricViewHolder) holder).itemView, pos, cooperaLyricBean);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cooperaLyricBeenList.size() + (isLoadMoreFooterShown ? 1 : 0);
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

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }
}
