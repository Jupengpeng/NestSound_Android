package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/28 0028.
 * 赞
 */
public class ZambiaBean implements Serializable {

    int userid;
    String name;
    String headurl;
    String MusicName;
    String MusicId;
    String MusicPic;
    String Isnewfov;
    String addtime;

    public String getMusicName() {
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
        return userid;
    }

    public void setUserid(int userId) {
        this.userid = userid;
    }
}
