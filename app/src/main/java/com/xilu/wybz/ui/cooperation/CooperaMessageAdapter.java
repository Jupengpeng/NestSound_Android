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
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.view.CircleImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CooperaMessageAdapter extends RecyclerView.Adapter<CooperaMessageAdapter.CooperaMessageViewHolder> {
    private List<CooperaMessageBean> cooperaLyricBeenList;
    private Context context;


    public CooperaMessageAdapter(List<CooperaMessageBean> cooperaLyricBeenList, Context context) {
        this.cooperaLyricBeenList = cooperaLyricBeenList;
        this.context = context;
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
    public CooperaMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CooperaMessageAdapter.CooperaMessageViewHolder holder = new CooperaMessageAdapter.CooperaMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.cooperamessage_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CooperaMessageViewHolder holder, int position) {
        CooperaMessageBean cooperaMessageBean = cooperaLyricBeenList.get(position);
        if (cooperaMessageBean != null) {
            holder.nickname_tv.setText(cooperaMessageBean.getNickname());
            holder.createdate_tv.setText(DateFormatUtils.formatX1(cooperaMessageBean.getCreatedate()));

//                SpannableString s = StringStyleUtil.getParentCommentStyleStr(bean);

            if (cooperaMessageBean.getComment_type() == 2) {
                SpannableString s = StringStyleUtil.getWorkMessageStyleStr(context, cooperaMessageBean);
                holder.comment_tv.setText(s);
            } else {
                holder.comment_tv.setText(cooperaMessageBean.getComment());
            }

        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.layout, position, 1, cooperaMessageBean);
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mOnItemLongClickListener.onItemLongClick(holder.layout, position, 1, cooperaMessageBean);
                return false;
            }
        });
        holder.head_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.head_iv, position, 2, cooperaMessageBean);
            }
        });


    }
    public void removeitem(int pos){
        cooperaLyricBeenList.remove(pos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cooperaLyricBeenList.size();
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
}
