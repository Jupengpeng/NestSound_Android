package com.xilu.wybz.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

/**
 * Created by Administrator on 2016/7/12.
 */
public class DeviceUtils {

    public static String MAIN_DEVICE_ID;



    private static Context context;


    public static String getDeviceId() {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE );
        String imeistring = telephonyManager.getDeviceId();
        String imsistring = telephonyManager.getSubscriberId();
        return imsistring;
    }


    public static String getSeralNumber() {

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class );
            String serialnum = (String)(   get.invoke(c, "ro.serialno", "unknown" )  );
        } catch (Exception ignored) {
        }
        return "";
    }


    public static String getMacAdrresss() {
        return "";
    }

    public static String getAndroidId() {
        String androidId = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        return "";
    }


    public static String getMetaValue(Context context, String key) {
        String metaValue = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            metaValue = appInfo.metaData.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return metaValue;
    }


}
