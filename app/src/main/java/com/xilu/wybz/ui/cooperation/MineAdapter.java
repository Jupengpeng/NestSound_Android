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
                    holder.mine_status.setText("正在进行");
                    holder.mine_status.setTextColor(Color.parseColor("#ffb705"));
                    break;
                case 3:
                    holder.mine_status.setText("已经删除");
                    holder.mine_status.setTextColor(Color.parseColor("#ffb705"));
                    break;

                case 4:
                    holder.mine_status.setText("已经到期");
                    holder.mine_status.setTextColor(Color.parseColor("#ff6161"));

                    break;
                case 8:
                    holder.mine_status.setText("合作成功");
                    holder.mine_status.setTextColor(Color.parseColor("#ffb705"));

                    break;
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemLongClickListener.onItemLongClick(holder.itemView, position);
                return false;
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
        @Bind(R.id.mine_status)
        TextView mine_status;

        public MineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
