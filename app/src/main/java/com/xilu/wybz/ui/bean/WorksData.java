package com.xilu.wybz.ui.bean;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.enums.Relation;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Created by June on 16/5/3.
 * 歌曲 歌词 或者 灵感记录
 */
public class WorksData implements Serializable {
    public String itemid;//歌曲id
    public String pic;//歌曲封面
    public String title;//标题
    public String name;//标题
    public String author;//作者
    public String headurl;//用户头像
    public int looknum;//浏览量
    public int isOpen;//是否公开
    public int fovnum;//收藏量
    public int zannum;//点赞量
    public int commentnum;//评论量
    public String shareurl;//分享地址
    public int sampleid;//模板Id
    public String playurl;//mp3地址
    public int mp3times;//mp3时长
    public String hotmp3;//伴奏播放地址
    public int hotmp3times;//伴奏时长 秒
    public int status;//1歌曲 2歌词
    public String lyrics;//歌词
    public String detail;//歌词详情 灵感记录的内容
    public String prev;//上一首
    public String next;//下一首
    public String come;
    public int iscollect;//1=已收藏，0=未收藏
    public int isZan;//1=已赞，0=未赞
    public int uid;//作者id
    public int hotid;//伴奏id
    public String hotAuthor;//伴奏作者
    public String hotTitle;//伴奏标题
    public String createTime;//创建时间
    public int recordtimes;//录音时长
    public String recordPath;//录歌时人声或者灵感记录的音频
    public String picList;//灵感记录的图片集合

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShareurl() {
        return shareurl;
    }

    public void setShareurl(String shareurl) {
        this.shareurl = shareurl;
    }

    public String getHotmp3() {
        return hotmp3;
    }

    public void setHotmp3(String hotmp3) {
        this.hotmp3 = hotmp3;
    }

    public int getHotmp3times() {
        return hotmp3times;
    }

    public void setHotmp3times(int hotmp3times) {
        this.hotmp3times = hotmp3times;
    }

    public void setHotAuthor(String hotAuthor) {
        this.hotAuthor = hotAuthor;
    }

    public String getPicList() {
        return picList;
    }

    public void setPicList(String picList) {
        this.picList = picList;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
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

    public void getName(String title) {
        this.name = name;
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

    public int getZannum() {
        return zannum;
    }

    public void setZannum(int zannum) {
        this.zannum = zannum;
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

    public String getHotAuthor() {
        return hotAuthor;
    }

    public void setHotAuhtor(String hotAuthor) {
        this.hotAuthor = hotAuthor;
    }

    public String getHotTitle() {
        return hotTitle;
    }

    public void setHotTitle(String hotTitle) {
        this.hotTitle = hotTitle;
    }

    public int getSampleid() {
        return sampleid;
    }

    public void setSampleid(int sampleid) {
        this.sampleid = sampleid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateTime() {
        if(StringUtil.isLong(createTime)){
            long time = Long.parseLong(createTime);
            createTime = DateTimeUtil.timestamp2Time(time);
        }
        return createTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getRecordtimes() {
        return recordtimes;
    }

    public void setRecordtimes(int recordtimes) {
        this.recordtimes = recordtimes;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public void setRecordPath(String recordPath) {
        this.recordPath = recordPath;
    }

    public String getPics() {
        return picList;
    }

    public void setPics(String picList) {
        this.picList = picList;
    }
}
