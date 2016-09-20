package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.ShareResponseBean;

/**
 * Created by Administrator on 2016/6/5.
 */
public interface ISaveSongView extends IBaseView{
    void saveWordSuccess(ShareResponseBean response);
    void saveWordFail();
}
