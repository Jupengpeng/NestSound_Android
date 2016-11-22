package com.xilu.wybz.bean;

/**
 * Created by Administrator on 2016/10/24.
 */

public class MineBean {
    int id ;
    String title;
    long createtime;
    int worknum;
    int status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getWorknum() {
        return worknum;
    }

    public void setWorknum(int worknum) {
        this.worknum = worknum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
