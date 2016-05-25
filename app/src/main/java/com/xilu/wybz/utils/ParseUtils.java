package com.xilu.wybz.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.bean.MsgBean;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.WorksData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtils {

    public static MsgBean parseMsgBean(String jsonData) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonData);
            MsgBean mb = new MsgBean();
            mb.setCode(jsonObject.getString("code"));
            mb.setMessage(jsonObject.getString("message"));
            return mb;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<TemplateBean> parseTemplateList(Context context, String response) {
        List<TemplateBean> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code==200) {
                templateList = new Gson().fromJson(new JSONObject(response).getString("data"),new TypeToken<List<TemplateBean>>(){}.getType());
            }else{
                if(jsonObject.has("message")&&!TextUtils.isEmpty(jsonObject.getString("message")))
                    showMsg(context,jsonObject.getString("message"));
            }
            return templateList;
        } catch (Exception e) {
            e.printStackTrace();
            return templateList;
        }
    }

    public static List<WorksData> getWorksData(Context context, String response) {
        List<WorksData> worksDatas = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code==200) {
                worksDatas = new Gson().fromJson(new JSONObject(response).getString("data"),new TypeToken<List<WorksData>>(){}.getType());
            }else{
                if(jsonObject.has("message")&&!TextUtils.isEmpty(jsonObject.getString("message")))
                    showMsg(context,jsonObject.getString("message"));
            }
            return worksDatas;
        } catch (Exception e) {
            e.printStackTrace();
            return worksDatas;
        }
    }


    public static MineBean parseMineBean(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String dataJson = jsonObject.getString("data");
            return (MineBean)new Gson().fromJson(dataJson,MineBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    public static String parseSponse(){
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            int code = jsonObject.getInt("code");
//            if (code==200) {
//                worksDatas = new Gson().fromJson(new JSONObject(response).getString("data"),new TypeToken<List<WorksData>>(){}.getType());
//            }else{
//                if(jsonObject.has("message")&&!TextUtils.isEmpty(jsonObject.getString("message")))
//                    showMsg(context,jsonObject.getString("message"));
//            }
//            return worksDatas;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return worksDatas;
//        }
//    }



    public static boolean checkCode(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            int code = jsonObject.getInt("code");
            return code == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showMsg(Context context, String jsonData) {
        String msg = getMsg(jsonData);
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.toast(context, msg);
        }
    }

    public static String getMsg(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String msg = jsonObject.getString("message");
            return msg;
        } catch (Exception e) {
            return "";
        }
    }

}
