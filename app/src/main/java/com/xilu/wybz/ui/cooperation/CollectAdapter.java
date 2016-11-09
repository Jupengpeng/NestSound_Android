package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CollectBean;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.utils.DateFormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CollectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CollectBean> collectBeanList;
    private Context context;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected boolean isLoadMoreFooterShown = false;

    public CollectAdapter(List<CollectBean> collectBeanList, Context context) {
        this.collectBeanList = collectBeanList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    private CollectAdapter.OnItemClickListener mOnItemClickListener;
    private CollectAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(CollectAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(CollectAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CollectAdapter.CollectViewHolder holder = new CollectAdapter.CollectViewHolder(LayoutInflater.from(context).inflate(R.layout.collect_item, parent, false));
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.collect_item, null);
            return new CollectViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }

    public void removeItem(int pos) {
        collectBeanList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CollectViewHolder) {
            CollectBean collectBean = collectBeanList.get(position);

            if (collectBean != null) {
                switch (collectBean.getStatus()) {
                    case 1://正在进行
                        ((CollectViewHolder) holder).collect_tv_status.setText("正在进行");
                        break;
                    case 3://对面停止合作
                        ((CollectViewHolder) holder).collect_tv_status.setText("对方停止该合作");
                        break;
                    case 4://已到期
                        ((CollectViewHolder) holder).collect_tv_status.setText("已到期");
                        ((CollectViewHolder) holder).collect_tv_status.setTextColor(Color.parseColor("#ff6161"));
                        break;
                    case 8://合作成功
                        ((CollectViewHolder) holder).collect_tv_status.setText("合作成功");
                        break;
                }
                ((CollectViewHolder) holder).collect_tv_name.setText(collectBean.getNickname());
                ((CollectViewHolder) holder).collect_tv_title.setText(collectBean.getTitle());
                ((CollectViewHolder) holder).collect_tv_participatenum.setText(collectBean.getWorknum() + "人参与合作");
                ((CollectViewHolder) holder).collect_tv_time.setText(DateFormatUtils.formatX1(collectBean.getCreatetime()));
                ZnImageLoader.getInstance().displayImage(collectBean.getHeadurl(), ZnImageLoader.getInstance().headOptions, ((CollectViewHolder) holder).collect_headerurl_iv);
            }
            ((CollectViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(((CollectViewHolder) holder).itemView, position);
                }
            });
            ((CollectViewHolder) holder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mOnItemLongClickListener.onItemLongClick(((CollectViewHolder) holder).itemView, position);

                    return false;
                }
            });


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
        return collectBeanList.size() + (isLoadMoreFooterShown ? 1 : 0);
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.collect_headerurl_iv)
        ImageView collect_headerurl_iv;
        @Bind(R.id.collect_tv_status)
        TextView collect_tv_status;
        @Bind(R.id.collect_tv_name)
        TextView collect_tv_name;
        @Bind(R.id.collect_tv_participatenum)
        TextView collect_tv_participatenum;
        @Bind(R.id.collect_tv_time)
        TextView collect_tv_time;
        @Bind(R.id.collect_tv_title)
        TextView collect_tv_title;

        public CollectViewHolder(View itemView) {
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
