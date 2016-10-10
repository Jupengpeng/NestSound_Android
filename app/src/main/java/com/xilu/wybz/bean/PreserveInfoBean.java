package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/8.
 */

public class PreserveInfoBean implements Serializable{

/*
    "message":"",
    "data":
    {
        productInfo:{
            "id":1,
            "title":"name",
            "lyricAuthor":"sd"
            "songAuthor":"sd",
            "accompaniment":"yinchao",//伴奏
            "createTime"："14265756856"
            "image":"http://pic.yinchao.cn/214124.png"
            "type":1

            "preserveDate":
            "preserveID"://保全号
        }
        personInfo:{
            "cUserName":"name",
            "cCardId":"1244",
            "c_sort_id":1 //1是歌曲 2 歌词 3词曲  按照123不同改变身份
            "cPhone":"123542657"
        }

        id:
        certUrl: //证书地址
        statu: //保全状态 1保全成功 0 保全失败
    }
    "code":200*/

    public PersonInfo personInfo;
    public ProductInfo productInfo;

    public int id;
    public String certUrl;
    public int statu;


}
