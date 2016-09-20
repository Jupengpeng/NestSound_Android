package com.xilu.wybz.bean;

/**
 * Created by Zning on 2015/9/2.
 */
public class UserMusicBean {

    String type;
    CzMusicBean czMusicBean;
    MusicBean musicBean;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CzMusicBean getCzMusicBean() {
        return czMusicBean;
    }

    public void setCzMusicBean(CzMusicBean czMusicBean) {
        this.czMusicBean = czMusicBean;
    }

    public MusicBean getMusicBean() {
        return musicBean;
    }

    public void setMusicBean(MusicBean musicBean) {
        this.musicBean = musicBean;
    }
}
