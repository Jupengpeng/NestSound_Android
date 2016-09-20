package com.xilu.wybz.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by June on 2015/8/31.
 */
public class MyCommon {
    //定制歌曲
    public static final String customization = "http://html.yinchao.cn/app-customization-index.html";
    //默认头像
    public static final String defult_head = "http://pic.yinchao.cn/uploadfiles/2015/09/14/201509141121211442200881.png";
    //播放的状态
    public static final int IDLE = 0;//未开始
    public static final int INITIALIZED = 1;//初始化
    public static final int PREPARED = 2;//准备
    public static final int PREPARING = 3;//准备中
    public static final int STARTED = 4;//开始播放
    public static final int PLAYED = 5;//播放
    public static final int PAUSED = 6;//暂停
    public static final int STOPPED = 7;//停止
    public static final int COMPLETED = 8;//完成
    public static final int SUCCESSED = 9;//获取数据成功
    public static final int END = -1;//结束
    public static final int ERROR = -2;//播放出错
    public static final int FAILED = -3;//获取数据失败
    //播放模式
    public static final int PLAY_MODEL_ORDER = 0;//顺序播放
    public static final int PLAY_MODEL_RANDOM = 1;//随机播放
    public static final int PLAY_MODEL_LOOP = 2;//单曲循环
    //系统选图
    public static final int requestCode_photo = 200;//系统相册
    public static final int requestCode_crop = 201;//系统裁切
    //播放歌曲的来源 From
    public static final String MSG_ZAN = "msg_zan";
    public static final String MSG_FOV = "msg_fov";
    public static final String MATCH_NEW = "MATCH_NEW";
    public static final String MATCH_HOT = "MATCH_HOT";
    public static final String MATCH_END = "MATCH_END";
    public static final String MSG_COMMENT = "msg_comment";
    public static final String NEWS = "news";
    public static final String BANNER = "banner";
    public static final String MORENEWS = "morenews";
    public static final String MORERED = "morered";
    public static final String RED = "red";
    public static final String GEDAN = "gedan";
    public static final String MUSICTALK = "yueshuo";
    public static final String MUSICTALK_MORE = "yueshuo_more";
    public static final String SEARCH = "search";
    public static final String RANK_SONG = "rank_song";
    public static final String TUIJIAN = "tuijian";
    public static final String ZUIXIN = "zuixin";
    public static final String MYSONG = "mysong";
    public static final String MYLYRICS = "mylyrics";
    public static final String MYFAV = "myfav";
    public static final String USERSONG = "usersong";
    public static final String USERFAV = "userfav";
    public static final String MAKEHOT = "makehot";
    //七牛上传文件的空间名
    public static final String[] fixxs = new String[]{"inspire","lyrcover","muscover","music","headport"};
    //用户协议
    public static final String AGREEMENT = "http://api.yinchao.cn/html/xieyi.html";
    public static final String PROTOCOL_1 = "http://api.yinchao.cn/html/xieyi.html";
    public static final String PROTOCOL_2 = "http://api.yinchao.cn/html/xieyi.html";
    public static final String PROTOCOL_3 = "http://api.yinchao.cn/html/xieyi.html";

    public static final String TAG_RECORD_IN = "tag_record_in";
    public static final String TYPE_MAKE = "make_";
    public static final String TYPE_DIYTYPE = "diytype_";
    //分享平台
    public static final String QQAppId = "1105011804";
    public static final String QQAppKey = "S1BfcSRxD8Dx5l3s";
    public static final String WECHAT_APP_ID = "wx10b95b65884a92c0";
    public static final String WECHAT_APP_SECRET = "e82e6e746efa3e92a6afa3e4be4ae7e4";
    public static final String SINA_APP_KEY = "4209676241";
    public static final String SINA_APP_SECRET = "8856a07ab99f27d94005a3fb536ad4c8";
    public static final String WECHAT_PKG = "com.tencent.mm";
    public static final String SINA_PKG = "com.sina.weibo";
    public static final String QQ_PKG = "com.tencent.mobileqq";
    public static final String QZONE_PKG = "com.qzone";
    //推送类型
    public final static String PUSH_TYPE_COMMENT="comment"; //对我的评论
    public final static String PUSH_TYPE_ZAN="zan"; //对我的点赞
    public final static String PUSH_TYPE_FOV="fov"; //对我的收藏
    public final static String PUSH_TYPE_FOCUS="focus"; //对我的关注
    public final static String PUSH_TYPE_RECOMENDTOINDEX="recommedToIndex"; //我的作品被推荐至首页
    public final static String PUSH_TYPE_ADDTOSONGLIST="addToSonglist";  //我的作品被添加至歌单
    public final static String PUSH_TYPE_SYSTEMMSG="systemmsg";  //我的作品被添加至歌单
    public final static String PUSH_TYPE_NEWACTIVITY="newActivity"; //最新活动上线推送
    public final static String PUSH_TYPE_ACTIVITYFINISH="activityFinish";  //活动最终获奖推送
    public final static String PUSH_TYPE_COPYRIGHSUCCESS="copyrighSuccess"; //保权成功
    public final static String PUSH_TYPE_COPYRIGH="copyright"; //保权成功
    public final static String PUSH_TYPE_COPYRIGHFAIL="copyrighFail";  //保权失败

    //默认歌词图片
    public static List<String> getLyricsPic() {
        List<String> lyricsPics = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            lyricsPics.add("lrycirs_backgroup" + ((i < 10) ? "0" : "") + i + ".png");
        }
        return lyricsPics;
    }
    //音乐的类型 1本地单曲循环 2本地的列表去播放 3按照网络返回的上下首去播放（目前网络接口这边返回的不对 暂时都按照本地的去播放）
    public static int getFromMusicType(String from) {
        Map<String, Integer> map = new HashMap<>();
        map.put(MSG_ZAN, 1);////消息 点赞
        map.put(MSG_FOV, 1);//消息 收藏
        map.put(MSG_COMMENT, 1);//消息 评论
        map.put(MAKEHOT, 1);//消息 评论

        map.put(MATCH_NEW, 2);//banner
        map.put(MATCH_HOT, 2);//banner
        map.put(MATCH_END, 2);//banner
        map.put(BANNER, 2);//banner
        map.put(MUSICTALK_MORE, 2);//乐说更多
        map.put(TUIJIAN, 2);//推荐作品
        map.put(ZUIXIN, 2);//最新作品
        map.put(GEDAN, 2);//歌单
        map.put(RANK_SONG, 2);//排行榜

        map.put(MORENEWS, 2);//发现More最新
        map.put(SEARCH, 2);//搜索
        map.put(NEWS, 2);//发现最新
        map.put(MORERED, 2);//发现More最热
        map.put(RED, 2);//发现最热
        map.put(MUSICTALK, 2);//乐说

        map.put(MYSONG, 2);//我的作品
        map.put(MYFAV, 2);//我的收藏
        map.put(USERSONG, 2);//别人的收藏
        map.put(USERFAV, 2);//别人的歌曲

        return map.get(from) == null ? 1 : map.get(from);
    }
    //七牛的图片 带参数 按控件大小去生成缩缩图
    public static String getImageUrl(String url, int width, int height) {
        return url + "?imageView2/1/w/" + width + "/h/" + height;
    }

}
