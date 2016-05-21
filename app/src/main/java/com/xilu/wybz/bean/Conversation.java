package com.xilu.wybz.bean;

import com.litesuits.orm.db.annotation.Table;
import java.util.List;

@Table("Conversation") //指定表名，可任意写
public class Conversation extends BaseModel {

    public static final String MESSAGEID = "messageId";
    public static final String ISVISIBILITY = "isVisibility";
    //这里又是一个实体类的LIST，可以理解为另外一张表。这么写，就代表 Conversation表和User表是 一对多关系
    private List<String> user;
    private String nickName;
    private String headImgUrl;
    private String content;
    private String sendDate;
    private int msgType;
    private int subType;
    private int messageId; // 用于话题聊天的id，和私聊的 Userid
    private int senderUserId;
    private Boolean isVisibility = true;
    private int messageType;
    private int messageNum;
    //  以下省略一堆 get和set方法
}