package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;
import com.xilu.wybz.utils.StringStyleUtil;

import java.io.Serializable;

/**
 * Created by Zning on 2015/9/2.
 * 播放详情页数据
 */
public class MusicDetailBean implements Serializable {

    String itemid;//作品的唯一标识ID
    String pic;
    String name;
    String author;
    String looknum;
    int fovnum;//收藏数量
    int upnum;//点赞数量
    String lyrics;
    String isfinished;
    String playurl;
    String times;
    String from;
    String prev;
    String next;
    boolean isfov;//收藏判断
    String is_zan;//点赞判断
    String authorid;
    String hotid;
    String headurl;
    String createday;
    CommentListBean commentlist;

    public String getHotid() {
        return hotid;
    }

    public void setHotid(String hotid) {
        this.hotid = hotid;
    }

    public String getIs_zan() {
        return is_zan;
    }

    public void setIs_zan(String is_zan) {
        this.is_zan = is_zan;
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
        if (author == null) {
            author = "";
        } else {
            author = StringStyleUtil.removeSpecialCharacters(author);
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

    public int getFovnum() {
        return fovnum;
    }

    public void setFovnum(int fovnum) {
        this.fovnum = fovnum;
    }

    public int getUpnum() {
        return upnum;
    }

    public void setUpnum(int upnum) {
        this.upnum = upnum;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public boolean isfov() {
        return isfov;
    }

    public void setIsfov(boolean isfov) {
        this.isfov = isfov;
    }

    public String getHeadurl() {
        if (!TextUtils.isEmpty(headurl)) {
            headurl = headurl.replace(YinChaoConfig.DEFAULT_HEAD_URL, "");
        }
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getCreateday() {
        return createday;
    }

    public void setCreateday(String createday) {
        this.createday = createday;
    }

    public CommentListBean getCommentlist() {
        return commentlist;
    }

    public void setCommentlist(CommentListBean commentlist) {
        this.commentlist = commentlist;
    }
}
