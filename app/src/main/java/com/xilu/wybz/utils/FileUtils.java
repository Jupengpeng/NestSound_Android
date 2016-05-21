package com.xilu.wybz.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Zning on 2015/8/26.
 */
public class FileUtils {

    public static final String ROOTPATH = "/yinchao";
    public static final String APKPATH = getSDPath()+"/yinchao/apk/";
    public static final String MUSICDOWNLOADPATH = "/download/music";
    public static final String MUSICCACHEPATH = "/cache/music";
    public static final String RECORDTEMPPATH = "/cache/record/temp";
    public static final String RECORDRAWPATH = "/cache/record/raw";
    public static final String IMGCACHEPATH = "/cache/img";
    public static final String LOCALplayurl = "/local/music";
    public static final String LOCALIMGPATH = "/local/img";
    public static final String USERIMGPATH = "/user/img";
    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public final static int AUDIO_SAMPLE_RATE = 44100;  //44.1KHz,普遍使用的频率

    public static void delAllFile() {
        FileUtils.delFile(new File(getRootPath() + USERIMGPATH));
        FileUtils.delFile(new File(getRootPath() + APKPATH));
        FileUtils.delFile(new File(getRootPath() + LOCALplayurl));
        FileUtils.delFile(new File(getRootPath() + RECORDRAWPATH));
        FileUtils.delFile(new File(getRootPath() + RECORDTEMPPATH));
        FileUtils.delFile(new File(getRootPath() + LOCALIMGPATH));
        FileUtils.delFile(new File(getRootPath() + IMGCACHEPATH));
        FileUtils.delFile(new File(getRootPath() + MUSICCACHEPATH));
    }

    public static String getSDPath() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return null;
        }
    }

    public static String getRootPath() {
        return getSDPath() + ROOTPATH;
    }

    public static String getMusicCachePath(String cacheName) {
        return makeFileFold(getRootPath() + MUSICCACHEPATH) + "/" + cacheName;
    }

    public static String getMusicCachePath(String foldName, String fileName) {
        return makeFileFold(getRootPath() + MUSICCACHEPATH + "/" + foldName) + "/" + fileName;
    }

    public static String getMusicDownloadPath(String name) {
        return makeFileFold(getRootPath() + MUSICDOWNLOADPATH) + "/" + name;
    }

    public static String getTempDiyPath() {
        return makeFileFold(getRootPath() + RECORDTEMPPATH) + "/temp_diy";
    }

    public static String getTempRecordPath() {
        return makeFileFold(getRootPath() + RECORDTEMPPATH) + "/temp_record";
    }

    public static String getRawRecordPath() {
        return makeFileFold(getRootPath() + RECORDTEMPPATH) + "/raw_record";
    }

    public static String getRawRecordPath(String foldName, String fileName) {
        return makeFileFold(getRootPath() + RECORDRAWPATH + "/" + foldName) + "/" + fileName;
    }

    public static String getRawRecordFoldPath(String foldName) {
        return makeFileFold(getRootPath() + RECORDRAWPATH + "/" + foldName);
    }

    public static String getTempMusicSharePath() {
        return makeFileFold(getRootPath() + RECORDTEMPPATH) + "/temp_music";
    }

    public static String getTempImgSharePath() {
        return makeFileFold(getRootPath() + RECORDTEMPPATH) + "/temp_img";
    }

    public static String getTempMusicImgPath() {
        return makeFileFold(getRootPath() + IMGCACHEPATH) + "/temp_music";
    }

    public static String getLocalplayurl(String id) {
        return makeFileFold(getRootPath() + LOCALplayurl) + "/" + id + ".mp3";
    }

    public static String getLocalImgPath(String id) {
        return makeFileFold(getRootPath() + LOCALIMGPATH) + "/" + id + ".png";
    }

    public static String getUserImgPath() {
        return makeFileFold(getRootPath() + USERIMGPATH) + "/user.png";
    }

    static String makeFileFold(String file) {
        File filePath = new File(file);
        if (filePath.isFile()) {
            filePath.delete();
        }
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        return filePath.toString();
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static String getStringFromFile(File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }
    public static boolean saveFile(String filePath, InputStream inputStream) {
        return saveFile(filePath, inputStream, false);
    }

    public static boolean saveFile(String filePath, InputStream inputStream, boolean restart) {
        try {
            File file = new File(filePath);
            if (restart && file.exists()) file.deleteOnExit();
            if (file.isFile() && file.length() > 0) {
                return true;
            }
            if (!new File(getRootPath() + MUSICCACHEPATH).exists()) {
                new File(getRootPath() + MUSICCACHEPATH).mkdirs();
            }
            FileOutputStream outputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[1024 * 4];
            int len = -1;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);  //在这里使用另一个重载，防止流写入的问题.
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (Exception e) {
            Log.e("Exception",e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(String oldPath, String newPath) {
        try {
            File oldfile = new File(oldPath);
            new File(newPath).deleteOnExit();
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[4 * 1024];
                int byteread = 0;
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean saveSqBmp(String savePath, Bitmap bitmap) {
        try {
            File picFile = new File(savePath);
            if (picFile.isFile() && picFile.length() > 0) {
                return true;
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(savePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveBmp(String savePath, Bitmap bitmap, boolean restart) {
        try {
            if (!restart) {
                File picFile = new File(savePath);
                if (picFile.isFile() && picFile.length() > 0) {
                    return true;
                }
            }
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(savePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveBmp(String savePath, Bitmap bitmap) {
        return saveBmp(savePath, bitmap, false);
    }

    public static boolean uniteAMRFile(File[] partsPaths, String unitedFilePath) {
        try {
            File unitedFile = new File(unitedFilePath);
            FileOutputStream fos = new FileOutputStream(unitedFile);
            RandomAccessFile ra = null;
            for (int i = 0; i < partsPaths.length; i++) {
                ra = new RandomAccessFile(partsPaths[i], "r");
                if (i != 0) {
                    ra.seek(6);
                }
                byte[] buffer = new byte[1024 * 8];
                int len = 0;
                while ((len = ra.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
            ra.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean uniteAMRFile(String[] partsPaths, String unitedFilePath) {
        try {
            File unitedFile = new File(unitedFilePath);
            unitedFile.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(unitedFile);
            RandomAccessFile ra = null;
            for (int i = 0; i < partsPaths.length; i++) {
                ra = new RandomAccessFile(partsPaths[i], "r");
                if (i != 0) {
                    ra.seek(6);
                }
                byte[] buffer = new byte[1024 * 8];
                int len = 0;
                while ((len = ra.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
            ra.close();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void delFile(File file) {
        if (file.exists()) {                    //判断文件是否存在
            if (file.isFile()) {
                file.delete();                       //delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) {
                File files[] = file.listFiles(); //声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) {         //遍历目录下所有的文件
                    delFile(files[i].getAbsoluteFile());
                    //把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }
    /**
     * 判断是否有外部存储设备sdcard
     *
     * @return true | false
     */
    public static boolean isSdcardExit() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static int getFileSize(String urlString) throws Exception {
        int lenght = 0;
        URL mUrl = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
        conn.setConnectTimeout(5 * 1000);
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept-Encoding", "identity");
        conn.setRequestProperty("Referer", urlString);
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.connect();

        int responseCode = conn.getResponseCode();
        // 判断请求是否成功处理
        if (responseCode == 200) {
            lenght = conn.getContentLength();
        }
        return lenght;
    }
}
