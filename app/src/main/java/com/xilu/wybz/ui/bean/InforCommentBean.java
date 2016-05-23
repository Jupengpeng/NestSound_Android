package com.xilu.wybz.ui.bean;


import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;
import com.xilu.wybz.utils.StringStyleUtil;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/3 0003.
 * 这是消息 评论
 */
public class InforCommentBean implements Serializable {
    String pic;
    String author;
    String workname;
    String userid;
    String name;
    String headurl;
    int createday;
    String comment;
    String parentcomment;//如果是子评论,这个这段为主评论内容
    int status;//0-主评论,1-子评论
    String id;
    String workid;

    public String getWorkid() {
        return workid;
    }

    public void setWorkid(String workid) {
        this.workid = workid;
    }

    public String getParentcomment() {
        return parentcomment;
    }

    public void setParentcomment(String parentcomment) {
        this.parentcomment = parentcomment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        if (pic == null) {
            pic = "";
        }
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAuthor() {
        if(author==null){
            author="";
        }else{
            author = StringStyleUtil.removeSpecialCharacters(author);
        }
        return author;
    }

    public void setAuthor(String author) {
        if (author == null) {
            author = "";
        }
        this.author = author;
    }

    public String getWorkname() {
        if (workname == null) {
            workname = "未命名";
        }
        return workname;
    }

    public void setWorkname(String workname) {
        this.workname = workname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        if(name==null){
            name="";
        }else{
            name = StringStyleUtil.removeSpecialCharacters(name);
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadurl() {
        if(!TextUtils.isEmpty(headurl)){
            headurl = headurl.replace(YinChaoConfig.DEFAULT_HEAD_URL,"");
        }
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public int getCreateday() {
        return createday;
    }

    public void setCreateday(int createday) {
        this.createday = createday;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
