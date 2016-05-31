package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/2 0002.
 * 这是系统消息
 */
public class SystemBean implements Serializable {
    public int id;
    public String title;
    public String pic;
    public int type;//1=纯文本，2=图片加超链接
    public int status;//1=未读，0=已读
    public String context;//消息内容
    public String url;//h5
    public long createdate;

}
