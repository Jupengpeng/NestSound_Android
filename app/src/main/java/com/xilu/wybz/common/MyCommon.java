package com.xilu.wybz.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zning on 2015/8/31.
 */
public class MyCommon {
    public static int requestCode_photo = 200;
    public static int requestCode_crop = 201;
    public static int PLAY_MODEL_ORDER = 1;
    public static int PLAY_MODEL_RANDOM = 2;
    public static int PLAY_MODEL_LOOP = 3;
    public static String NEWS = "news";
    public static String MORENEWS = "morenews";
    public static String MORERED = "morered";
    public static String RED = "red";
    public static String GEDAN = "gedan";
    public static String SEARCH = "search";
    public static String RANK_SONG = "rank_song";
    public static String TUIJIAN = "tuijian";
    public static String ZUIXIN = "zuixin";
    public static final String ACTION_PLAY = "com.musicplay.action.PLAY";
    public static final String ACTION_IS_PLAY = "com.musicplay.action.ISPLAY";
    public static final String ACTION_NET_PLAY = "com.musicplay.action.NET_PLAY";
    public static final String ACTION_PAUSE = "com.musicplay.action.PAUSE";
    public static final String ACTION_STOP_PROGRESS = "com.musicplay.action.STOPPROGRESS";
    public static final String ACTION_IS_PAUSE = "com.musicplay.action.ISPAUSE";
    public static final String ACTION_PROGRESS = "com.musicplay.action.PROGRESS";
    public static final String ACTION_STOP = "com.musicplay.action.STOP";
    public static final String ACTION_PREVIOUS = "com.musicplay.action.PREVIOUS";
    public static final String ACTION_NEXT = "com.musicplay.action.NEXT";
    public static final String ACTION_START = "com.musicplay.action.START";
    public static final String ACTION_EXIT = "com.musicplay.action.EXIT";
    public static final String ACTION_JUMR = "com.musicplay.action.JUMP";
    public static final String ACTION_UPDATE_PROGRESS = "com.musicplay.action.UPDATEPROGRESS";
    public static final String ACTION_JUMR_OTHER = "com.musicplay.action.JUMP_OTHER";
    public static final String ACTION_FIND = "com.musicplay.action.FIND";
    public static final String ACTION_UPDATE_ALL = "com.musicplay.action.UPDATE_ALL";
    public static final String ACTION_LISTCHANGED = "com.musicplay.action.LISTCHANGED";
    public static final String ACTION_SET_PLAYMODE = "com.musicplay.action.PALY_MODE";
    public static final String ACTION_STAR_THREAD = "com.musicplay.action.STAR_THREA";
    public static final String ACTION_UPDATELIST = "com.musicplay.action.UPDATE_LIST";
    public static final String ACTION_PHONE_STATE = "android.intent.action.PHONE_STATE";
    public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";

    public static final String AGREEMENT = "http://api.wuyuebuzuo.com/html/xieyi.html";
    public static final String ACTION_FAV = "action_fav";
    public static final String ACTION_HOME = "action_home";
    public static final String ACTION_USER = "action_user";

    public static final String ACTION_MUSIC_INIT = "action_music_init";
    public static final String ACTION_MUSIC_PP = "action_music_pp";
    public static final String ACTION_UPDATE_USER = "action_update_user";
    public static final String ACTION_UPDATE_PHOTO = "action_update_photo";
    public static final String ACTION_USER_SHOW = "action_user_show";
    public static final String ACTION_UPDATE_HOME = "action_update_home";


    public static final String TAG_RECORD_IN = "tag_record_in";
    public static final String TAG_RECORD_OUT = "tag_record_out";
    public static final String TYPE_MAKE = "make_";
    public static final String TYPE_DIY_ITEM = "diy_item_";
    public static final String TYPE_DIYTYPE = "diytype_";
    public static final String TYPE_TEMPLATE = "template_";
    public static final String TYPE_TEMPLATE1 = "template1_";
    public static final String TYPE_MAIN = "main_";
    //分享平台
    public static final String QQAppId = "1105011804";
    public static final String QQAppKey = "S1BfcSRxD8Dx5l3s";
    public static final String WECHAT_APP_ID = "wx10b95b65884a92c0";
    public static final String WECHAT_APP_SECRET = "e82e6e746efa3e92a6afa3e4be4ae7e4";
    public static final String SINA_APP_KEY = "4209676241";
    public static final String SINA_APP_SECRET = "8856a07ab99f27d94005a3fb536ad4c8";
    //默认歌词图片
    public static List<String> getLyricsPic() {
        List<String> lyricsPics = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            lyricsPics.add("lrycirs_backgroup" + ((i < 10) ? "0" : "") + i + ".png");
        }
        return lyricsPics;
    }
    //音乐的类型
    public static int getFromMusicType(String from) {// 1本地单首 2 本地多首 3服务器控制
        Map<String, Integer> map = new HashMap<>();
        map.put("mag_zan", 1);////消息 点赞
        map.put("mag_fov", 1);//消息 收藏
        map.put("msg_comment", 1);//消息 评论

        map.put("banner", 2);//banner
        map.put("ranking", 2);//排行榜
        map.put(TUIJIAN, 2);//推荐作品
        map.put(ZUIXIN, 2);//最新作品
        map.put(GEDAN, 2);//歌单
        map.put(RANK_SONG, 2);//歌单

        map.put(MORENEWS, 2);//最新
        map.put(SEARCH, 2);//搜
        map.put(NEWS, 2);//最新
        map.put(MORERED, 2);//最热
        map.put(RED, 2);//最热

        map.put("mywork", 3);//我的作品
        map.put("myfov", 3);//我的收藏
        map.put("myzan", 3);//我的收藏

        map.put("yueshuo", 3);//乐说


        return map.get(from) == null ? 1 : map.get(from);
    }

    public static String getImageUrl(String url, int width, int height) {
        return url + "?imageView2/1/w/" + width + "/h/" + height;
    }



}
