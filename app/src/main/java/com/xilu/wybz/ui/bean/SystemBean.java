package com.xilu.wybz.ui.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/2 0002.
 * 这是系统消息
 */
public class SystemBean implements Serializable {
    String sysheadurl;
    String sysname;
    String sendtime;
    String picurl;
    String detailurl;
    String content;
    String noteid;

    public String getSysheadurl() {
        return sysheadurl;
    }

    public void setSysheadurl(String sysheadurl) {
        this.sysheadurl = sysheadurl;
    }

    public String getSysname() {
        return sysname;
    }

    public void setSysname(String sysname) {
        this.sysname = sysname;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public String getPicurl() {
        return picurl;
    }

    public void setPicurl(String picurl) {
        this.picurl = picurl;
    }

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNoteid() {
        return noteid;
    }

    public void setNoteid(String noteid) {
        this.noteid = noteid;
    }
}
