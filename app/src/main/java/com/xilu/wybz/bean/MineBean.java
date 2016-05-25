package com.xilu.wybz.bean;

import java.util.List;

/**
 * Created by Zning on 2015/9/2.
 */
public class MineBean {

    int fansnum;
    int gznum;
    UserBean user;

    List<WorksData> list;


    public int getFansnum() {
        return fansnum;
    }

    public void setFansnum(int fansnum) {
        this.fansnum = fansnum;
    }

    public int getGznum() {
        return gznum;
    }

    public void setGznum(int gznum) {
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
