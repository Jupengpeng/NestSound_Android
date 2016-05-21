package com.xilu.wybz.bean;

import com.xilu.wybz.common.MyHttpClient;

/**
 * Created by Administrator on 2016/3/10.
 */
public class YueShuoBean {
    String itemid;
    String pic;
    String name;
    String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getPic() {
        if (pic == null) {
            pic = "";
        }
        return pic.startsWith("http") ? pic : MyHttpClient.ROOT_URL + pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
