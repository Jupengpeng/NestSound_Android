package com.xilu.wybz.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by JW on 2015/10/12.
 */
public class PhoneDeviceUtil {


    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getPhoneImei(Context activity) {
        String imei = "";
        if(PermissionUtils.checkReadPhoneStatePermission(activity)){
            imei = ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        } else{
            imei = "unkonw";
        }
        return imei;
    }

    public static String getMachine(Context context) {
        return "android|301|android" + Build.VERSION.RELEASE + "|" + getPhoneModel().toUpperCase() + "|" + getPhoneImei(context) + "|" + DensityUtil.getScreenW(context) + "|" + DensityUtil.getScreenH(context) + "";
    }
}
