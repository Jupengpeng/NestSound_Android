package com.xilu.wybz.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class CooperationBean implements Serializable{

    /**
     * uid : 22467
     * headurl : http://pic.yinchao.cn/xxx/12313231.png
     * nickname : test
     * signature : 哈哈
     */

    private UserInfoBean userInfo;
    /**
     * userInfo : {"uid":22467,"headurl":"http://pic.yinchao.cn/xxx/12313231.png","nickname":"test","signature":"哈哈"}
     * id : 5
     * commentList : [{"uid":54426,"nickname":"guan","isread":0,"target_uid":22467,"comment_type":2,"headerurl":"http://pic.yinchao.cn/headport/2016072013460041343001png","id":2,"targetheaderurl":"http://pic.yinchao.cn/xxx/12313231.png","comment":"合作需求回帖","target_nickname":"test"},{"uid":22467,"nickname":"test","isread":0,"target_uid":0,"comment_type":1,"headerurl":"http://pic.yinchao.cn/xxx/12313231.png","id":1,"targetheaderurl":"","comment":"评论合作需求测试","target_nickname":""}]
     * demandInfo : {"endtime":1478660051000,"uid":22467,"createtime":1477398766000,"id":5,"commentnum":2,"requirement":"合作要求","title":"wwwwwwwwwwwwwwwwww","status":1,"itemid":6,"lyrics":"wwwwwwwwwwwwwwwwww","worknum":1}
     */

    private int id;
    /**
     * endtime : 1478660051000
     * uid : 22467
     * createtime : 1477398766000
     * id : 5
     * commentnum : 2
     * requirement : 合作要求
     * title : wwwwwwwwwwwwwwwwww
     * status : 1
     * itemid : 6
     * lyrics : wwwwwwwwwwwwwwwwww
     * worknum : 1
     */

    private DemandInfoBean demandInfo;
    /**
     * uid : 54426
     * nickname : guan
     * isread : 0
     * target_uid : 22467
     * comment_type : 2
     * headerurl : http://pic.yinchao.cn/headport/2016072013460041343001png
     * id : 2
     * targetheaderurl : http://pic.yinchao.cn/xxx/12313231.png
     * comment : 合作需求回帖
     * target_nickname : test
     */

    private List<CommentListBean> commentList;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DemandInfoBean getDemandInfo() {
        return demandInfo;
    }

    public void setDemandInfo(DemandInfoBean demandInfo) {
        this.demandInfo = demandInfo;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class UserInfoBean implements Serializable{
        private int uid;
        private String headurl;
        private String nickname;
        private String signature;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getHeadurl() {
            return headurl;
        }

        public void setHeadurl(String headurl) {
            this.headurl = headurl;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    public static class DemandInfoBean implements  Serializable{
        private long endtime;
        private int uid;
        private long createtime;
        private int id;
        private int commentnum;
        private String requirement;
        private String title;
        private int status;
        private int itemid;
        private String lyrics;
        private int worknum;

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCommentnum() {
            return commentnum;
        }

        public void setCommentnum(int commentnum) {
            this.commentnum = commentnum;
        }

        public String getRequirement() {
            return requirement;
        }

        public void setRequirement(String requirement) {
            this.requirement = requirement;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public int getWorknum() {
            return worknum;
        }

        public void setWorknum(int worknum) {
            this.worknum = worknum;
        }
    }

    public static class CommentListBean implements Serializable{
        private int uid;
        private String nickname;
        private int isread;
        private int target_uid;
        private int comment_type;
        private String headerurl;
        private int id;
        private String targetheaderurl;
        private String comment;
        private String target_nickname;

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

        public int getIsread() {
            return isread;
        }

        public void setIsread(int isread) {
            this.isread = isread;
        }

        public int getTarget_uid() {
            return target_uid;
        }

        public void setTarget_uid(int target_uid) {
            this.target_uid = target_uid;
        }

        public int getComment_type() {
            return comment_type;
        }

        public void setComment_type(int comment_type) {
            this.comment_type = comment_type;
        }

        public String getHeaderurl() {
            return headerurl;
        }

        public void setHeaderurl(String headerurl) {
            this.headerurl = headerurl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getTarget_nickname() {
            return target_nickname;
        }

        public void setTarget_nickname(String target_nickname) {
            this.target_nickname = target_nickname;
        }
    }
}
