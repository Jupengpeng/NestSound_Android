package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.bean.MusicTalk;

import java.util.List;

/**
 * Created by June on 16/5/7.
 */
public interface IMusicTalkDetailView extends IBaseView{
    void loadSuccess(MusicTalk musicTalk);
    void loadError();
    void zanSuccess();
    void zanFail();
    void shareSuccess();
    void shareFail();
}
