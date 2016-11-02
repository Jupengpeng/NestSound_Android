package com.xilu.wybz.bean;

/**
 * Created by Administrator on 2016/10/28.
 */

public class CooperaMessageBean {

    int id;
    int itemid;
    int isread;
    long createdate;
    int uid;
    String nickname;
    String headerurl;
    int target_uid;
    String target_nickname;
    String targetheaderurl;
    String comment;
    int comment_type;


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

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
    }

    public long getCreatedate() {
        return createdate;
    }

    public void setCreatedate(long createdate) {
        this.createdate = createdate;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeaderurl() {
        return headerurl;
    }

    public void setHeaderurl(String headerurl) {
        this.headerurl = headerurl;
    }

    public int getTarget_uid() {
        return target_uid;
    }

    public void setTarget_uid(int target_uid) {
        this.target_uid = target_uid;
    }

    public String getTarget_nickname() {
        return target_nickname;
    }

    public void setTarget_nickname(String target_nickname) {
        this.target_nickname = target_nickname;
    }

    public String getTargetheaderurl() {
        return targetheaderurl;
    }

    public void setTargetheaderurl(String targetheaderurl) {
        this.targetheaderurl = targetheaderurl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getComment_type() {
        return comment_type;
    }

    public void setComment_type(int comment_type) {
        this.comment_type = comment_type;
    }
}
