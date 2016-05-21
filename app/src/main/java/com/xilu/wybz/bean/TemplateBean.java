package com.xilu.wybz.bean;

import com.xilu.wybz.utils.StringStyleUtil;

/**
 * Created by Zning on 2015/9/7.
 * 热门录制曲目 其中的list
 */
public class TemplateBean {

    String itemid;//模板的唯一标识ID
    String pic;//模板的封面图片
    String name;//模板的名字
    int type;//类型：0：普通，1：热门，2：最新
    String playurl;//模板的在线播放地址
    String times;//歌曲的总长度，单位为秒
    int usenum;//使用次数
    String author;//作者

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

    public int getUsernum() {
        return usenum;
    }

    public void setUsernum(int count) {
        this.usenum = count;
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
}
