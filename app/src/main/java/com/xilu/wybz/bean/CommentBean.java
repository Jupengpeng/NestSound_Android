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
    public int id;
    public int itemid;
    public int uid;
    public int type;
    public int comment_type;
    public long createdate;
    public String comment;
    public String headerurl;
    public String nickname;
    public String target_nickname;
    public int target_uid;

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
