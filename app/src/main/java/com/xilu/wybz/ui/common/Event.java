package com.xilu.wybz.ui.common;

import com.xilu.wybz.bean.PhotoBean;
import com.xilu.wybz.bean.WorksData;

import java.util.HashSet;
import java.util.List;

/**
 * Created by Administrator on 2016/3/4.
 */
public class Event {
    public static class DelFocusEvent {
    }
    public static class ShowSearchTabEvent {
    }
    public static class SelectPicEvent {
        List<PhotoBean> pics;
        public SelectPicEvent(List<PhotoBean> pics){
            this.pics=pics;
        }

        public List<PhotoBean> getPics() {
            return pics;
        }

        public void setPics(List<PhotoBean> pics) {
            this.pics = pics;
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

    public static class PublishSuccessEvent {
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

        public void setLyricsdisplayBean(WorksData worksData) {
            this.worksData = worksData;
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

    public static class PlayMusicEvent {
        int pos;
        String from;

        public PlayMusicEvent(int pos, String from) {
            this.pos = pos;
            this.from = from;
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }
    }

    public static class ReplyComment {
        String id;

        public ReplyComment(String mId) {
            id = mId;
        }

        public String getId() {
            return id;
        }
    }

    public static class ChangeLeftEvent {
    }

    public static class HideMsgEvent {
        int type;

        public HideMsgEvent(int mType) {
            type = mType;
        }

        public int getType() {
            return type;
        }
    }

    public static class ChangeFocusNumEvent {
        int type;

        public ChangeFocusNumEvent(int mType) {
            type = mType;
        }

        public int getType() {
            return type;
        }
    }

    public static class LoginSuccessEvent {
    }

    public static class LoginOutEvent {
    }

    public static class HideMsgNumEvent {
    }

    public static class DelPosEvent {
        HashSet<Integer> ids;
        HashSet<Integer> postions;

        public DelPosEvent(HashSet<Integer> mIds, HashSet<Integer> mPositions) {
            ids = mIds;
            postions = mPositions;
        }

        public HashSet<Integer> getIds() {
            return ids;
        }

        public HashSet<Integer> getPositions() {
            return postions;
        }
    }
}
