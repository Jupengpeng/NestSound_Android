package com.xilu.wybz.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.Window;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * 手机信息
 * Created by June on 2015/10/12.
 */
public class PhoneInfoUtil {
    public static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    public static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    public static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBar(Context activity) {
        boolean hasNavigationBar = false;
        Resources rs = activity.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }

        return hasNavigationBar;
    }
    /**
     * 获取DeviceID
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
    /**
     * 获取MAC地址
     * @param context
     * @return
     */
    public static String getMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (StringUtil.isEmpty(mac)) {
            mac = "";
        }
        return mac;
    }

    /**
     * 获取UDID
     * @param context
     * @return
     */
    public static String getUDID(Context context) {
        String udid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        if (StringUtil.isEmpty(udid) || udid.equals("9774d56d682e549c")
                || udid.length() < 15) {
            SecureRandom random = new SecureRandom();
            udid = new BigInteger(64, random).toString(16);
        }

        if (StringUtil.isEmpty(udid)) {
            udid = "";
        }

        return udid;
    }
    //提交到HTTP HEAD
    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getPhoneImei(Context activity) {
        String imei = "unkonw";
        try {
            imei = ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }catch (Exception e){

        }
        return imei;
    }

    public static String getMachine(Context context) {
        return "android|301|android" + Build.VERSION.RELEASE + "|" + getPhoneModel().toUpperCase() + "|" + getPhoneImei(context) + "|" + DensityUtil.getScreenW(context) + "|" + DensityUtil.getScreenH(context) + "";
    }
    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }

    public static boolean isMIUIV6() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null && prop.getProperty(KEY_MIUI_VERSION_NAME, null).equals("V6");
        } catch (final IOException e) {
            return false;
        }
    }

    //适配MIUI 黑色状态栏 darkFlag = true开启
    public static void MiuiCj(Activity activity, boolean darkFlag) {
        Window window = activity.getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);
            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (darkFlag)
                //或状态栏透明且黑色字体
                extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
            else
                //只需要状态栏透明
                extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //清除黑色字体
            //            extraFlagField.invoke(window, 0, darkModeFlag);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    // 以下是获得版本信息的工具方法
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }
}
