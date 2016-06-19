package com.xilu.wybz.common;

import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.bean.UserBean;
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

    public static class ShowSearchTabEvent {
        boolean hasData;
        public ShowSearchTabEvent(boolean hasData){
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

        public PPStatusEvent(int type) {
            status = type;
        }

        public int getStatus() {
            return status;
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
        int itemid;
        public RemoveMySongEvent(int itemid){
            this.itemid = itemid;
        }
        public int getItemid(){
            return itemid;
        }
    }

    public static class UpdateFollowNumEvent {
        /*
        * type 0加 1减
        *  from 0 关注 1粉丝
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
}
