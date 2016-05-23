package com.xilu.wybz.ui.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;
import com.xilu.wybz.utils.StringStyleUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zning on 2015/9/7.
 */
public class CommentBean implements Serializable {

    public String id;
    public String userid;
    public String name;
    public String headurl;
    public String createday;
    public String comment;
    public List<CommentBean> childcommentlist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        if (name == null) {
            name = "";
        } else {
            name = StringStyleUtil.removeSpecialCharacters(name);
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCreateday() {
        return createday;
    }

    public void setCreateday(String createday) {
        this.createday = createday;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<CommentBean> getChildcommentlist() {
        if (childcommentlist == null) {
            childcommentlist = new ArrayList<>();
        }
        return childcommentlist;
    }

    public void setChildcommentlist(List<CommentBean> childcommentlist) {
        this.childcommentlist = childcommentlist;
    }
}
