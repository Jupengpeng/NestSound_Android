package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.utils.DateFormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class MineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MineBean> mineBeanList;
    private Context context;
    private int itemWidth;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected boolean isLoadMoreFooterShown = false;

    public MineAdapter(List<MineBean> mineBeanList, Context context) {
        this.mineBeanList = mineBeanList;
        this.context = context;

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position,int status);
    }

    private MineAdapter.OnItemClickListener mOnItemClickListener;
    private MineAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(MineAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(MineAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MineAdapter.MineViewHolder holder = new MineAdapter.MineViewHolder(LayoutInflater.from(context).inflate(R.layout.mine_item, parent, false));
//        return holder;
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.mine_item, null);
            return new MineViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }


        return null;
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
    public int getItemCount() {
        return mineBeanList.size() + (isLoadMoreFooterShown ? 1 : 0);
    }

    public void removeItem(int pos) {
        mineBeanList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MineViewHolder) {
            MineBean mineBean = mineBeanList.get(position);
            if (mineBean != null) {
                ((MineViewHolder) holder).mine_tv_lyricsname.setText(mineBean.getTitle());
                ((MineViewHolder) holder).mine_tv_time.setText(DateFormatUtils.formatX1(mineBean.getCreatetime()));
                switch (mineBean.getStatus()) {
                    case 1:
                        ((MineViewHolder) holder).mine_status.setText("正在进行");
                        ((MineViewHolder) holder).mine_status.setTextColor(Color.parseColor("#ffb705"));
                        break;
                    case 3:
                        ((MineViewHolder) holder).mine_status.setText("已经删除");
                        ((MineViewHolder) holder).mine_status.setTextColor(Color.parseColor("#ffb705"));
                        break;

                    case 4:
                        ((MineViewHolder) holder).mine_status.setText("已经到期");
                        ((MineViewHolder) holder).mine_status.setTextColor(Color.parseColor("#ff6161"));

                        break;
                    case 8:
                        ((MineViewHolder) holder).mine_status.setText("合作成功");
                        ((MineViewHolder) holder).mine_status.setTextColor(Color.parseColor("#ffb705"));

                        break;
                }
            }
            ((MineViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
            ((MineViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, position,mineBean.getStatus());
                    return false;
                }
            });

        }
    }


    class MineViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.mine_tv_lyricsname)
        TextView mine_tv_lyricsname;
        @Bind(R.id.mine_tv_time)
        TextView mine_tv_time;
        @Bind(R.id.mine_status)
        TextView mine_status;

        public MineViewHolder(View itemView) {
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
