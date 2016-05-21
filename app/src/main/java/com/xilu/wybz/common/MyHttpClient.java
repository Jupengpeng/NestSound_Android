package com.xilu.wybz.common;

import android.util.Log;


/**
 * Created by June on 2016/5/4.
 */
public class MyHttpClient {
    public static final String ROOT_URL = "http://112.124.125.2/";
    public static String QINIU_URL = "http://7xru8x.com2.z0.glb.qiniucdn.com/";
    public static final String BASE_URL = ROOT_URL+"java/";

    public static String getQnToken(){
        return "common/uploadcallback/qiniutoken";
    }
    //伴奏列表
    public static String getHotUrl() {
        return "common/accompaniment/list";
    }
    //首页数据
    public static String getHomeUrl() {
        return "common/index/app";
    }
    //歌曲详情
    public static String getMusicWorkUrl() {
        return "common/music/work";
    }
    //歌单列表
    public static String getGleeListUrl(){
        return "common/glee/list";
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
    public static String getRecommendListUrl() {
        return "common/glee/detail";
    }
    //发现里面歌曲
    public static String getFindSongList() {
        return "common/find/moremusic";
    }
    //发现里面歌曲
    public static String getFindLyricsList() {
        return "common/find/morelyrics";
    }
    //活动
    public static String getEventUrl() {
        return String.format("/getevent.php");
    }

    //我的列表
    public static String getMineUrl(String userid) {
        return String.format("/getmine.php?userid=%s", userid);
    }

    //我的歌词列表
    public static String getmylyrics(String userid, int page) {
        return String.format("/getmylyrics.php?userid=%s&page=%s", userid, page);
    }

    //韵脚列表
    public static String getyunjiao() {
        return String.format("/getyunjiao.php");
    }


    //伴奏信息
    public static String getHotDetailUrl(String id) {
        return String.format("/gethotiddetail.php?id=%s", id);
    }

    public static String getLyricsUrl(String keyword) {
        return String.format("/getlyrics.php?keyword=%s", keyword);
    }

    //热门录制曲目接口
    public static String getHotTemplateUrl() {
        return String.format("/gethottemplate.php");
    }

    //DIY录制曲目接口
    public static String getDiyTemplateUrl() {
        return "/getdiytemplate.php";
    }

    //公共上传接口
    public static String getCommonUrl() {
        return "/upload.php";
    }

    public static String getSaveLyricsUrl() {
        return "/newsetsavelyrics.php";
    }

    //收藏
    public static String getAddFavUrl(String musicId, String userId) {
        return String.format("/setaddfov.php?id=%s&userid=%s", musicId, userId);
    }

    //删除收藏
    public static String getRemoveFavUrl(String musicId, String userId) {
        return String.format("/setremovefov.php?id=%s&userid=%s", musicId, userId);
    }

    //批量删除收藏
    public static String getRemoveSomeFavUrl(String musicId, String userId) {
        return String.format("/setremovesomefov.php?id=%s&userid=%s", musicId, userId);
    }

    //点赞
    public static String getUpvoteUrl() {
        return String.format("/setupvote.php");
    }

    public static String getPlayUrl(String musicId, String deviceId) {
        return String.format("/setplay.php?id=%s&deviceid=%s", musicId, deviceId);
    }

    public static String getDelMusicUrl(String musicId, String userId) {
        return String.format("/setdelsomemusic.php?id=%s&userid=%s", musicId, userId);
    }

    public static String getDelLyricsUrl(String musicId, String userId) {
        return String.format("/setdelsomelyrics.php?id=%s&userid=%s", musicId, userId);
    }

    public static String getLoginWechatUrl(String code) {
        return String.format("/loginbyweixin.php?code=%s", code);
    }

    public static String getLoginSinaUrl(String code) {
        return String.format("/loginbyweibo.php?code=%s", code);
    }

    public static String getLoginQQUrl(String openid, String token) {
        return String.format("/loginbyqq.php?openid=%s&token=%s", openid, token);
    }


    public static String getLoginPhoneUrl(String mobile, String code) {
        return String.format("/loginbymob.php?mobile=%s&code=%s", mobile, code);
    }

    //登录
    public static String getLoginUrl() {
        return "login/mobile";
    }

    //注册
    public static String getRegiterUrl() {
        return "regist/mobile";
    }

    //修改密码
    public static String getPasswordUrl() {
        return "login/resetPassword";
    }

    //注册验证码发送
    public static String getSmsCodeUrl() {
        return "send/code";
    }

    //修改密码验证码发送
    public static String getPasswordCodeUrl(String mobile) {
        return String.format("/sendmobcode.php?mobile=%s&type=2", mobile);
    }

    public static String getAddCommentUrl() {
        return String.format("/setaddcomment.php");
    }

    //分享的路径
    public static String getSharePlayUrl(String itemid) {
        return String.format("http://www.wuyuebuzuo.com/html/p.php?id=%s", itemid);
    }


    //消息评论
    public static String getInformation(String userid, int page) {
        return String.format("/getmycomment.php?userid=%s&page=%s", userid, page);
    }


    //消息赞
    public static String getZanList(String userId, int page) {
        return String.format("/getzan.php?userid=%s&page=%s&type=all", userId,page);
    }

    //消息收藏
    public static String getFovList() {
        return String.format("/getfov.php");
    }

    //系统消息
    public static String getSystemt() {
        return String.format("/getsysnote.php");
    }

    //歌词显示
    public static String getLyricsdisplay(String id, String userId) {
        return String.format("/getworklyrics.php?id=%s&userid=%s", id, userId);
    }


    public static String getfocusfansnum(String userid) {
        return String.format("/getfocusfansnum.php?userid=%s", userid);
    }
}
