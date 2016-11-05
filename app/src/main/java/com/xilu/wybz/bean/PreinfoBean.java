package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/4.
 */

public class PreinfoBean implements Serializable{

    long itemid;
    String title;
    String lyrics;
    int wUid;
    String wUsername;
    int lUid;
    String lUsername;

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public int getwUid() {
        return wUid;
    }

    public void setwUid(int wUid) {
        this.wUid = wUid;
    }

    public String getwUsername() {
        return wUsername;
    }

    public void setwUsername(String wUsername) {
        this.wUsername = wUsername;
    }

    public int getlUid() {
        return lUid;
    }

    public void setlUid(int lUid) {
        this.lUid = lUid;
    }

    public String getlUsername() {
        return lUsername;
    }

    public void setlUsername(String lUsername) {
        this.lUsername = lUsername;
    }
}
