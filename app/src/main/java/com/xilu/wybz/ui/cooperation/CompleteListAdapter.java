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

public class CompleteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CooperaDetailsBean.CompleteListBean> CompleteListBean;
    private Context context;
    private int flag;
    private boolean b = false;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected boolean isLoadMoreFooterShown = false;

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
//        CompleteListAdapter.CompleteListViewHolder holder = new CompleteListAdapter.CompleteListViewHolder(LayoutInflater.from(context).inflate(R.layout.completework_item, parent, false));
//        return holder;
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.completework_item, null);
            return new CompleteListViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CompleteListViewHolder) {
            CooperaDetailsBean.CompleteListBean completeBean = CompleteListBean.get(position);
            if (completeBean != null) {
                ((CompleteListViewHolder)holder).complete_title.setText("歌曲名: " + completeBean.getTitle());
                ((CompleteListViewHolder)holder).complete_lUsername.setText("作词    : " + completeBean.getLUsername());
                ((CompleteListViewHolder)holder).complete_wUsername.setText("作曲    : " + completeBean.getWUsername());
                ((CompleteListViewHolder)holder).complete_createtime.setText(DateFormatUtils.formatX1(completeBean.getCreatetime()));
                ZnImageLoader.getInstance().displayImage(completeBean.getPic(), ZnImageLoader.getInstance().headOptions, ((CompleteListViewHolder)holder).complete_iv);
                if (flag == 1) {
                    ((CompleteListViewHolder)holder).complete_isaccess_bt.setVisibility(View.GONE);
                }
                if (completeBean.getAccess() == 1) {
                    b = true;
                    ((CompleteListViewHolder)holder).complete_isaccess_bt.setBackgroundResource(R.drawable.finishbt_bg);
                    ((CompleteListViewHolder)holder).complete_isaccess_bt.setText("已采纳");
                }
                if (completeBean.getAccess() != 1 && b == false) {
                    ((CompleteListViewHolder)holder).complete_isaccess_bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(((CompleteListViewHolder)holder).complete_isaccess_bt, pos, 1);
                        }
                    });
                } else {

                }
                ((CompleteListViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = ((CompleteListViewHolder)holder).getLayoutPosition();
                        mOnItemClickListener.onItemClick(((CompleteListViewHolder)holder).itemView, pos, 2);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return CompleteListBean.size() + (isLoadMoreFooterShown ? 1 : 0);
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

    class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }
}
