package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;

import java.util.List;

/**
 * Created by Zning on 2015/9/2.
 */
public class MineBean {

    String fannum;
    String gznum;
    UserBean user;

    List<WorksData> list;


    public String getFannum() {
        return fannum;
    }

    public void setFannum(String fannum) {
        this.fannum = fannum;
    }

    public String getGznum() {
        return gznum;
    }

    public void setGznum(String gznum) {
        this.gznum = gznum;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<WorksData> getList() {
        return list;
    }

    public void setList(List<WorksData> list) {
        this.list = list;
    }
}
