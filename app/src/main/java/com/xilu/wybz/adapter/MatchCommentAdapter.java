package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MatchCommentAdapter extends RecyclerView.Adapter<MatchCommentAdapter.MatchCommentViewHolder> {
    private List<CommentBean> mList;
    private Context context;
    private int itemWidth;
    public MatchCommentAdapter(Context context, List<CommentBean> worksDataList, int column) {
        this.context = context;
        this.mList = worksDataList;
//        itemWidth = (DensityUtil.getScreenW(context)-DensityUtil.dip2px(context,(column+1)*10))/column;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public MatchCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MatchCommentViewHolder holder = new MatchCommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_match_comment, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MatchCommentViewHolder holder, final int position) {
        CommentBean commentBean = mList.get(position);
        holder.tvContent.setText(commentBean.comment);
        holder.tvNickName.setText(commentBean.getNickname());
        holder.tvNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrefsUtil.getUserId(context)!=commentBean.uid) {
                    UserInfoActivity.toUserInfoActivity(context, commentBean.uid, commentBean.nickname);
                }
            }
        });
        if(StringUtils.isNotBlank(commentBean.target_nickname)){
            holder.tvTargetNickNmae.setText(commentBean.getTarget_nickname());
            holder.tvReply.setVisibility(View.VISIBLE);
        }else{
            holder.tvTargetNickNmae.setText("");
            holder.tvReply.setVisibility(View.GONE);
        }
        holder.tvTargetNickNmae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PrefsUtil.getUserId(context)!=commentBean.target_uid) {
                    UserInfoActivity.toUserInfoActivity(context, commentBean.target_uid, commentBean.target_nickname);
                }
            }
        });
        holder.tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MatchCommentViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_nickname)
        TextView tvNickName;
        @Bind(R.id.tv_target_nickname)
        TextView tvTargetNickNmae;
        @Bind(R.id.tv_reply)
        TextView tvReply;
        public MatchCommentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
