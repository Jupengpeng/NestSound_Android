package com.xilu.wybz.bean.message;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/11.
 */

public class PreserveMessageBean implements Serializable{

    public int id;
    public int did;
    public int status;//1=未读，0=已读
    public String pushtype;
    public String title;
    public String content;//消息内容
    public String orderNo;
    public long createtime;
}
