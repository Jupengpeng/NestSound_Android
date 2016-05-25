package com.xilu.wybz.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.LyricBean;
import com.xilu.wybz.bean.LyricsListBean;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.bean.MsgBean;
import com.xilu.wybz.bean.MusicBean;
import com.xilu.wybz.bean.MusicDetailBean;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;

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

    public static List<TemplateBean> parseTemplateList(Context context, String response) {
        List<TemplateBean> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                templateList = new Gson().fromJson(new JSONObject(response).getString("data"), new TypeToken<List<TemplateBean>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
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
            if (code == 200) {
                worksDatas = new Gson().fromJson(new JSONObject(response).getString("data"), new TypeToken<List<WorksData>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
            return worksDatas;
        } catch (Exception e) {
            e.printStackTrace();
            return worksDatas;
        }
    }

    public static List<MusicTalk> getMusicTalksData(Context context, String response) {
        List<MusicTalk> musicTalks = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                musicTalks = new Gson().fromJson(new JSONObject(response).getString("data"), new TypeToken<List<MusicTalk>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return musicTalks;
    }
    public static List<SongAlbum> getSongAlbumsData(Context context, String response) {
        List<SongAlbum> songAlbums = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                songAlbums = new Gson().fromJson(new JSONObject(response).getString("data"), new TypeToken<List<SongAlbum>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songAlbums;
    }
    public static UserBean getUserBean(Context context, String response) {
        UserBean userBean = new UserBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                userBean = new Gson().fromJson(response, UserBean.class);
            }else{
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userBean;
    }
    public static DataBean getDataBean(String response) {
        DataBean dataBean = new DataBean();
        try {
            dataBean = new Gson().fromJson(response, DataBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
    }

    public static MineBean parseMineBean(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            String dataJson = jsonObject.getString("data");
            return new Gson().fromJson(dataJson, MineBean.class);
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

    public static void showMsg(Context context, String msg) {
        ToastUtils.toast(context, msg);
    }

}
