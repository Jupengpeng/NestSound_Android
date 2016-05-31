package com.xilu.wybz.bean;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/3 0003.
 * 这是消息 评论
 */
public class MsgCommentBean implements Serializable {
    public int workid;//作品ID
    public int id;//评论ID
    public int itemid;//作品ID
    public int uid;//发评论者的ID
    public int type;//1=歌曲，2=歌词
    public int comment_type; //1=默认，2=跟帖
    public long createdate;//发布时间
    public String author;//作者名称
    public String title;//作品标题
    public String pic;//作品图片
    public String comment;//评论内容
    public String headerurl;//评论人头像
    public String nickname;//发评论者的昵称
    public String targetname;//被评论跟帖者的昵称

    public int getTarget_uid() {
        return target_uid;
    }

    public void setTarget_uid(int target_uid) {
        this.target_uid = target_uid;
    }

    public int getWorkid() {
        return workid;
    }

    public void setWorkid(int workid) {
        this.workid = workid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getComment_type() {
        return comment_type;
    }

    public void setComment_type(int comment_type) {
        this.comment_type = comment_type;
    }

    public long getCreatedate() {
        return createdate;
    }

    public void setCreatedate(long createdate) {
        this.createdate = createdate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHeaderurl() {
        return headerurl;
    }

    public void setHeaderurl(String headerurl) {
        this.headerurl = headerurl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname;
    }

    public int target_uid; //被评论者的id

}
