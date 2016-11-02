package com.xilu.wybz.ui.cooperation;

import android.content.Context;
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

public class MineAdapter extends RecyclerView.Adapter<MineAdapter.MineViewHolder> {
    private List<MineBean> mineBeanList;
    private Context context;


    public MineAdapter(List<MineBean> mineBeanList, Context context) {
        this.mineBeanList = mineBeanList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
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
    public MineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MineAdapter.MineViewHolder holder = new MineAdapter.MineViewHolder(LayoutInflater.from(context).inflate(R.layout.mine_item, parent, false));
        return holder;
    }

    public void removeItem(int pos) {
        mineBeanList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MineViewHolder holder, int position) {
        MineBean mineBean = mineBeanList.get(position);
        if (mineBean != null) {
            holder.mine_tv_lyricsname.setText(mineBean.getTitle());
            holder.mine_tv_time.setText(DateFormatUtils.formatX1(mineBean.getCreatetime()));
            switch (mineBean.getStatus()) {
                case 1:
                    holder.mine_tv_count.setText(mineBean.getWorknum() + "");
                    break;
                case 2:
                    holder.mine_tv_count.setVisibility(View.GONE);
                    holder.mine_tv_success.setVisibility(View.VISIBLE);
                    break;

                case 3:
                    holder.mine_tv_count.setVisibility(View.GONE);
                    holder.mine_tv_expire.setVisibility(View.VISIBLE);
                    break;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mineBeanList.size();
    }

    class MineViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.mine_tv_lyricsname)
        TextView mine_tv_lyricsname;
        @Bind(R.id.mine_tv_time)
        TextView mine_tv_time;
        @Bind(R.id.mine_tv_count)
        TextView mine_tv_count;
        @Bind(R.id.mine_tv_success)
        TextView mine_tv_success;
        @Bind(R.id.mine_tv_expire)
        TextView mine_tv_expire;

        public MineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
