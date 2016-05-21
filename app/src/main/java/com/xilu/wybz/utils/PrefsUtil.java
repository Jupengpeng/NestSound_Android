package com.xilu.wybz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.UserInfo;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;

import java.util.ArrayList;
import java.util.List;

public class PrefsUtil {

    public static final String SETTING = "setting";
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;

    public static SharedPreferences getSpf(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static void putBoolean(String key, boolean value, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static void putInt(String key, int value, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return preferences.getInt(key, 0);
    }

    public static void putFloat(String key, Float value, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        preferences.edit().putFloat(key, value).commit();
    }

    public static Float getFloat(String key, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return preferences.getFloat(key, 0f);
    }

    public static void putLong(String key, Long value, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        preferences.edit().putLong(key, value).commit();
    }

    public static Long getLong(String key, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return preferences.getLong(key, 0L);
    }

    public static void putString(String key, String value, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).commit();
    }

    public static String getString(String key, Context context) {
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static UserBean getUserInfo(Context context) {
        UserBean ub = new UserBean();
        String userInfo = getString("userInfo",context);
        if(!TextUtils.isEmpty(userInfo)){
            ub = new Gson().fromJson(userInfo, UserBean.class);
        }
        return ub;
    }

    public static String getUserId(Context context) {
        return getUserInfo(context).uid;
    }

    public static void saveUserInfo(Context context, UserBean ub) {
        putString("userInfo",new Gson().toJson(ub),context);
    }

    public static boolean getFrist(Context context) {
        SharedPreferences spf = getSpf(context, "frist");
        return spf.getBoolean("status", true);
    }

    public static void saveFrist(Context context) {
        SharedPreferences spf = getSpf(context, "frist");
        SharedPreferences.Editor editor = spf.edit();
        editor.putBoolean("status", false);
        editor.commit();
    }

    //获取本地们关键词
    public static List<String> getWordkeys(Context context){
        List<String> keywords = new ArrayList<>();
        String keys = PrefsUtil.getString("keywords", context);
        if (!TextUtils.isEmpty(keys)) {
            keywords = new Gson().fromJson(keys,new TypeToken<List<String>>(){}.getType());
        }
        return keywords;
    }
    //获取歌词
    public static WorksData getLyrics(String id, Context context){
        WorksData lyricsdisplayBean = new WorksData();
        String lyrics = PrefsUtil.getString("lyrics"+id, context);
        if (!TextUtils.isEmpty(lyrics)) {
            lyricsdisplayBean = new Gson().fromJson(lyrics,WorksData.class);
        }
        return lyricsdisplayBean;
    }
}
