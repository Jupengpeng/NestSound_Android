package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.MyCommon;

import java.io.Serializable;

/**
 * Created by Zning on 2015/9/7.
 */
public class FovBean implements Serializable {

    int userid;
    String name;
    String headurl;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userId) {
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
            headurl = headurl.replace(MyCommon.defult_head, "");
        }
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }
}
