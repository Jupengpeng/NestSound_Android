package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaMessageBean;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.view.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CooperaMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CooperaMessageBean> cooperaLyricBeenList;
    private Context context;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected boolean isLoadMoreFooterShown = false;

    public CooperaMessageAdapter(List<CooperaMessageBean> cooperaLyricBeenList, Context context) {
        this.cooperaLyricBeenList = cooperaLyricBeenList;
        this.context = context;
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
    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type, CooperaMessageBean cooperaMessageBean);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position, int type, CooperaMessageBean cooperaMessageBean);
    }

    private CooperaMessageAdapter.OnItemClickListener mOnItemClickListener;
    private CooperaMessageAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(CooperaMessageAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(CooperaMessageAdapter.OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        CooperaMessageAdapter.CooperaMessageViewHolder holder = new CooperaMessageAdapter.CooperaMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.cooperamessage_item, parent, false));
//        return holder;
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cooperamessage_item, null);
            return new CooperaMessageViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof CooperaMessageViewHolder) {
            CooperaMessageBean cooperaMessageBean = cooperaLyricBeenList.get(position);
            if (cooperaMessageBean != null) {
                ((CooperaMessageViewHolder) holder).nickname_tv.setText(cooperaMessageBean.getNickname());
                ((CooperaMessageViewHolder) holder) .createdate_tv.setText(DateFormatUtils.formatX1(cooperaMessageBean.getCreatedate()));
                ZnImageLoader.getInstance().displayImage(cooperaMessageBean.getHeaderurl(),ZnImageLoader.getInstance().headOptions,((CooperaMessageViewHolder) holder).head_iv);
//                SpannableString s = StringStyleUtil.getParentCommentStyleStr(bean);

                if (cooperaMessageBean.getComment_type() == 2) {
                    SpannableString s = StringStyleUtil.getWorkMessageStyleStr(context, cooperaMessageBean);
                    ((CooperaMessageViewHolder) holder).comment_tv.setText(s);
                } else {
                    ((CooperaMessageViewHolder) holder).comment_tv.setText(cooperaMessageBean.getComment());
                }

            }
            ((CooperaMessageViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(((CooperaMessageViewHolder) holder).layout, position, 1, cooperaMessageBean);
                }
            });
            ((CooperaMessageViewHolder) holder).layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onItemLongClick(((CooperaMessageViewHolder) holder).layout, position, 1, cooperaMessageBean);
                    return false;
                }
            });
            ((CooperaMessageViewHolder) holder).head_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(((CooperaMessageViewHolder) holder).head_iv, position, 2, cooperaMessageBean);
                }
            });

        }
    }
    public void removeitem(int pos){
        cooperaLyricBeenList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cooperaLyricBeenList.size()+ (isLoadMoreFooterShown ? 1 : 0);
    }

    class CooperaMessageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.head_iv)
        CircleImageView head_iv;
        @Bind(R.id.nickname_tv)
        TextView nickname_tv;
        @Bind(R.id.comment_tv)
        TextView comment_tv;
        @Bind(R.id.createdate_tv)
        TextView createdate_tv;
        @Bind(R.id.layout)
        RelativeLayout layout;

        public CooperaMessageViewHolder(View itemView) {
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
