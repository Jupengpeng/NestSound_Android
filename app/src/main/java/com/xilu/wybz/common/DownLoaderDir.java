package com.xilu.wybz.common;

import android.os.Environment;

/**
 * Created by Administrator on 2016/3/7.
 */
public class DownLoaderDir {
    public static String SD = Environment.getExternalStorageDirectory() + "";
    public static String rootDir = SD + "/yinchao/";
    public static String mp3Dir = SD + "/yinchao/mp3/";
    public static String inspireMp3Dir = SD + "/yinchao/inspire_record/mp3/";
    public static String inspirePicDir = SD + "/yinchao/inspire_record/img/";
    public static String rootpicDir = SD + "/yinchao/image/";
    public static String apkDir = SD + "/yinchao/apk/";
    public static String logoDir = SD + "/yinchao/image/logo/";
    public static String savePic = SD + "/yinchao/image/save/";
    public static String coverPic = SD + "/yinchao/image/cover/";
    public static String cacheDir = SD + "/yinchao/image/cache/";
}
