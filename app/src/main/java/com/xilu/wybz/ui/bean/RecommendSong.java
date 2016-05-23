package com.xilu.wybz.ui.bean;

import com.xilu.wybz.common.MyHttpClient;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/10.
 */
public class RecommendSong implements Serializable{
    public String itemid;
    public String pic;
    public String name;

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
