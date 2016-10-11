package com.xilu.wybz.bean.message;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/2 0002.
 * 这是系统消息
 */
public class SystemMessageBean implements Serializable {

    public int id;
    public int status;//1=未读，0=已读
    public String title;
    public String pushtype;
    public String context;//消息内容
    public String url;//h5
    public long createtime;

}
