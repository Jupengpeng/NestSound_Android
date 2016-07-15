package com.xilu.wybz.utils;

import android.content.Context;
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
        /*
         * getDeviceId() function Returns the unique device ID.
         * for example,the IMEI for GSM and the MEID or ESN for CDMA phones.
         */
        String imeistring = telephonyManager.getDeviceId();
        /*
        * getSubscriberId() function Returns the unique subscriber ID,
        * for example, the IMSI for a GSM phone.
        */
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


}
