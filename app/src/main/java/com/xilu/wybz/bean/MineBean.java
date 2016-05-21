package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;

import java.util.List;

/**
 * Created by Zning on 2015/9/2.
 */
public class MineBean {

    String userid;
    String name;
    String headurl;
    int sex;//0男 1女
    String birthday;
    String info;
    List<MusicBean> workList;
    List<MusicBean> fovList;
    List<LyricBean> lyricList;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<MusicBean> getWorkList() {
        return workList;
    }

    public void setWorkList(List<MusicBean> workList) {
        this.workList = workList;
    }

    public List<MusicBean> getFovList() {
        return fovList;
    }

    public void setFovList(List<MusicBean> fovList) {
        this.fovList = fovList;
    }

    public List<LyricBean> getLyricList() {
        return lyricList;
    }

    public void setLyricList(List<LyricBean> lyricList) {
        this.lyricList = lyricList;
    }
}
