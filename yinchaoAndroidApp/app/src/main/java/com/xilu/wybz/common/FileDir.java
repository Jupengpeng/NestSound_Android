package com.xilu.wybz.common;

import android.os.Environment;

/**
 * Created by Administrator on 2016/3/7.
 */
public class FileDir {
    public static String SD = Environment.getExternalStorageDirectory() + "";
    public static String rootDir = SD + "/yinchao/";
    public static String mp3Dir = SD + "/yinchao/mp3/";
    public static String picDir = SD + "/yinchao/image/";
    public static String apkDir = SD + "/yinchao/apk/";
    public static String draftDir = SD + "/yinchao/draft/";


    public static String inspireMp3Dir = mp3Dir+"inspire/";
    public static String songMp3Dir = mp3Dir+"music/";
    public static String hotDir = mp3Dir+"hot/";

    public static String logoDir = picDir+"logo/";
    public static String coverPic = picDir+"cover/";
    public static String blurPic = picDir+"blur/";
    public static String headPic = picDir+"head/";
    public static String mineBgPic = picDir+"mineBg/";
    public static String posterPic = picDir+"poster/";
    public static String inspirePicDir = picDir+"inspire/";
}
