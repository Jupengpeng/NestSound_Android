package com.xilu.wybz.bean;

/**
 * Created by Administrator on 2016/9/21.
 */

public class PreservationInfo {

    public int id;
    public String worksname;
    public long createtime;
    public int statue;
    public int sort_id;


    public PreservationInfo() {
    }

    public PreservationInfo(String worksname, long createtime, int statue, int sort_id) {
        this.worksname = worksname;
        this.createtime = createtime;
        this.statue = statue;
        this.sort_id = sort_id;
    }
}
