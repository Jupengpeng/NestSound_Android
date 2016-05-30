package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.MsgCommentBean;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ICommentView extends IBaseView {

    void showCommentData(List<CommentBean> commentBeans);

    void showMsgCommentData(List<MsgCommentBean> commentBeans);

    void loadFail();

    void loadNoMore();

    void loadNoData();

    void commentSuccess();

    void commentFail();

    void delSuccess(int pos);

    void delFail();
}
