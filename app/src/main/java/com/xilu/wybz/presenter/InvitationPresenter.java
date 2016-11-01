package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.Invitation;
import com.xilu.wybz.ui.IView.IInvitationView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public class InvitationPresenter extends BasePresenter<IInvitationView> {


    public InvitationPresenter(Context context, IInvitationView iView) {
        super(context, iView);
    }
    public void getInvitationList(){

        List<Invitation> invitationList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Invitation invitation = new Invitation();
            invitation.setNickname("QWEWE"+i);
            invitation.setSignature("我的大刀早已饥渴难耐"+i);
            invitation.setInvite(i);
            invitationList.add(invitation);
        }
        iView.showInvitationList(invitationList);

    }
}
