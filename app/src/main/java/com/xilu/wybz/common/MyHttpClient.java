package com.xilu.wybz.common;

/**
 * Created by June on 2016/5/4.
 */
public class MyHttpClient {


    public final static String RELEASE_ROOT_URL = "http://api.yinchao.cn";
    //    public final static String RELEASE_ROOT_URL = "http:192.168.0.99:8080";
    public final static String TEST_ROOT_URL = "http://test.yinchao.cn";
    public final static String TEMP_ROOT_URL = "http://test.yinchao.cn";


    public static String ROOT_URL = RELEASE_ROOT_URL;

    public static String PRE_ROOT = ROOT_URL;
    public static String BASE_URL = ROOT_URL;

    public static String QINIU_AUDIO_URL = "http://audio.yinchao.cn";
    public static String QINIU_URL = "http://pic.yinchao.cn";


    public static String PROTOCOL_1 = "";
    public static String PROTOCOL_2 = "";
    public static String PROTOCOL_3 = "";


    public static String getQnToken() {
        return "/java/common/uploadcallback/qiniutoken";
    }

    public static String getTokenCheck() {
        return "/java/common/login/tokenCheck";
    }

    //伴奏分类列表
    public static String getHotUrl() {
        return "/java/common/accompaniment/list/v22";
    }

    //伴奏默认列表
    public static String getDefaultHotListUrl() {
        return "/java/common/accompaniment/defaultlist/v22";
    }

    //伴奏详情
    public static String getHotDetailUrl() {
        return "/java/common/hot/detail";
    }

    //首页数据
    public static String getHomeUrl() {
        return "/java/common/index/app/v22";
    }

    //乐说列表
    public static String getMusicTalkUrl() {
        return "/java/common/yueshuo/list/v22";
    }

    //歌曲详情
    public static String getMusicWorkUrl() {
        return "/java/common/music/work";
    }

    //歌词详情
    public static String getLyricsdisplay() {
        return "/java/common/lyrics/detail";
    }

    //反馈
    public static String getFeed() {
        return "/java/feedback/save";
    }

    //举报
    public static String getUserReport() {
        return "/java/report/save";
    }

    //修改用户资料
    public static String getModifyUserInfo() {
        return "/java/userCenter/updateuserinfo";
    }

    //歌单列表
    public static String getGleeListUrl() {
        return "/java/common/glee/list";
    }

    //歌单详情
    public static String getGleeDetailUrl() {
        return "/java/common/glee/detail";
    }

    //发现里面歌曲
    public static String getFindSongList() {
        return "/java/common/find/music/v22";
    }

    //发现里面更多歌曲
    public static String getFindMoreSongList() {
        return "/java/common/find/moremusic/v22";
    }

    //发现里面歌词
    public static String getFindLyricsList() {
        return "/java/common/find/lyrics/v22";
    }

    //发现里面更多歌词
    public static String getFindMoreLyricsList() {
        return "/java/common/find/morelyrics/v22";
    }

    //发现里面歌曲
    public static String getFindActivityList() {
        return "/java/common/find/activelist";
    }

    //发现里面排行榜
    public static String getRankingList() {
        return "/java/common/find/rankList";
    }

    //登录
    public static String getLoginUrl() {
        return "/java/common/login/mobile";
    }

    //退出登录
    public static String getLoginOut() {
        return "/java/common/login/logout";
    }

    //注册
    public static String getRegiterUrl() {
        return "/java/common/regist/mobile";
    }

    //修改密码
    public static String getPasswordUrl() {
        return "userCenter/updateuserpass";
    }

    //重置密码
    public static String getResetPwdUrl() {
        return "/java/common/login/resetPassword";
    }

    //注册验证码发送
    public static String getSmsCodeUrl() {
        return "/java/common/send/code";
    }

    //用户歌词列表
    public static String getUserLyricsListUrl() {
        return "/java/common/lyrics/myLyrics";
    }

    //用户歌曲列表
    public static String getUserMusicListUrl() {
        return "/java/common/music/worklist";
    }

