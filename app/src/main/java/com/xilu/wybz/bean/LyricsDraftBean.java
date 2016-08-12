package com.xilu.wybz.bean;

import com.google.gson.Gson;
import com.xilu.wybz.utils.DateFormatUtils;

/**
 * Created by Administrator on 2016/8/10.
 */
public class LyricsDraftBean {

    public String id = "";
    public String title = "";
    public String content = "";
    public String uid = "";
    public String workname = "";
    public String draftdesc = "";
    public String createtime = "";

    transient public String file = null;

    public String getFormatTime(){
        return DateFormatUtils.format(createtime,DateFormatUtils.PATTERN_B);
    }



    public String getJsonString(){
        return new Gson().toJson(this);
    }

    public static LyricsDraftBean getBeanByString(String json){
        return new Gson().fromJson(json,LyricsDraftBean.class);
    }
}
