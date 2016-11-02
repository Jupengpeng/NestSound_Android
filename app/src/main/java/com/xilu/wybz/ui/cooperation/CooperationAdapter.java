package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperationBean;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.view.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CooperationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CooperationBean> cooperationBeanList;
    private Context context;
    private CooperaCommentAdapter adapter;
    private List<CooperationBean.CommentListBean> commentBeanList = new ArrayList<>();
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected boolean isLoadMoreFooterShown = false;
    public CooperationAdapter(List<CooperationBean> cooperationBeanList, Context context) {

        this.cooperationBeanList = cooperationBeanList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    private CooperationAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CooperationAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        CooperationAdapter.CooperationViewHolder holder = new CooperationAdapter.CooperationViewHolder(LayoutInflater.from(context).inflate(R.layout.cooperation_item, parent, false));
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cooperation_item, null);
            return new CooperationViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CooperationViewHolder) {
            CooperationBean cooperationBean = cooperationBeanList.get(position);
            if (cooperationBean != null) {
                ((CooperationViewHolder) holder).coopera_tv_name.setText(cooperationBean.getUserInfo().getNickname());
                ((CooperationViewHolder) holder).coopera_tv_time.setText(DateFormatUtils.formatX1(cooperationBean.getDemandInfo().getCreatetime()));
                ((CooperationViewHolder) holder).coopera_tv_lyricsname.setText(cooperationBean.getDemandInfo().getTitle());
                ((CooperationViewHolder) holder).coopera_tv_lyricscontent.setText(cooperationBean.getDemandInfo().getLyrics());
                ((CooperationViewHolder) holder).coopera_tv_require.setText(cooperationBean.getDemandInfo().getRequirement());
                ((CooperationViewHolder) holder).coopera_commentnum_tv.setText("更多" + cooperationBean.getDemandInfo().getCommentnum() + "条留言");
                ((CooperationViewHolder) holder).coopera_worknum_tv.setText("已有" + cooperationBean.getDemandInfo().getWorknum() + "位巢人参与合作");
                ((CooperationViewHolder) holder).coopera_tv_lyricscontent.setText(cooperationBean.getDemandInfo().getLyrics());
                ((CooperationViewHolder) holder).coopera_comment_rectclerview.setNestedScrollingEnabled(false);
                ((CooperationViewHolder) holder).coopera_comment_rectclerview.setLayoutManager(new LinearLayoutManager(context));
                commentBeanList = cooperationBean.getCommentList();
                adapter = new CooperaCommentAdapter(commentBeanList, context);
                ((CooperationViewHolder) holder).coopera_comment_rectclerview.setAdapter(adapter);

            }
            ((CooperationViewHolder) holder).ll_jump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(  ((CooperationViewHolder) holder).ll_jump, pos, 1);
                }
            });
            ((CooperationViewHolder) holder).ll_jump2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(  ((CooperationViewHolder) holder).ll_jump2, pos, 2);
                }
            });
            ((CooperationViewHolder) holder).coopera_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(  ((CooperationViewHolder) holder).ll_jump2, pos, 3);
                }
            });
        }
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
    public int getItemCount() {
        return cooperationBeanList.size()+ (isLoadMoreFooterShown ? 1 : 0);
    }

    class CooperationViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.coopera_head_iv)
        ImageView coopera_head_iv;
        @Bind(R.id.coopera_tv_name)
        TextView coopera_tv_name;
        @Bind(R.id.coopera_tv_time)
        TextView coopera_tv_time;
        @Bind(R.id.coopera_bt)
        Button coopera_bt;
        @Bind(R.id.coopera_tv_lyricsname)
        TextView coopera_tv_lyricsname;
        @Bind(R.id.coopera_tv_lyricscontent)
        TextView coopera_tv_lyricscontent;
        @Bind(R.id.coopera_tv_require)
        TextView coopera_tv_require;
        @Bind(R.id.ll_jump)
        LinearLayout ll_jump;
        @Bind(R.id.ll_jump2)
        LinearLayout ll_jump2;
        @Bind(R.id.coopera_comment_rectclerview)
        MyRecyclerView coopera_comment_rectclerview;
        @Bind(R.id.coopera_commentnum_tv)
        TextView coopera_commentnum_tv;
        @Bind(R.id.coopera_worknum_tv)
        TextView coopera_worknum_tv;

        public CooperationViewHolder(View itemView) {
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
