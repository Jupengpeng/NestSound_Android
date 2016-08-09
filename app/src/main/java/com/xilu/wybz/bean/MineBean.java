package com.xilu.wybz.bean;

import java.util.List;

/**
 * Created by June on 2015/9/2.
 */
public class MineBean {

    public int fansnum;
    public int gznum;
    public int lyricsnum;
    public int worknum;
    public int inspirenum;
    public int fovnum;
    public int isFocus;////是否已关注  0=未关注，1=已关注过，2=已互相关注过
    public UserBean user;
    public List<WorksData> list;

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
