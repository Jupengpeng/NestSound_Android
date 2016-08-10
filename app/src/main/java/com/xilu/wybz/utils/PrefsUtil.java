package com.xilu.wybz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xilu.wybz.bean.HotBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.KeySet;

public class PrefsUtil {

    public static final String DOMAIN = "domain";
    public static final String SETTING = "setting";
    public static final String USERINFO = "userinfo";

    static SharedPreferences preferences;

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

    public static int getUserId(Context context) {
        UserBean ub = new UserBean();
        String userInfo = getString("userInfo",context);
        if(!TextUtils.isEmpty(userInfo)){
            ub = new Gson().fromJson(userInfo, UserBean.class);
        }
        return ub.userid;
    }

    public static void saveUserInfo(Context context, UserBean ub) {
        putString("userInfo",new Gson().toJson(ub),context);
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
    public static WorksData getLocalLyrics(Context context){
        WorksData worksData = new WorksData();
        String lyrics = PrefsUtil.getString(KeySet.LOCAL_LYRICS, context);
        if (!TextUtils.isEmpty(lyrics)) {
            worksData = new Gson().fromJson(lyrics,WorksData.class);
        }
        return worksData;
    }
    //保存伴奏分类
    public static void saveHotBean(Context context, HotBean hotBean){
        putString("hotBean",new Gson().toJson(hotBean),context);
    }
    //保存伴奏分类
    public static HotBean getHotBean(Context context){
        String hotBean = getString("hotBean",context);
        if(StringUtil.isNotBlank(hotBean)){
            return new Gson().fromJson(hotBean,HotBean.class);
        }
        return null;
    }
    //保存正在播放的音乐
    public static void saveMusicData(Context context, WorksData worksData){
        putString("musicData_"+worksData.itemid,new Gson().toJson(worksData),context);
    }
    //
    //保存正在播放的音乐
    public static void clearMusicData(Context context, int id){
        putString("musicData_"+id,null,context);
        putInt("playId",0,context);
        putString("playGedanId","",context);
        putString("playFrom","",context);
        PrefsUtil.putInt("playPos", -1, context);
    }
    //
    public static WorksData getMusicData(Context context,int id){
        WorksData worksData = null;
        String data = getString("musicData_"+id,context);
        if(StringUtil.isNotBlank(data)){
            worksData = new Gson().fromJson(data,WorksData.class);
        };
        return worksData;
    }
    public static void clearData(Context context){
        preferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }

    public static void saveUserInfo(Context context, int uid, UserBean ub) {
        preferences = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        preferences.edit().putString("uid_"+uid, new Gson().toJson(ub)).commit();
    }
    public static UserBean getUserInfo(Context context, int uid) {
        preferences = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        String userData = preferences.getString("uid_"+uid,"");
        if(StringUtil.isNotBlank(userData)){
            return new Gson().fromJson(userData,UserBean.class);
        }
        return null;
    }
    public static void clearUserData(Context context) {
        preferences = context.getSharedPreferences(USERINFO, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
    }
}
