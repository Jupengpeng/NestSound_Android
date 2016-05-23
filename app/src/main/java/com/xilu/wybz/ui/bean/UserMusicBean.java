package com.xilu.wybz.ui.bean;

import com.xilu.wybz.bean.*;
import com.xilu.wybz.bean.MusicBean;

/**
 * Created by Zning on 2015/9/2.
 */
public class UserMusicBean {

    String type;
    com.xilu.wybz.bean.CzMusicBean czMusicBean;
    com.xilu.wybz.bean.MusicBean musicBean;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public com.xilu.wybz.bean.CzMusicBean getCzMusicBean() {
        return czMusicBean;
    }

    public void setCzMusicBean(com.xilu.wybz.bean.CzMusicBean czMusicBean) {
        this.czMusicBean = czMusicBean;
    }

    public com.xilu.wybz.bean.MusicBean getMusicBean() {
        return musicBean;
    }

    public void setMusicBean(MusicBean musicBean) {
        this.musicBean = musicBean;
    }
}
