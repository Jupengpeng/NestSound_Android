package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.utils.DateFormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CompleteListAdapter extends RecyclerView.Adapter<CompleteListAdapter.CompleteListViewHolder> {
    private List<CooperaDetailsBean.CompleteListBean> CompleteListBean;
    private Context context;
    private int flag;

    public CompleteListAdapter(List<CooperaDetailsBean.CompleteListBean> CompleteListBean, Context context, int flag) {
        this.CompleteListBean = CompleteListBean;
        this.context = context;
        this.flag = flag;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    private CompleteListAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CompleteListAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CompleteListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CompleteListAdapter.CompleteListViewHolder holder = new CompleteListAdapter.CompleteListViewHolder(LayoutInflater.from(context).inflate(R.layout.completework_item, parent, false));
        return holder;
    }


    @Override
    public void onBindViewHolder(CompleteListViewHolder holder, int position) {
        CooperaDetailsBean.CompleteListBean completeBean = CompleteListBean.get(position);
        if (completeBean != null) {
                holder.complete_title.setText("歌曲名: "+completeBean.getTitle());
            holder.complete_lUsername.setText("作词    : "+completeBean.getLUsername());
            holder.complete_wUsername.setText("作曲    : "+completeBean.getWUsername());
            holder.complete_createtime.setText(DateFormatUtils.formatX1(completeBean.getCreatetime()));
            ZnImageLoader.getInstance().displayImage(completeBean.getPic(), ZnImageLoader.getInstance().headOptions, holder.complete_iv);

            if (completeBean.getAccess() == 1) {
                holder.complete_isaccess_bt.setBackgroundResource(R.drawable.finishbt_bg);
                holder.complete_isaccess_bt.setText("已采纳");
                holder.complete_isaccess_bt.setEnabled(false);
            }
            if (flag == 1) {
                holder.complete_isaccess_bt.setVisibility(View.GONE);
            }
            if (completeBean.getAccess() != 1) {
                holder.complete_isaccess_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.complete_isaccess_bt, pos, 1);
                    }
                });
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos, 2);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return CompleteListBean.size();
    }

    class CompleteListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.complete_iv)
        ImageView complete_iv;
        @Bind(R.id.complete_title)
        TextView complete_title;
        @Bind(R.id.complete_wUsername)
        TextView complete_wUsername;
        @Bind(R.id.complete_lUsername)
        TextView complete_lUsername;
        @Bind(R.id.complete_createtime)
        TextView complete_createtime;
        @Bind(R.id.complete_isaccess_bt)
        Button complete_isaccess_bt;

        public CompleteListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
