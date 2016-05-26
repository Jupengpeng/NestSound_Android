package com.xilu.wybz.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.Lyricat;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.TokenBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ParseUtils {

    public static List<TemplateBean> parseTemplateList(Context context, String response) {
        List<TemplateBean> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                templateList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<TemplateBean>>() {
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
    public static List<SongAlbum> getSongAlbumsData(Context context, String response) {
        List<SongAlbum> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                templateList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<SongAlbum>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateList;
    }

    public static List<Lyricat> getLyricatsData(Context context, String response) {
        List<Lyricat> templateList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                templateList = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<Lyricat>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return templateList;
    }

    public static List<WorksData> getWorksData(Context context, String response) {
        List<WorksData> worksDatas = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                worksDatas = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<WorksData>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worksDatas;
    }
    public static List<MusicTalk> getMusicTalksData(Context context, String response) {
        List<MusicTalk> worksDatas = null;
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                worksDatas = new Gson().fromJson(jsonObject.getString("data"), new TypeToken<List<MusicTalk>>() {
                }.getType());
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return worksDatas;
    }


    public static DataBean getDataBean(Context context,String response) {
        DataBean dataBean = new DataBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                dataBean = new Gson().fromJson(jsonObject.getString("data"),DataBean.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
    }
    public static UserBean getUserBean(Context context, String response) {
        UserBean dataBean = new UserBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                dataBean = new Gson().fromJson(jsonObject.getString("data"),UserBean.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
    }
    public static TokenBean getTokenBean(Context context, String response) {
        TokenBean dataBean = new TokenBean();
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            if (code == 200) {
                dataBean = new Gson().fromJson(jsonObject.getString("data"),TokenBean.class);
            } else {
                showMsg(context, jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataBean;
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
