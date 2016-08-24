package com.xilu.wybz.bean;


import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;
import com.xilu.wybz.utils.StringStyleUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/3 0003.
 * 这是消息 评论
 */
public class CommentBean implements Serializable {
    public int id;//评论ID
    public String itemid;//作品ID
    public int type;//1=歌曲，2=歌词
    public int comment_type; //1=默认，2=跟帖
    public long createdate;//发布时间
    public String comment;//评论内容

    public int uid;//发评论者的ID
    public String headerurl;//评论人头像
    public String nickname;//发评论者的昵称

    public String targetheaderurl;//被评论人头像
    public String target_nickname;//被评论跟帖者的昵称
    public int target_uid; //被评论者的id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
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

    public String getTarget_nickname() {
        return target_nickname;
    }

    public void setTarget_nickname(String target_nickname) {
        this.target_nickname = target_nickname;
    }

    public int getTarget_uid() {
        return target_uid;
    }

    public void setTarget_uid(int target_uid) {
        this.target_uid = target_uid;
    }
}
