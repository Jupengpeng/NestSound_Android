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
import com.xilu.wybz.bean.Invitation;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class InvitationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Invitation> invitationList;
    private Context context;
    private int type;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    protected boolean isLoadMoreFooterShown = false;

    private List<Integer> selecd = new ArrayList<>();

    public InvitationAdapter(List<Invitation> invitationList, Context context) {
        this.invitationList = invitationList;
        this.context = context;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private InvitationAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(InvitationAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        InvitationAdapter.InvitationViewHolder holder = new InvitationAdapter.InvitationViewHolder(LayoutInflater.from(context).inflate(R.layout.invitation_item, parent, false));
//
//        return holder;
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.invitation_item, null);
            return new InvitationViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_pull_to_refresh_footer, parent, false);
            return new FooterViewHolder(view);
        }
        return null;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (holder instanceof InvitationViewHolder) {
            Invitation invitation = invitationList.get(position);
            ((InvitationViewHolder) holder).itemView.setTag(position);

            if (invitation != null) {
                ZnImageLoader.getInstance().displayImage(invitation.getHeadurl(), ZnImageLoader.getInstance().headOptions, ((InvitationViewHolder) holder).invitation_head_iv);
                ((InvitationViewHolder) holder).invitation_tv_name.setText(invitation.getNickname());
                ((InvitationViewHolder) holder).invitation_tv_signature.setText(invitation.getSignature());
                if (invitation.getInvite() == 1) {
                    ((InvitationViewHolder) holder).invitation_bt.setBackgroundResource(R.drawable.finishbt_bg);
                    ((InvitationViewHolder) holder).invitation_bt.setText("已邀请");
                }
            }
            if (invitation.getRecommend() == 0) {
                ((InvitationViewHolder) holder).invitation_recommend.setVisibility(View.GONE);
            }

            if (invitation.getInvite() == 0) {
                ((InvitationViewHolder) holder).invitation_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(((InvitationViewHolder) holder).invitation_bt, position);
                        ((InvitationViewHolder) holder).invitation_bt.setBackgroundResource(R.drawable.finishbt_bg);
                        ((InvitationViewHolder) holder).invitation_bt.setText("已邀请");
                        invitation.setInvite(1);
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return invitationList.size() + (isLoadMoreFooterShown ? 1 : 0);
    }

    class InvitationViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.invitation_head_iv)
        CircleImageView invitation_head_iv;
        @Bind(R.id.invitation_tv_name)
        TextView invitation_tv_name;
        @Bind(R.id.invitation_tv_signature)
        TextView invitation_tv_signature;
        @Bind(R.id.invitation_bt)
        Button invitation_bt;
        @Bind(R.id.finishinvitation_bt)
        Button finishinvitation_bt;
        @Bind(R.id.invitation_recommend)
        ImageView invitation_recommend;

        public InvitationViewHolder(View itemView) {
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
