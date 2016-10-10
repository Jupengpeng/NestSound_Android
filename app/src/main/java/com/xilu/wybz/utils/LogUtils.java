package com.xilu.wybz.utils;

import android.text.TextUtils;

/**
 * 日志信息
 * 
 * @author June
 * @createDate 2016/8/15
 */
public class LogUtils {

    /**
     * 日志开关
     */
    private static boolean isDebug = true;


    private static final String AUTHOR ="YinChao-->";


    public static void debug(boolean status){
        isDebug = status;
    }

    public static void d(String tag, String message) {
        if (isDebug) {
            android.util.Log.d(tag,AUTHOR + message);
        }
    }

    public static void i(String tag, String message) {
        if (isDebug) {
            android.util.Log.i(tag, AUTHOR +message);
        }
    }

    /**
     * Json格式化输出
     * @param tag
     * @param message 内容
     * @param isOutputOriginalContent 是否输入原内容
     */
    public static void iJsonFormat(String tag, String message, boolean isOutputOriginalContent) {
        if (isDebug && !TextUtils.isEmpty(message)) {
            if(isOutputOriginalContent)
//                android.util.Log.i(tag, AUTHOR + message);
                android.util.Log.i(tag, AUTHOR +"\n" + JsonUtils.format(StringUtils.convertUnicode(message)));
        }
    }


    public static void w(String tag, String message) {
        if (isDebug) {
            android.util.Log.w(tag,AUTHOR + message);
        }

    }

    public static void e(String tag, String message) {
        if (isDebug) {
            android.util.Log.e(tag,AUTHOR + message);
        }
    }
}