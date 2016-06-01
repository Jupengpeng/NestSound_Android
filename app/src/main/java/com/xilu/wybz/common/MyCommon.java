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
    public static String MSG_ZAN = "msg_zan";
    public static String MSG_FOV = "msg_fov";
    public static String MSG_COMMENT = "msg_comment";
    public static String NEWS = "news";
    public static String BANNER = "banner";
    public static String MORENEWS = "morenews";
    public static String MORERED = "morered";
    public static String RED = "red";
    public static String GEDAN = "gedan";
    public static String MUSICTALK = "yueshuo";
    public static String MUSICTALK_MORE = "yueshuo_more";
    public static String SEARCH = "search";
    public static String RANK_SONG = "rank_song";
    public static String TUIJIAN = "tuijian";
    public static String ZUIXIN = "zuixin";

    public static final String AGREEMENT = "http://api.wuyuebuzuo.com/html/xieyi.html";

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
        map.put(MSG_ZAN, 1);////消息 点赞
        map.put(MSG_FOV, 1);//消息 收藏
        map.put(MSG_COMMENT, 1);//消息 评论

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

        map.put("mywork", 3);//我的作品
        map.put("myfov", 3);//我的收藏
        map.put("myzan", 3);//我的收藏

        return map.get(from) == null ? 1 : map.get(from);
    }

    public static String getImageUrl(String url, int width, int height) {
        return url + "?imageView2/1/w/" + width + "/h/" + height;
    }



}
