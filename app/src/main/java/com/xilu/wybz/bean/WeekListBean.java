package com.xilu.wybz.bean;

import com.xilu.wybz.utils.StringStyleUtil;

/**
 * Created by Administrator on 2016/3/10.
 */
public class WeekListBean {
    String itemid;
    String pic;
    String name;
    String author;
    String looknum;
    String hotname;//伴奏
    int createday;

    public int getCreateday() {
        return createday;
    }

    public void setCreateday(int createday) {
        this.createday = createday;
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

    public String getLooknum() {
        return looknum;
    }

    public void setLooknum(String looknum) {
        this.looknum = looknum;
    }

    public String getHotname() {
        return hotname;
    }

    public void setHotname(String hotname) {
        this.hotname = hotname;
    }
}
