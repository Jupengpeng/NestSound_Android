package com.xilu.wybz.common.interfaces;

import com.xilu.wybz.bean.UserMusicBean;

/**
 * Created by Zning on 2015/10/26.
 */
public interface IMusicListener {
    void onDel(UserMusicBean bean);

    void toLocalPlay(String id, String from);

    void toNetPlay(String id, String from);

    void toPlay();
}
