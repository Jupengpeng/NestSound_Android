package com.xilu.wybz.ui.bean;

import com.xilu.wybz.utils.StringStyleUtil;

/**
 * Created by Zning on 2015/9/7.
 */
public class LyricBean {

    String itemid;
    String id;
    int sampleid;
    String title;
    String lyrics;
    String author;
    String createday;
    String time;
    String pic;
    boolean isvisibleselect;//是否可以选择
    boolean ischecked;//是否选中

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
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

    public String getCreateday() {
        return createday;
    }

    public void setCreateday(String createday) {
        this.createday = createday;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isvisibleselect() {
        return isvisibleselect;
    }

    public void setIsvisibleselect(boolean isvisibleselect) {
        this.isvisibleselect = isvisibleselect;
    }

    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getSampleid() {
        return sampleid;
    }

    public void setSampleid(int sampleid) {
        this.sampleid = sampleid;
    }
}
