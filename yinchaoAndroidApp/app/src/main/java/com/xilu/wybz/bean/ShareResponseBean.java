package com.xilu.wybz.bean;

/**
 * Created by Administrator on 2016/6/6.
 */
public class ShareResponseBean {

    public String itemid;
    public String shareurl;

    public String getCompleteShareUrl(){
        return shareurl+"?id="+itemid;
    }

}
