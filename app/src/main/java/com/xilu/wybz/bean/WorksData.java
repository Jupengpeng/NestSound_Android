package com.xilu.wybz.bean;


import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by June on 16/5/3.
 * 歌曲 歌词 或者 灵感记录
 */
//@Table("workdata")
public class WorksData implements Serializable {
    public String itemid;//歌曲id
    public String pic;//歌曲封面
    public String title;//标题
    public String name;//标题
    public String author;//作者
    public String headurl;//用户头像
    public int looknum;//浏览量
    public int fovnum;//收藏量
    public int zannum;//点赞量
    public int commentnum;//评论量
    public String shareurl;//分享地址
    public int sampleid;//模板Id
    public String playurl;//mp3地址
    public String mp3;//mp3地址
    public int mp3times;//mp3时长
    public String hotmp3;//伴奏播放地址
    public int hotmp3times;//伴奏时长 秒
    public int status;//是否公开 0不公开 1公开
    public int is_issue;//是否公开 0不公开 1公开
    public int type;//1歌曲 2歌词
    public String lyrics;//歌词
    public String detail;//歌词详情 灵感记录的内容
    public int prev;//上一首
    public int next;//下一首
    public String come;//列表来源
    public int iscollect;//1=已收藏，0=未收藏
    public int isZan;//1=已赞，0=未赞
    public int uid;//作者id
    public int hotid;//伴奏id
    public String hotAuthor;//伴奏作者
    public String hotTitle;//伴奏标题
    public long createTime;//创建时间
    public long createtime;//创建时间
    public long createdate;//创建时间
    public int recordtimes;//录音时长
    public String recordPath;//录歌时人声或者灵感记录的音频
    public String audio;//录歌时人声或者灵感记录的音频
    public String spirecontent;//录歌时人声或者灵感记录的音频
    public String pics;//灵感记录的图片集合
    public String createtype;//HOT/DIY
    public String useheadset;//耳机1是，0否
    public String recordmp3;
    public String recordurl;
    public String musicurl;
    public String diyids;//歌词详情页返回的描述
    public boolean isPlay;//是否播放

    public int effect;//音效

    /**
     * 合作
     *"wUid":作曲人id,
     "wUsername:"作曲人姓名",
     "lUid":作词人id,
     "lUsername:"作词人姓名",
     */
    public String wUid;
    public String wUsername ="test1";
    public String lUid;
    public String lUsername = "test2";

    /**
     *
     * @return
     */
    public String getComAuthor(){
        String result="作曲人姓名:";
        if(StringUtils.isNotBlank(wUsername)){
            result += wUsername;
        }
        result += "\n作词人姓名:";
        if(StringUtils.isNotBlank(lUsername)){
            result += lUsername;
        }
        return result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreatedate() {
        return createdate;
    }

    public void setCreatedate(long createdate) {
        this.createdate = createdate;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getSpirecontent() {
        return spirecontent;
    }

    public void setSpirecontent(String spirecontent) {
        this.spirecontent = spirecontent;
    }

    public String getCreatetype() {
        return createtype;
    }

    public void setCreatetype(String createtype) {
        this.createtype = createtype;
    }

    public String getUseheadset() {
        return useheadset;
    }

    public void setUseheadset(String useheadset) {
        this.useheadset = useheadset;
    }

    public String getRecordmp3() {
        return recordmp3;
    }

    public void setRecordmp3(String recordmp3) {
        this.recordmp3 = recordmp3;
    }

    public String getRecordurl() {
        return recordurl;
    }

    public void setRecordurl(String recordurl) {
        this.recordurl = recordurl;
    }

    public String getMusicurl() {
        return musicurl;
    }

    public void setMusicurl(String musicurl) {
        this.musicurl = musicurl;
    }

    public String getDiyids() {
        return diyids;
    }

    public void setDiyids(String diyids) {
        this.diyids = diyids;
    }


    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

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
        if(StringUtils.isNotBlank(pic)&&!pic.startsWith("http")){
            pic = MyHttpClient.QINIU_URL+pic;
        }
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

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
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

    public long getCreateTime() {
        return createTime;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
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
}
