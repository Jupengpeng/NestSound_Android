package com.xilu.wybz.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.login.LoginActivity;

import java.util.List;
import java.util.Map;

public class SystemUtils {

    public static void toAct(Context context, Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }
    //打图库
    public static void openGallery(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, MyCommon.requestCode_photo);
    }

    // 调用系统剪裁，貌似超过300宽高的返回图片对此方法无效
    public static void startPhotoZoom(Activity activity, Uri uri, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, MyCommon.requestCode_crop);
    }

    public static String getMacAddress(Context context) {
        try {

            final WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifi == null)
                return null;

            WifiInfo info = wifi.getConnectionInfo();
            String macAddress = info.getMacAddress();

            if (macAddress == null && !wifi.isWifiEnabled()) {
                wifi.setWifiEnabled(true);
                for (int i = 0; i < 10; i++) {
                    WifiInfo _info = wifi.getConnectionInfo();
                    macAddress = _info.getMacAddress();
                    if (macAddress != null)
                        break;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                wifi.setWifiEnabled(false);
            }
            return macAddress;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static boolean isWiredHeadsetOn(Context context) {
        AudioManager localAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return localAudioManager.isWiredHeadsetOn();
    }
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
        if(serviceList == null || serviceList.isEmpty())
            return false;
        for(int i = 0; i < serviceList.size(); i++) {
            if(serviceList.get(i).service.getClassName().equals(className) && TextUtils.equals(
                    serviceList.get(i).service.getPackageName(), context.getPackageName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
    public static String getTimeFormat(long allTime) {
        int miao = (int) allTime / 1000;
        int ff = miao / 60;
        int mm = miao % 60;

        String start = ff > 9 ? "" : "0";
        String end = mm > 9 ? ":" : ":0";
        return start + ff + end + mm;

    }

    public static boolean isLogin(Context context) {
        UserBean userBean = PrefsUtil.getUserInfo(context);
        if (userBean == null || userBean.userid==0) {
            toAct(context, LoginActivity.class);
            return false;
        } else {
            return true;
        }
    }
}
