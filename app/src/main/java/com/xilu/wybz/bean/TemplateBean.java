package com.xilu.wybz.bean;

import java.io.Serializable;
/**
 * Created by Zning on 2015/9/7.
 * 热门录制曲目 其中的list
 */
public class TemplateBean implements Serializable{
    public String id;//模板的唯一标识ID
    public String aid;//活动ID
    public int usenum;//使用次数
    public String pic;//模板的封面图片
    public String title;//模板的名字
    public String mp3;//模板的在线播放地址
    public int mp3times;//歌曲的总长度，单位为秒
    public String author;//作者
    public boolean isPlay;//播放状态
}
