package com.xilu.wybz.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.LyricBean;
import com.xilu.wybz.bean.LyricsListBean;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.bean.MsgBean;
import com.xilu.wybz.bean.MusicBean;
import com.xilu.wybz.bean.MusicDetailBean;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.UserBean;

import org.json.JSONArray;
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

    public static MineBean parseMineBean(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String code = jsonObject.getString("code");
            if (!code.equals("200")) {

                return null;
            }
            MineBean mineBean = new MineBean();
            JSONObject dataJson = jsonObject.getJSONObject("data");


            return mineBean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MusicDetailBean parseMusicDetailBean(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String code = jsonObject.getString("errorcode");
            if (!code.equals("0")) {
                return null;
            }
            JSONObject dataJson = jsonObject.getJSONObject("data");
            String infoJson = dataJson.getString("info");
            MusicDetailBean musicDetailBean = new Gson().fromJson(infoJson, MusicDetailBean.class);
            return musicDetailBean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<LyricsListBean> parseLyricsList(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String code = jsonObject.getString("errorcode");
            if (!code.equals("0")) {
                return null;
            }
            JSONObject dataJson = jsonObject.getJSONObject("data");
            JSONObject infoJson = dataJson.getJSONObject("info");

            List<LyricsListBean> lyricsList = new ArrayList<>();
            JSONObject lyricsJson = infoJson.getJSONObject("lyricslist");
            JSONArray itemArr = lyricsJson.getJSONArray("items");

            for (int i = 0, len = itemArr.length(); i < len; i++) {
                JSONObject itemJson = itemArr.getJSONObject(i);
                LyricsListBean llb = new LyricsListBean();
                llb.setItemtitle(itemJson.getString("itemtitle"));
                List<String> ls = new ArrayList<>();
                JSONArray subitemArr = itemJson.getJSONArray("lyricsitem");
                for (int j = 0, jen = subitemArr.length(); j < jen; j++) {
                    JSONObject subJson = subitemArr.getJSONObject(j);
                    ls.add(subJson.getString("lyrics"));
                }
                llb.setLyricsList(ls);

                lyricsList.add(llb);
            }

            return lyricsList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<TemplateBean> parseTemplateList(String jsonData) {
        List<TemplateBean> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String code = jsonObject.getString("errorcode");
            if (!code.equals("0")) {
                return templateList;
            }
            JSONObject dataJson = jsonObject.getJSONObject("data");
            JSONObject infoJson = dataJson.getJSONObject("info");
            JSONObject templateJson = infoJson.getJSONObject("hotlist");
            String items = templateJson.getString("items");
            templateList = new Gson().fromJson(items, new TypeToken<List<TemplateBean>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateList;
    }

    public static UserBean parseUserBean(String jsonData) {
        UserBean ub = new UserBean();
        try {
            ub = new Gson().fromJson(jsonData,UserBean.class);
        } catch (Exception e) {
        }
        return ub;
    }

    //发布过
    public static String parseMusicId(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String code = jsonObject.getString("errorcode");
            if (!code.equals("0")) {
                return null;
            }
            JSONObject dataJson = jsonObject.getJSONObject("data");
            JSONObject infoJson = dataJson.getJSONObject("info");
            return infoJson.getString("musicid");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean parseStatus(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String code = jsonObject.getString("errorcode");
            return code.equals("0");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> parseKeylistBean(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String code = jsonObject.getString("errorcode");
            if (!code.equals("0")) {
                return null;
            }
            JSONObject dataJson = jsonObject.getJSONObject("data");
            JSONObject infoJson = dataJson.getJSONObject("info");
            List<String> strList = new ArrayList<>();
            JSONObject strJson = infoJson.getJSONObject("wordlist");
            JSONArray itemArr = strJson.getJSONArray("items");
            for (int i = 0, len = itemArr.length(); i < len; i++) {
                JSONObject itemJson = itemArr.getJSONObject(i);
                strList.add(itemJson.getString("word"));
            }
            return strList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


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
