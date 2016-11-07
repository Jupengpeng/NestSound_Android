package com.xilu.wybz.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */

public class CooperaDetailsBean {


    /**
     * id : 19
     * userInfo : {"uid":25799,"bgpic":"1476700658.jpg","headurl":"http://pic.yinchao.cn/1476689907.jpg","nickname":"疯子","signature":"摩羯吉祥物图"}
     * completeList : [{"createtime":1478328600000,"title":"才有时间能参与","lUsername":"积极","pic":"http://pic.yinchao.cn/muscover/2016110514500069933101.jpg","itemid":9,"access":0,"wUsername":"疯子"},{"createtime":1478260080000,"title":"才有时间能参与","lUsername":"积极","pic":"http://pic.yinchao.cn/muscover/2016110419480028970415.jpg","itemid":7,"access":0,"wUsername":"疯子"}]
     * commentList : [{"uid":47794,"nickname":"积极","isread":1,"createdate":1478347273000,"target_uid":0,"comment_type":1,"itemid":19,"headerurl":"http://pic.yinchao.cn/headport/2016101415022560836214.jpg","type":2,"id":65,"targetheaderurl":"","comment":"馍","target_nickname":""},{"uid":47794,"nickname":"积极","isread":1,"createdate":1478347269000,"target_uid":0,"comment_type":1,"itemid":19,"headerurl":"http://pic.yinchao.cn/headport/2016101415022560836214.jpg","type":2,"id":64,"targetheaderurl":"","comment":"路","target_nickname":""},{"uid":47794,"nickname":"积极","isread":1,"createdate":1478347257000,"target_uid":0,"comment_type":1,"itemid":19,"headerurl":"http://pic.yinchao.cn/headport/2016101415022560836214.jpg","type":2,"id":63,"targetheaderurl":"","comment":"咯","target_nickname":""}]
     * demandInfo : {"id":19,"endtime":1479541773000,"createtime":1478245773000,"uid":25799,"commentnum":43,"requirement":"Yang yang yang","title":"才有时间能参与","status":1,"itemid":28718,"worknum":2,"lyrics":"想他欢迎您","iscollect":0}
     */

    private int id;
    /**
     * uid : 25799
     * bgpic : 1476700658.jpg
     * headurl : http://pic.yinchao.cn/1476689907.jpg
     * nickname : 疯子
     * signature : 摩羯吉祥物图
     */

    private UserInfoBean userInfo;
    /**
     * id : 19
     * endtime : 1479541773000
     * createtime : 1478245773000
     * uid : 25799
     * commentnum : 43
     * requirement : Yang yang yang
     * title : 才有时间能参与
     * status : 1
     * itemid : 28718
     * worknum : 2
     * lyrics : 想他欢迎您
     * iscollect : 0
     */

    private DemandInfoBean demandInfo;
    /**
     * createtime : 1478328600000
     * title : 才有时间能参与
     * lUsername : 积极
     * pic : http://pic.yinchao.cn/muscover/2016110514500069933101.jpg
     * itemid : 9
     * access : 0
     * wUsername : 疯子
     */

    private List<CompleteListBean> completeList;
    /**
     * uid : 47794
     * nickname : 积极
     * isread : 1
     * createdate : 1478347273000
     * target_uid : 0
     * comment_type : 1
     * itemid : 19
     * headerurl : http://pic.yinchao.cn/headport/2016101415022560836214.jpg
     * type : 2
     * id : 65
     * targetheaderurl :
     * comment : 馍
     * target_nickname :
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
        private String bgpic;
        private String headurl;
        private String nickname;
        private String signature;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getBgpic() {
            return bgpic;
        }

        public void setBgpic(String bgpic) {
            this.bgpic = bgpic;
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
        private int id;
        private long endtime;
        private long createtime;
        private int uid;
        private int commentnum;
        private String requirement;
        private String title;
        private int status;
        private int itemid;
        private int worknum;
        private String lyrics;
        private int iscollect;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getEndtime() {
            return endtime;
        }

        public void setEndtime(long endtime) {
            this.endtime = endtime;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
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

        public int getWorknum() {
            return worknum;
        }

        public void setWorknum(int worknum) {
            this.worknum = worknum;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public int getIscollect() {
            return iscollect;
        }

        public void setIscollect(int iscollect) {
            this.iscollect = iscollect;
        }
    }

    public static class CompleteListBean {
        private long createtime;
        private String title;
        private String lUsername;
        private String pic;
        private int itemid;
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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
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
        private long createdate;
        private int target_uid;
        private int comment_type;
        private int itemid;
        private String headerurl;
        private int type;
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

        public long getCreatedate() {
            return createdate;
        }

        public void setCreatedate(long createdate) {
            this.createdate = createdate;
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

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public String getHeaderurl() {
            return headerurl;
        }

        public void setHeaderurl(String headerurl) {
            this.headerurl = headerurl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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
