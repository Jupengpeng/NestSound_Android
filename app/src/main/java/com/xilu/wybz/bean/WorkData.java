package com.xilu.wybz.bean;

import com.litesuits.orm.db.annotation.Table;


import java.util.List;

@Table("workdata")
public class WorkData extends BaseModel{
    public String itemid;//歌曲id
    public String pic;//歌曲封面
    public String title;//标题
    public String author;//作者
    public String headurl;//用户头像
    public int looknum;//浏览量
    public int isOpen;//是否公开
    public int fovnum;//收藏量
    public int zannum;//点赞量
    public int sampleid;//模板Id
    public String playurl;//mp3地址
    public int mp3times;//mp3时长
    public int status;//1歌曲 2歌词
    public String lyrics;//歌词
    public String detail;//歌词详情 灵感记录的内容
    public String prev;//上一首
    public String next;//下一首
    public String come;
    public int iscollect;//1=已收藏，0=未收藏
    public int isZan;//1=已赞，0=未赞
    public int uid;//作者id
    public int hotid;//伴奏id
    public String hotAutor;//伴奏作者
    public String hotTitle;//伴奏标题
    public String createTime;//创建时间
    public int recordtimes;//录音时长
    public String recordPath;//录歌时人声或者灵感记录的音频
    public List<String> picList;//灵感记录的图片集合
}
