package com.xilu.wybz.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */

public class CooperaDetailsBean {

    /**
     * id : 5
     * userInfo : {"uid":22467,"headurl":"http://pic.yinchao.cn/xxx/12313231.png","nickname":"test","signature":"哈哈"}
     * completeList : [{"createtime":1477397884000,"title":"合作","lUsername":"词作者","itemid":1,"pic":"/123123.jpg","access":0,"wUsername":"曲作者"}]
     * commentList : [{"uid":54426,"nickname":"guan","isread":0,"target_uid":22467,"comment_type":2,"headerurl":"http://pic.yinchao.cn/headport/2016072013460041343001png","id":2,"targetheaderurl":"http://pic.yinchao.cn/xxx/12313231.png","comment":"合作需求回帖","target_nickname":"test"},{"uid":22467,"nickname":"test","isread":0,"target_uid":0,"comment_type":1,"headerurl":"http://pic.yinchao.cn/xxx/12313231.png","id":1,"targetheaderurl":"","comment":"评论合作需求测试","target_nickname":""}]
     * demandInfo : {"uid":22467,"createtime":1477468124000,"endtime":1478660051000,"id":5,"commentnum":2,"title":"wwwwwwwwwwwwwwwwww","requirement":"合作要求","status":1,"itemid":6,"lyrics":"wwwwwwwwwwwwwwwwww","worknum":1}
     */

    private int id;
    /**
     * uid : 22467
     * headurl : http://pic.yinchao.cn/xxx/12313231.png
     * nickname : test
     * signature : 哈哈
     */

    private UserInfoBean userInfo;
    /**
     * uid : 22467
     * createtime : 1477468124000
     * endtime : 1478660051000
     * id : 5
     * commentnum : 2
     * title : wwwwwwwwwwwwwwwwww
     * requirement : 合作要求
     * status : 1
     * itemid : 6
     * lyrics : wwwwwwwwwwwwwwwwww
     * worknum : 1
     */

    private DemandInfoBean demandInfo;
    /**
     * createtime : 1477397884000
     * title : 合作
     * lUsername : 词作者
     * itemid : 1
     * pic : /123123.jpg
     * access : 0
     * wUsername : 曲作者
     */

    private List<CompleteListBean> completeList;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public DemandInfoBean getDemandInfo() {
        return demandInfo;
    }

    public void setDemandInfo(DemandInfoBean demandInfo) {
        this.demandInfo = demandInfo;
    }

    public List<CompleteListBean> getCompleteList() {
        return completeList;
    }

    public void setCompleteList(List<CompleteListBean> completeList) {
        this.completeList = completeList;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public static class UserInfoBean {
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

    public static class DemandInfoBean {
        private int uid;
        private long createtime;
        private long endtime;
        private int id;
        private int commentnum;
        private String title;
        private String requirement;
        private int status;
        private int itemid;
        private String lyrics;
        private int worknum;

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

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRequirement() {
            return requirement;
        }

        public void setRequirement(String requirement) {
            this.requirement = requirement;
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

    public static class CompleteListBean {
        private long createtime;
        private String title;
        private String lUsername;
        private int itemid;
        private String pic;
        private int access;
        private String wUsername;

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLUsername() {
            return lUsername;
        }

        public void setLUsername(String lUsername) {
            this.lUsername = lUsername;
        }

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getAccess() {
            return access;
        }

        public void setAccess(int access) {
            this.access = access;
        }

        public String getWUsername() {
            return wUsername;
        }

        public void setWUsername(String wUsername) {
            this.wUsername = wUsername;
        }
    }

    public static class CommentListBean {
        private int uid;
        private String nickname;
        private int isread;
        private int target_uid;
        private int comment_type;
        private String headerurl;
        private int id;
        private String targetheaderurl;
        private long createdate;
        private String comment;
        private String target_nickname;

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
