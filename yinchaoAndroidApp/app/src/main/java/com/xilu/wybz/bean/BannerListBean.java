package com.xilu.wybz.bean;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.utils.StringStyleUtil;

/**
 * Created by Administrator on 2016/3/11.
 */
public class BannerListBean {
    String itemid;
    String pic;
    String name;
    String author;
    String times;
    String playurl;
    String upnum;
    String fovnum;
    String looknum;
    int createday;
    int type;

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getUpnum() {
        return upnum;
    }

    public void setUpnum(String upnum) {
        this.upnum = upnum;
    }

    public String getFovnum() {
        return fovnum;
    }

    public void setFovnum(String fovnum) {
        this.fovnum = fovnum;
    }

    public String getLooknum() {
        return looknum;
    }

    public void setLooknum(String looknum) {
        this.looknum = looknum;
    }

    public int getCreateday() {
        return createday;
    }

    public void setCreateday(int createday) {
        this.createday = createday;
    }

    public String getPic() {
        return pic.startsWith("http") ? pic : MyHttpClient.ROOT_URL + pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        if(author==null){
            author="";
        }else{
            author= StringStyleUtil.removeSpecialCharacters(author);
        }
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
