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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.InvitationViewHolder> {
    private List<Invitation> invitationList;
    private Context context;


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
    public InvitationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InvitationAdapter.InvitationViewHolder holder = new InvitationAdapter.InvitationViewHolder(LayoutInflater.from(context).inflate(R.layout.invitation_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(InvitationViewHolder holder, int position) {
        Invitation invitation = invitationList.get(position);
        ZnImageLoader.getInstance().displayImage(invitation.getHeadurl(), ZnImageLoader.getInstance().headOptions, holder.invitation_head_iv);

        if (invitation != null) {
            holder.invitation_tv_name.setText(invitation.getNickname());
            holder.invitation_tv_signature.setText(invitation.getSignature());
            if (invitation.getInvite() == 1) {
                holder.invitation_bt.setVisibility(View.GONE);
                holder.finishinvitation_bt.setVisibility(View.VISIBLE);
            }
        }
        if (position > 2) {
            holder.invitation_recommend.setVisibility(View.GONE);
        }
        holder.invitation_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.invitation_bt, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return invitationList.size();
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
}
