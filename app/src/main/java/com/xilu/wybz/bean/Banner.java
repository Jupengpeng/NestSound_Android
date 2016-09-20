package com.xilu.wybz.bean;

/**
 * Created by June on 16/5/3.
 * 首页顶部banner
 */
public class Banner {
    public int type;//0 音乐 1网页
    public String itemid;
    public String pic;
    public String name;
    public String author;
    public int createday;
    public String looknum;
    public String fovnum;
    public String upnum;
    public String playurl;
    public String times;

    public String getUpnum() {
        return upnum;
    }

    public void setUpnum(String upnum) {
        this.upnum = upnum;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getPic() {
        return pic;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCreateday() {
        return createday;
    }

    public void setCreateday(int createday) {
        this.createday = createday;
    }

    public String getLooknum() {
        return looknum;
    }

    public void setLooknum(String looknum) {
        this.looknum = looknum;
    }

    public String getFovnum() {
        return fovnum;
    }

    public void setFovnum(String fovnum) {
        this.fovnum = fovnum;
    }

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
