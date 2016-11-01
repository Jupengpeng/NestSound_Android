package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperationBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CooperaCommentAdapter extends RecyclerView.Adapter<CooperaCommentAdapter.CooperaCommentViewHolder> {
    private List<CooperationBean.CommentListBean> commentBeanList;
    private Context context;


    public CooperaCommentAdapter(List<CooperationBean.CommentListBean> commentBeanList, Context context) {
        this.commentBeanList = commentBeanList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private CooperaCommentAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CooperaCommentAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CooperaCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CooperaCommentAdapter.CooperaCommentViewHolder holder = new CooperaCommentAdapter.CooperaCommentViewHolder(LayoutInflater.from(context).inflate(R.layout.cooperacomment_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CooperaCommentViewHolder holder, int position) {
        CooperationBean.CommentListBean commentBean = commentBeanList.get(position);
        if (commentBean != null) {
            holder.content.setText(commentBean.getNickname()+":" + commentBean.getComment());
        }

    }

    @Override
    public int getItemCount() {
        return commentBeanList.size();
    }

    class CooperaCommentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.content)
        TextView content;

        public CooperaCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
