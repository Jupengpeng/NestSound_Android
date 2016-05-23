package com.xilu.wybz.ui.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;

import java.io.Serializable;

/**
 * Created by Zning on 2015/9/7.
 */
public class FovBean implements Serializable {

    String userid;
    String name;
    String headurl;

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
}
