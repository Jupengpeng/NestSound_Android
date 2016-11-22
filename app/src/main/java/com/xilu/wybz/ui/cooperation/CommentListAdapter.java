package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaDetailsBean;
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

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentListViewHolder> {
    private List<CooperaDetailsBean.CommentListBean> commentListBean;
    private Context context;


    public CommentListAdapter(List<CooperaDetailsBean.CommentListBean> commentListBean, Context context) {
        this.commentListBean = commentListBean;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private CommentListAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CommentListAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CommentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentListAdapter.CommentListViewHolder holder = new CommentListAdapter.CommentListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CommentListViewHolder holder, int position) {
        CooperaDetailsBean.CommentListBean commentBean = commentListBean.get(position);
        Log.e("BBB", commentBean.getComment());
        holder.tv_name.setText(commentBean.getNickname());
        if (commentBean.getComment_type() == 2) {

            SpannableString s = StringStyleUtil.getWorkMessageStyleStr(context, commentBean);
            holder.tv_content.setText(s);
        }else{
            holder.tv_content.setText(commentBean.getComment());
        }

        holder.tv_date.setText(DateFormatUtils.formatX1(commentBean.getCreatedate()));
        ZnImageLoader.getInstance().displayImage(commentBean.getHeaderurl(), ZnImageLoader.getInstance().headOptions, holder.iv_head);
        holder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.iv_head, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentListBean.size();
    }

    class CommentListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.tv_date)
        TextView tv_date;
        @Bind(R.id.tv_content)
        TextView tv_content;
        @Bind(R.id.iv_head)
        CircleImageView iv_head;

        public CommentListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
