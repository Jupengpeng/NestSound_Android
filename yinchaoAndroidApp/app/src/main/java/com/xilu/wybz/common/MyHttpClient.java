package com.xilu.wybz.common;

/**
 * Created by June on 2016/5/4.
 */
public class MyHttpClient {


    public final static String RELEASE_ROOT_URL = "http://api.yinchao.cn/";
    public final static String TEST_ROOT_URL = "http://112.124.125.2/";
    public final static String TEMP_ROOT_URL = "http://112.124.125.2/";

    public final static String BASE_PATH = "java/";

    public static String ROOT_URL = RELEASE_ROOT_URL;
    public static String PRE_ROOT = ROOT_URL.substring(0,RELEASE_ROOT_URL.length()-1);
    public static String BASE_URL = ROOT_URL + BASE_PATH;

    public static String QINIU_AUDIO_URL = "http://audio.yinchao.cn/";
    public static String QINIU_URL = "http://pic.yinchao.cn/";



    public static String PROTOCOL_1 = "";
    public static String PROTOCOL_2 = "";
    public static String PROTOCOL_3 = "";


    public static String getQnToken() {
        return "common/uploadcallback/qiniutoken";
    }

    public static String getTokenCheck() {
        return "common/login/tokenCheck";
    }

    //伴奏分类列表
    public static String getHotUrl() {
        return "common/accompaniment/list/v22";
    }
    //伴奏默认列表
    public static String getDefaultHotListUrl() {
        return "common/accompaniment/defaultlist/v22";
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

    //退出登录
    public static String getLoginOut() {
        return "common/login/logout";
    }

    //注册
    public static String getRegiterUrl() {
        return "common/regist/mobile";
    }

    //修改密码
    public static String getPasswordUrl() {
        return "userCenter/updateuserpass";
    }
    //重置密码
    public static String getResetPwdUrl() {
        return "common/login/resetPassword";
    }
    //注册验证码发送
    public static String getSmsCodeUrl() {
        return "common/send/code";
    }

    //用户歌词列表
    public static String getUserLyricsListUrl() {
        return "common/lyrics/myLyrics";
    }

    //用户歌曲列表
    public static String getUserMusicListUrl() {
        return "common/music/worklist";
    }

    //点赞接口
    public static String getUpvoteUrl() {
        return "workzan/optZan/v22";
    }

    //收藏接口
    public static String getWorkFovUrl() {
        return "workfov/optZan/v22";
    }

    //消息收藏列表
    public static String getMsgFovList() {
        return "message/fovMyCenterList/v22";
    }

    //消息评论列表
    public static String getMsgCommentList() {
        return "message/commentCenterList/v22";
    }

    //消息点赞列表
    public static String getMsgZanList() {
        return "message/zanCenterList/v22";
    }

    //消息点赞列表
    public static String getMsgSystemList() {
        return "message/sysNoteList/v22";
    }

    //发布歌词
    public static String getSaveLyricsUrl() {
        return "lyricsopt/optLyrics";
    }
    //发布歌词(活动)
    public static String getSaveActivityLyricsUrl() {
        return "lyricsopt/activity";
    }
    //保存灵感记录
    public static String getSaveInspireUrl() {
        return "inspire/save";
    }

    //修改灵感记录
    public static String getUpdateInspireUrl() {
        return "inspire/update";
    }

    //个人主页
    public static String getUserCenter() {
        return "userCenter/index21";
    }

    //他人主页
    public static String getOtherCenter() {
        return "common/otherCenter/index21";
    }

    //歌词
    public static String getLyricatsUrl() {
        return "lyricsopt/getLyriCats";
    }

    //发表评论
    public static String getCommentListUrl() {
        return "common/comment/list";
    }

    //评论接口
    public static String getSaveCommentUrl() {
        return "commentopt/save/v22";
    }

    //删除评论
    public static String getDelCommentUrl() {
        return "commentopt/delete";
    }

    //更新IP
    public static String getBootPic() {
        return "common/boot/pic";
    }

    //粉丝列表
    public static String getFansList() {
        return "fanfocus/fanslist/v22";
    }

    //他人粉丝列表
    public static String getOtherFansList() {
        return "fanfocus/otherfanslist";
    }

    //搜索接口
    public static String getSearchList() {
        return "common/search/search";
    }

    //搜索接口
    public static String getFanFocusList() {
        return "fanfocus/optFans/v22";
    }

    //上传歌曲
    public static String getuploadmp3Url() {
        return BASE_URL + "common/uploadcallback/uploadmp3";
    }

    //合成歌曲
    public static String getTuningSongUrl() {
        return "musiopt/tuningMusic";
    }

    //发布歌曲
    public static String getSaveSongUrl() {
        return "musiopt/optMusic";
    }


    //删除我的作品
    public static String getDeleteWorksUrl() {
        return "userCenter/deleteWork";
    }

    //更新歌曲 歌词公开的状态
    public static String getUpdateMusicUrl() {
        return "musiopt/updateMusic";
    }

    public static String getUpdateLyricsUrl() {
        return "lyricsopt/updateLyrics";
    }

    //伴奏分类
    public static String getHotCatalogUrl() {
        return "common/accompanimentcategory/list/v22";
    }

    //音乐人列表
    public static String getMusicianListUrl() {
        return "common/musician/list";
    }

    //音乐人详情
    public static String getMusicianDetailUrl() {
        return "common/musician/detail";
    }

    public static String getLyricstemplateList() {
        return "lyricstemplate/list";
    }

    public static String getDraftList() {
        return "draft/list";
    }

    public static String getDraftSave() {
        return "draft/save";
    }

    public static String getDraftDelete() {
        return "draft/delete";
    }

    //专题活动
    public static String getMatchInfo() {
        return "common/activity/actdetail";
    }
    //专题参与人列表
    public static String getJoinUserList() {
        return "common/activity/userlist";
    }
    //专题活动作品列表
    public static String getMatchWorkList() {
        return "common/activity/worklist";
    }
    //专题活动作品列表
    public static String getOverMatchWorkList() {
        return "common/activity/overlist";
    }

    //专题活动作品列表
    public static String getAttendUrl() {
        return "activity/attend";
    }
    public static String getUpdateuserbgUrl() {
        return "userCenter/updateuserbg";
    }

    //未读消息
    public static String getMsgNumUrl(){
        return "message/center/v22";
    }
}
