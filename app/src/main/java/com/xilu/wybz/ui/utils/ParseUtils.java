package com.xilu.wybz.ui.utils;

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
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.utils.ToastUtils;

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
            String code = jsonObject.getString("errorcode");
            if (!code.equals("0")) {
                return null;
            }
            MineBean mineBean = new MineBean();
            JSONObject dataJson = jsonObject.getJSONObject("data");
            JSONObject infoJson = dataJson.getJSONObject("info");

            mineBean.setUserid(infoJson.getString("userid"));
            mineBean.setName(infoJson.getString("name"));
            mineBean.setHeadurl(infoJson.getString("headurl"));
            mineBean.setInfo(infoJson.getString("desc"));

            JSONObject workListJson = infoJson.getJSONObject("worklist");
            String workListArr = workListJson.getString("items");
            List<MusicBean> workMusicList = new Gson().fromJson(workListArr, new TypeToken<List<MusicBean>>() {
            }.getType());
            mineBean.setWorkList(workMusicList);

            JSONObject fovMusicJson = infoJson.getJSONObject("fovlist");
            String fovMusicArr = fovMusicJson.getString("items");
            List<MusicBean> fovMusicList = new Gson().fromJson(fovMusicArr, new TypeToken<List<MusicBean>>() {
            }.getType());
            mineBean.setFovList(fovMusicList);

            JSONObject lyricsMusicJson = infoJson.getJSONObject("lyricslist");
            String lyricsMusicArr = lyricsMusicJson.getString("items");
            List<LyricBean> lyricsMusicList = new Gson().fromJson(lyricsMusicArr, new TypeToken<List<LyricBean>>() {
            }.getType());

            mineBean.setLyricList(lyricsMusicList);

            return mineBean;
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
