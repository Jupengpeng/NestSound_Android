package com.xilu.wybz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.JoinUserBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.market.JoinUserListActivity;
import com.xilu.wybz.ui.market.StarListActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.StringUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class JoinUserAdapter extends RecyclerView.Adapter<JoinUserAdapter.JoinUserViewHolder> {
    private List<JoinUserBean> mList;
    private Context context;
    private int itemWidth;
    private String aid;
    public JoinUserAdapter(Context context, List<JoinUserBean> worksDataList, int column, String aid) {
        this.aid = aid;
        this.context = context;
        this.mList = worksDataList;
        itemWidth = (DensityUtil.getScreenW(context)-DensityUtil.dip2px(context,(column+1)*10))/column;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public JoinUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        JoinUserViewHolder holder = new JoinUserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_joinuser, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final JoinUserViewHolder holder, final int position) {
        JoinUserBean userBean = mList.get(position);
        if(userBean.isMore){
            holder.rlMore.setVisibility(View.VISIBLE);
        }else{
            holder.rlMore.setVisibility(View.GONE);
            if(StringUtils.isNotBlank(userBean.headurl))
                ImageLoadUtil.loadImage(MyCommon.getImageUrl(userBean.headurl,itemWidth,itemWidth), holder.ivHead);
            else
                ImageLoadUtil.loadImage("res:///"+R.drawable.ic_default_head_252, holder.ivHead);
        }

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
        holder.rlMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinUserListActivity.toJoinUserListActivity(context,aid);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class JoinUserViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_head)
        SimpleDraweeView ivHead;
        @Bind(R.id.rl_more)
        RelativeLayout rlMore;
        public JoinUserViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ivHead.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemWidth));
            rlMore.setLayoutParams(new FrameLayout.LayoutParams(itemWidth, itemWidth));
        }
    }
}
