package com.xilu.wybz.bean;

/**
 * Created by Administrator on 2016/1/6.
 * 用户反馈
 */
public class FendBean {
    int userId;
    String content;
    String phone;

    public int getUserid() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
