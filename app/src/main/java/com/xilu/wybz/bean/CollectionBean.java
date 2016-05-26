package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/29 0029.
 * 这个是消息 收藏
 */
public class CollectionBean implements Serializable {
    int userId;
    String name;
    String headurl;
    String MusicName;
    String MusicId;
    String MusicPic;
    String Isnewfov;
    String addtime;

    public String getMusicName() {
        if (TextUtils.isEmpty(MusicName)) {
            MusicName = "未命名";
        }
        return MusicName;
    }

    public void setMusicName(String musicName) {
        MusicName = musicName;
    }

    public String getMusicPic() {
        return MusicPic;
    }

    public void setMusicPic(String musicPic) {
        MusicPic = musicPic;
    }

    public String getMusicId() {
        return MusicId;
    }

    public void setMusicId(String musicId) {
        MusicId = musicId;
    }

    public String getIsnewfov() {
        return Isnewfov;
    }

    public void setIsnewfov(String isnewfov) {
        Isnewfov = isnewfov;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadurl() {
        if (!TextUtils.isEmpty(headurl)) {
            headurl = headurl.replace(YinChaoConfig.DEFAULT_HEAD_URL, "");
        }
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getUserid() {
        return userId;
    }

    public void setUserid(int userId) {
        this.userId = userId;
    }
}
