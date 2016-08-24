package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/28 0028.
 * 赞
 */
public class ZambiaBean implements Serializable {

    public String id;
    public int target_uid; //发布作品的用户id
    public String itemid;//收藏的作品id
    public String author;//作者名称
    public String title;//作品标题
    public String headerurl;//收藏者头像
    public String pic;//作品封面
    public int type;//1=歌词，2=歌曲
    public String nickname;//收藏者用户名称
    public long add_time;
}
