package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by Zning on 2015/9/2.
 */
public class MusicBean implements Serializable {

    public String itemid;//歌曲id
    public String pic;//歌曲封面
    public String title;//标题
    public String author;//作者
    public String headurl;//用户头像
    public int looknum;
    public int fovnum;
    public int upnum;
    public String playurl;//mp3地址
    public int mp3times;//mp3时长
    public boolean isvisibleselect;//是否可以选择
    public boolean ischecked;//是否选中
    public int status;//1歌曲 2歌词
    public String lyrics;//歌词
    public String prev;
    public String next;//下一首
    public String come;//上一首
    public int iscollect;////1=已收藏，0=未收藏
    public int isZan;//点赞判断
    public int uid;//作者id
    public int hotid;//伴奏id
    public String hotAutor;
    public String hotTitle;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getLooknum() {
        return looknum;
    }

    public void setLooknum(int looknum) {
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

    public String getPlayurl() {
        return playurl;
    }

    public void setPlayurl(String playurl) {
        this.playurl = playurl;
    }

    public int getMp3times() {
        return mp3times;
    }

    public void setMp3times(int mp3times) {
        this.mp3times = mp3times;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
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

    public String getCome() {
        return come;
    }

    public void setCome(String come) {
        this.come = come;
    }

    public int getIscollect() {
        return iscollect;
    }

    public void setIscollect(int iscollect) {
        this.iscollect = iscollect;
    }

    public int getIsZan() {
        return isZan;
    }

    public void setIsZan(int isZan) {
        this.isZan = isZan;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getHotid() {
        return hotid;
    }

    public void setHotid(int hotid) {
        this.hotid = hotid;
    }

    public String getHotAutor() {
        return hotAutor;
    }

    public void setHotAutor(String hotAutor) {
        this.hotAutor = hotAutor;
    }

    public String getHotTitle() {
        return hotTitle;
    }

    public void setHotTitle(String hotTitle) {
        this.hotTitle = hotTitle;
    }
}
