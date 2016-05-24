package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.InforCommentBean;
import com.xilu.wybz.bean.ZambiaBean;

import java.util.List;

/**
 * Created by June on 16/4/21.
 */
public interface ICommentView extends IBaseView {

    void showCommentData(List<InforCommentBean> commentBeans);

    void loadFail();

    void loadNoMore();

    void loadNoData();

    void commentSuccess();

    void commentFail();
}
