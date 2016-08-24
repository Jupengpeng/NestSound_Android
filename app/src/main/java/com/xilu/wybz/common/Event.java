package com.xilu.wybz.common;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by Administrator on 2016/3/4.
 */
public class Event {
    public static class UpdataUserBean {
        UserBean userBean;
        int type;

        public UpdataUserBean(UserBean userBean, int type) {
            this.type = type;
            this.userBean = userBean;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public UserBean getUserBean() {
            return userBean;
        }
    }

    public static class UpdataUserInfoBean {
        UserInfoBean userBean;
        int type;

        public UpdataUserInfoBean(UserInfoBean userBean, int type) {
            this.type = type;
            this.userBean = userBean;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public UserInfoBean getUserBean() {
            return userBean;
        }
    }

    public static class UpdataWorksList {
        WorksData worksData;
        int type;//0 灵感记录 1我的歌曲 2我的歌词 3我的收藏
        int change;//0 加 1减 2修改

        public int getChange() {
            return change;
        }

        public UpdataWorksList(WorksData worksData, int type, int change) {
            this.type = type;
            this.change = change;
            this.worksData = worksData;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public WorksData getWorksData() {
            return worksData;
        }
    }

    public static class UpdateWorkNum {
        WorksData worksData;
        int type;//1 喜欢 2 赞

        public UpdateWorkNum(WorksData worksData, int type) {
            this.type = type;
            this.worksData = worksData;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public WorksData getWorksData() {
            return worksData;
        }
    }
    public static class UpdateWorksNum {
        int type;//1歌曲 2歌词 3收藏 4灵感记录
        int count;//1加 -1减
        public UpdateWorksNum(int type, int count) {
            this.type = type;
            this.count = count;
        }

        public int getType() {
            return type;
        }
        public int getCount() {
            return count;
        }
    }
    public static class UpdataCommentNumEvent {
        int type;
        int num;

        public UpdataCommentNumEvent(int type, int num) {
            this.type = type;
            this.num = num;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getNum() {
            return num;
        }

    }

    public static class UpdataCommentListEvent {
        List<CommentBean> commentBeenList;

        public UpdataCommentListEvent(List<CommentBean> commentBeenList) {
            this.commentBeenList = commentBeenList;
        }


        public List<CommentBean> getCommentList() {
            return commentBeenList;
        }

    }

    public static class ShowSearchTabEvent {
        boolean hasData;

        public ShowSearchTabEvent(boolean hasData) {
            this.hasData = hasData;
        }

        public boolean isHasData() {
            return hasData;
        }
    }

    public static class HideKeyboardEvent {
    }

    public static class SaveSongSeccess {

    }

    public static class UpdateLyricsData {
        WorksData worksData;

        public UpdateLyricsData(WorksData worksData) {
            this.worksData = worksData;
        }

        public WorksData getWorksData() {
            return worksData;
        }
    }

    public static class SelectPicEvent {
        List<PhotoBean> pics;

        public SelectPicEvent(List<PhotoBean> pics) {
            this.pics = pics;
        }

        public List<PhotoBean> getPics() {
            return pics;
        }

    }

    public static class ImportWordEvent {
        WorksData worksData;

        public ImportWordEvent(WorksData worksData) {
            this.worksData = worksData;
        }

        public WorksData getWorksData() {
            return worksData;
        }

        public void setWorksData(WorksData worksData) {
            this.worksData = worksData;
        }
    }

    public static class ImportHotEvent {
        TemplateBean templateBean;

        public ImportHotEvent(TemplateBean templateBean) {
            this.templateBean = templateBean;
        }

        public TemplateBean getWorksData() {
            return templateBean;
        }

        public void setWorksData(TemplateBean templateBean) {
            this.templateBean = templateBean;
        }
    }

    public static class SaveLyricsSuccessEvent {
        int which;
        WorksData worksData;

        public SaveLyricsSuccessEvent(int which, WorksData worksData) {
            this.which = which;
            this.worksData = worksData;
        }

        public WorksData getLyricsdisplayBean() {
            return worksData;
        }

        public int getWhich() {
            return which;
        }

        public void setWhich(int which) {
            this.which = which;
        }
    }

    public static class UpdateUserInfo {
    }

    public static class MusicDataEvent {

    }

    public static class PPStatusEvent {
        int status;
        String from;

        public PPStatusEvent(int type, String come) {
            status = type;
            from = come;
        }

        public int getStatus() {
            return status;
        }

        public String getFrom() {
            return from;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class LoginSuccessEvent {
        UserBean userBean;

        public LoginSuccessEvent(UserBean userBean) {
            this.userBean = userBean;
        }

        public UserBean getUserBean() {
            return userBean;
        }
    }

    public static class LoginOutEvent {

    }

    public static class RemoveMySongEvent {
        String itemid;

        public RemoveMySongEvent(String itemid) {
            this.itemid = itemid;
        }

        public String getItemid() {
            return itemid;
        }
    }

    public static class UpdateFollowNumEvent {
        /*
        * type 关注后的状态
        *  from 0 关注 1粉丝 【修改关注数或粉丝数】
        */
        int type;
        int from;

        public UpdateFollowNumEvent(int type, int from) {
            this.type = type;
            this.from = from;
        }

        public int getType() {
            return type;
        }

        public int getFrom() {
            return from;
        }
    }

    public static class UpdataSecondProgressEvent {
        int percent;

        public UpdataSecondProgressEvent(int percent) {
            this.percent = percent;
        }

        public int getPercent() {
            return percent;
        }
    }

    public static class SavePosterSuccessEvent {

    }

    public static class AttendMatchSuccessEvent {

    }
}