    //点赞接口
    public static String getUpvoteUrl() {
        return "/java/workzan/optZan/v22";
    }


    //收藏接口
    public static String getWorkFovUrl() {
        return "/java/workfov/optfov/v22";
    }

    /**
     * 消息列表接口.
     *
     * @return
     */
    //消息收藏列表
    public static String getMsgFovList() {
        return "/java/message/fovMyCenterList/v22";
    }

    //消息评论列表
    public static String getMsgCommentList() {
        return "/java/message/commentCenterList/v22";
    }

    //消息点赞列表
    public static String getMsgZanList() {
        return "/java/message/zanCenterList/v22";
    }

    //保全消息列表
    public static String getMsgPreserveList() {
        return "/java/message/copyrightList/v22";
    }

    //消息系统列表
    public static String getMsgSystemList() {
        return "/java/message/sysNoteList/v22";
    }


    /**
     * @return
     */
    //发布歌词
    public static String getSaveLyricsUrl() {
        return "/java/lyricsopt/optLyrics";
    }

    //发布歌词(活动)
    public static String getSaveActivityLyricsUrl() {
        return "/java/lyricsopt/activity";
    }

    //保存灵感记录
    public static String getSaveInspireUrl() {
        return "/java/inspire/save";
    }

    //修改灵感记录
    public static String getUpdateInspireUrl() {
        return "/java/inspire/update";
    }

    //个人主页
    public static String getUserCenter() {
        return "/java/userCenter/index/v23";
    }

    //他人主页
    public static String getOtherCenter() {
        return "/java/common/otherCenter/index/v23";
    }

    //个人主页
    public static String getUserCenterList() {
        return "/java/userCenter/listdetails/v23";
    }

    //他人主页
    public static String getOtherCenterList() {
        return "/java/common/otherCenter/listdetails/v23";
    }

    //歌词
    public static String getLyricatsUrl() {
        return "/java/lyricsopt/getLyriCats";
    }

    //评论列表
    public static String getCommentListUrl() {
        return "/java/common/comment/list/v22";
    }

    //评论接口
    public static String getSaveCommentUrl() {
        return "/java/commentopt/save/v22";
    }

    //删除评论
    public static String getDelCommentUrl() {
        return "/java/commentopt/delete/v22";
    }

    //更新IP
    public static String getBootPic() {
        return "/java/common/boot/pic";
    }

    //粉丝列表
    public static String getFansList() {
        return "/java/fanfocus/fanslist/v22";
    }

    //他人粉丝列表
    public static String getOtherFansList() {
        return "/java/fanfocus/otherfanslist";
    }

    //搜索接口
    public static String getSearchList() {
        return "/java/common/search/search";
    }

    //搜索接口
    public static String getFanFocusList() {
        return "/java/fanfocus/optFans/v22";
    }

    //上传歌曲
    public static String getuploadmp3Url() {
        return BASE_URL + "/java/common/uploadcallback/uploadmp3";
    }

    //合成歌曲
    public static String getTuningSongUrl() {
        return "/java/musiopt/tuningMusic";
    }

    //发布歌曲
    public static String getSaveSongUrl() {
        return "/java/musiopt/optMusic";
    }


    //删除我的作品
    public static String getDeleteWorksUrl() {
        return "/java/userCenter/deleteWork";
    }

    //更新歌曲 歌词公开的状态
    public static String getUpdateMusicUrl() {
        return "/java/musiopt/updateMusic";
    }

    public static String getUpdateLyricsUrl() {
        return "/java/lyricsopt/updateLyrics";
    }

    //伴奏分类
    public static String getHotCatalogUrl() {
        return "/java/common/accompanimentcategory/list/v22";
    }

    //音乐人列表
    public static String getMusicianListUrl() {
        return "/java/common/musician/list";
    }

    //音乐人详情
    public static String getMusicianDetailUrl() {
        return "/java/common/musician/detail";
    }

    public static String getLyricstemplateList() {
        return "/java/lyricstemplate/list";
    }

