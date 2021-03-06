package com.xilu.wybz.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.xilu.wybz.common.FileDir;

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
import java.text.DecimalFormat;

/**
 * Created by June on 2015/8/26.
 */
public class FileUtils {

    public static final String ROOTPATH = "/yinchao";
    public static final String MUSICCACHEPATH = "/cache/music";
    public static final String RECORDTEMPPATH = "/cache/record/temp";
    public static final String RECORDRAWPATH = "/cache/record/raw";
    public static final String LOCALplayurl = "/local/music";
    public static final String LOCAL_RECORD = "/local/record";
    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath
     *            文件路径
     * @param sizeType
     *            获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }
    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    public static long getFileSizes(File f) {
        long size = 0;
        try {
            File flist[] = f.listFiles();
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSizes(flist[i]);
                } else {
                    size = size + getFileSize(flist[i]);
                }
            }
        }catch (Exception e){
        }
        return size;
    }
    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    public static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }
    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file){
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
            }
        }catch (Exception e){
        }
        return size;
    }

    public static void delAllFile() {
        FileUtils.delFile(new File(getRootPath() + LOCALplayurl));
        FileUtils.delFile(new File(getRootPath() + RECORDRAWPATH));
        FileUtils.delFile(new File(getRootPath() + RECORDTEMPPATH));
        FileUtils.delFile(new File(getRootPath() + MUSICCACHEPATH));
        FileUtils.delFile(new File(getRootPath() + LOCAL_RECORD));

        FileUtils.delFile(new File(FileDir.apkDir));
        FileUtils.delFile(new File(FileDir.picDir));
        FileUtils.delFile(new File(FileDir.mp3Dir));
        Fresco.getImagePipeline().clearDiskCaches();
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
    public static boolean fileExists(String path) {
        return new File(path).exists();
    }

    public static String getMusicCachePath(String cacheName) {
        return makeFileFold(getRootPath() + MUSICCACHEPATH) + "/" + cacheName;
    }

    public static String getTempRecordPath() {
        return makeFileFold(getRootPath() + RECORDTEMPPATH) + "/temp_record.mp3";
    }
    public static String getLocalRecordPath(String fileName) {
        return makeFileFold(getRootPath() + LOCAL_RECORD) + "/"+ fileName + ".mp3";
    }

    public static String getLocalplayurl(String id) {
        return makeFileFold(getRootPath() + LOCALplayurl) + "/" + id + ".mp3";
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

    public static boolean renameFile(String oldPath, String newPath) {
        try {
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                oldfile.renameTo(new File(newPath));
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

    public static void saveBmp(String savePath, Bitmap bitmap) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(savePath));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public static void delFiles(File file) {
        if (file.exists()) {                    //判断文件是否存在
            if (file.isFile()) {
                file.delete();                       //delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) {
                File files[] = file.listFiles(); //声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) {         //遍历目录下所有的文件
                    delFile(files[i].getAbsoluteFile());
                }
            }
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


    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }
}
