package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by June on 16/5/3.
 * 乐说
 */
public class MusicTalk implements Serializable{
    public String itemid;
    public String pic;
    public String name;
    public String detail;
    public String url;
    public String shareurl;
    public int zannum;
    public int commentnum;
    public int sharenum;
    public int isZan;//1=已赞，0=未赞
    public int type;
}
