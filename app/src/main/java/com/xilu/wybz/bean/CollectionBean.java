package com.xilu.wybz.bean;

import android.text.TextUtils;

import com.xilu.wybz.common.YinChaoConfig;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/2/29 0029.
 * 这个是消息 收藏
 */
public class CollectionBean implements Serializable {
    public int id;
    public int target_uid; //发布作品的用户id
    public int itemid;//收藏的作品id
    public int user_id;//收藏者的ID
    public String author;//作者名称
    public String title;//作品标题
    public String headerurl;//收藏者头像
    public String pic;//作品封面
    public int type;//1=歌词，2=歌曲
    public String nickname;//收藏者用户名称
    public long intabletime;//收藏时间

}
