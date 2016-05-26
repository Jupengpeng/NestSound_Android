package com.xilu.wybz.common;
/**
 * Created by June on 2016/5/4.
 */
public class MyHttpClient {
    public static final String ROOT_URL = "http://112.124.125.2/";
//    public static final String ROOT_URL = "http://192.168.0.123:8080/";
    public static String QINIU_URL = "http://7xru8x.com2.z0.glb.qiniucdn.com/";
    public static final String BASE_URL = ROOT_URL+"java/";

    public static String getQnToken(){
        return "common/uploadcallback/qiniutoken";
    }
    //伴奏列表
    public static String getHotUrl() {
        return "common/accompaniment/list";
    }
    //伴奏详情
    public static String getHotDetailUrl() {
        return "common/hot/detail";
    }
    //首页数据
    public static String getHomeUrl() {
        return "common/index/app";
    }
    //乐说列表
    public static String getMusicTalkUrl() {
        return "common/yueshuo/list";
    }
    //歌曲详情
    public static String getMusicWorkUrl() {
        return "common/music/work";
    }
    //歌词详情
    public static String getLyricsdisplay() {
        return "common/lyrics/detail";
    }
    //反馈
    public static String getFeed() {
        return "feedback/save";
    }
    //举报
    public static String getUserReport() {
        return "report/save";
    }
    //修改用户资料
    public static String getModifyUserInfo() {
        return "userCenter/updateuserinfo";
    }
    //歌单列表
    public static String getGleeListUrl() {
        return "common/glee/list";
    }
    //歌单详情
    public static String getGleeDetailUrl() {
        return "common/glee/detail";
    }
    //发现里面歌曲
    public static String getFindSongList() {
        return "common/find/music";
    }
    //发现里面更多歌曲
    public static String getFindMoreSongList() {
        return "common/find/moremusic";
    }
    //发现里面歌词
    public static String getFindLyricsList() {
        return "common/find/lyrics";
    }
    //发现里面更多歌词
    public static String getFindMoreLyricsList() {
        return "common/find/morelyrics";
    }
    //发现里面歌曲
    public static String getFindActivityList() {
        return "common/find/activelist";
    }
    //发现里面排行榜
    public static String getRankingList() {
        return "common/find/rankList";
    }
    //登录
    public static String getLoginUrl() {
        return "common/login/mobile";
    }
    //注册
    public static String getRegiterUrl() {
        return "common/regist/mobile";
    }
    //修改密码
    public static String getPasswordUrl() {
        return "common/login/resetPassword";
    }
    //注册验证码发送
    public static String getSmsCodeUrl() {
        return "common/send/code";
    }
    //用户歌词列表
    public static String getUserLyricsListUrl(){return  "common/lyrics/myLyrics";}
    //点赞接口
    public static String getUpvoteUrl(){return  "workzan/optZan";}
    //收藏接口
    public static String getWorkFovUrl(){return  "workfov/optZan";}
    //消息收藏列表
    public static String getMsgFovList(){return  "message/fovMyCenterList";}
    //消息评论列表
    public static String getMsgCommentList(){return  "message/commentCenterList";}
    //消息点赞列表
    public static String getMsgZanList(){return  "message/zanCenterList";}
    //消息点赞列表
    public static String getMsgSystemList(){return  "message/sysNoteList";}
    //发布歌词
    public static String getSaveLyricsUrl(){return  "lyricsopt/optLyrics";}
    //保存灵感记录
    public static String getSaveInspireUrl(){return  "inspire/save";}

    public static String getUserCenter(){return  "userCenter/index";}
    public static String getOtherCenter(){return  "otherCenter/index";}
    public static String getLyricatsUrl(){return  "lyricsopt/getLyriCats";}
    //发表评论
    public static String getCommentListUrl(){return "common/comment/list";}
    //评论接口
    public static String getSaveCommentUrl(){return  "commentopt/save";}
    //删除评论
    public static String getDelCommentUrl(){return "commentopt/delete";}
}