    public static String getDraftList() {
        return "/java/draft/list";
    }

    public static String getDraftSave() {
        return "/java/draft/save";
    }

    public static String getDraftDelete() {
        return "/java/draft/delete";
    }

    //专题活动
    public static String getMatchInfo() {
        return "/java/common/activity/actdetail";
    }

    //专题参与人列表
    public static String getJoinUserList() {
        return "/java/common/activity/userlist";
    }

    //专题活动作品列表
    public static String getMatchWorkList() {
        return "/java/common/activity/worklist";
    }

    //专题活动作品列表
    public static String getOverMatchWorkList() {
        return "/java/common/activity/overlist";
    }

    //专题活动作品列表
    public static String getAttendUrl() {
        return "/java/activity/attend";
    }

    public static String getUpdateuserbgUrl() {
        return "/java/userCenter/updateuserbg";
    }

    //未读消息
    public static String getMsgNumUrl() {
        return "/java/message/center/v22";
    }

    //乐说详情
    public static String getMusicTalkDetail() {
        return "/java/yueshuo/detail/v22";
    }
    //乐说点赞

    public static String getMusicTalkZan() {
        return "/java/yueshuo/zan/save/v22";
    }

    //乐说分享
    public static String getMusicTalkShareCount() {
        return "/java/common/yueshuo/sharecount/v22";
    }

    /**
     * 保全相关接口
     */
    //查询用户保全的作品列表
    public static String getpreservationList() {
        return "/copyright/preserve/getPreserveListByid";
    }

    //未保全列表
    public static String getProductAllList() {
        return "/copyright/preserve/getProductList";
    }


    //保全订单信息
    public static String getPreserveOrderDetail() {
        return "/copyright/preserve/getOrderDetail";
    }

    public static String getPreservePreserveInfo() {
        return "/copyright/preserve/getPreserveInfo";
    }

    public static String getPersonInfo() {
        return "/copyright/preserve/getBaoquanrenByid";
    }

    public static String getSavePersonInfo() {
        return "/copyright/preserve/addBaoquanren";
    }

    /**
     * 支付相关接口
     *
     * @return
     */
    public static String getPaypalOrder() {
        return "/paypal/order/save/v22";
    }

    public static String getPaypalPay() {
        return "/paypal/pay/save/v22";
    }

    public static String getPaypalStatus() {
        return "/paypal/pay/status/v22";
    }


//2.3版本接口
    //合作列表
    public static String getDemandlist() {
        return "/cooperate/index/demandlist/v23";
    }

    //发布需求
    public static String getDemand() {
        return "/cooperate/demand/public/v23";
    }

    //合作-发布需求-歌词列表
    public static String getLyricslist() {
        return "/cooperate/demand/lyricslist/v23";
    }

    //合作-合作(按钮)
    public static String getPreinfo() {
        return "/cooperate/demand/preinfo/v23";
    }

    //合作-合作详情页
    public static String getCooperaDetail() {
        return "/cooperate/demand/detail/v23";
    }

    //我的列表
    public static String getMylist() {
        return "/cooperate/index/mylist/v23";
    }

    //我的-删除合作
    public static String getDelete() {
        return "/cooperate/demand/delete/v23";
    }
    //我的-采纳作品
    public static String getAccept(){
        return "/cooperate/demand/accept/v23";
    }
    //收藏列表
    public static String getFovlist(){
        return "/cooperate/index/fovlist/v23";
    }
    //收藏-收藏操作
    public static String getFov (){
        return "/cooperate/demand/fov/v23";
    }
    //留言列表详情
    public static String getLeavelist (){
        return "/cooperate/comment/leavelist/v23";
    }
    //发表留言
    public static String getPublishComment (){
        return "/cooperate/comment/save/v23";
    }
    //邀请-邀请列表
    public static String getInvitelist (){
        return "/cooperate/invite/userlist/v23";
    }
    //邀请-发起邀请
    public static String getPublishInvite(){
        return "/cooperate/invite/save/v23";
    }
}

