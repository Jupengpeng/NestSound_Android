package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.ui.IView.*;

/**
 * Created by June on 16/4/5.
 */
public interface IPlayView extends com.xilu.wybz.ui.IView.IBaseView {
    void toUserInfo();//点击头像 我个人主页

    void toHotActivity();

    void toCommentActivity();

    void collectionMusic();

    void collectionMusicSuccess(String result);

    void collectionMusicFail(String msg);

    void zambiaMusic();

    void zambiaMusicSuccess(String result);

    void zambiaMusicFail();

}
