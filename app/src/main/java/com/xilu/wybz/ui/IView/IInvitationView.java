package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.Invitation;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public interface IInvitationView extends IBaseView {

    void showInvitationList(List<Invitation> invitationList) ;
//    void showSerachInvitationList(List<Invitation> invitationList) ;
//    void sendSuccess(int pos);
    void sendSuccess();
    void serachSuccess();
    void noData();
}
