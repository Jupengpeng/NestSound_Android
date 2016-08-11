package com.xilu.wybz.utils;

import com.google.gson.Gson;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.common.FileDir;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/10.
 */
public class LyricsDraftUtils {


    public static String directories = FileDir.draftDir;
//        public static String directories = "draftDir/";
    public static Charset UTF_8 = Charset.forName("utf-8");


    public static List<LyricsDraftBean> getAllDraft() {
        List<LyricsDraftBean> list = new ArrayList<>();
        LyricsDraftBean bean;
        File[] files = getList();
        if (files == null || files.length <= 0){
            return list;
        }
        Gson json = new Gson();
        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }
            String text = get(file);
            bean = json.fromJson(text, LyricsDraftBean.class);
            bean.file = file.getAbsolutePath();
            list.add(bean);
        }
        return list;
    }


    public static LyricsDraftBean getDraftByName(String fileName) {
        File file = new File(fileName);
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        String json = get(file);
        LyricsDraftBean bean =new Gson().fromJson(json, LyricsDraftBean.class);
        bean.file = file.getAbsolutePath();
        return bean;
    }


    public static boolean save(LyricsDraftBean bean) {
        String text = bean.getJsonString();
        String fileName = MD5Util.getMD5String(text);

        File file = new File(getNameByCheck(fileName));

        try {
            FileOutputStream out = new FileOutputStream(file);

            out.write(text.getBytes(UTF_8));
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String get(File file) {
        if (!file.isFile()) {
            return null;
        }

        try {
            FileInputStream in = new FileInputStream(file);
            byte[] bf = new byte[1024];
            StringBuffer buffer = new StringBuffer();
            int size = 0;
            while (true) {
                size = in.read(bf);
                if (size <= 0) {
                    break;
                }
                buffer.append(new String(bf, 0, size, UTF_8));
            }
            in.close();
            return buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getNameByCheck(String fileName) {
        String name = directories + fileName + ".json";
        File file = new File(name);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            return name;
        }
        int index = 1;
        while (true) {
            name = getName(fileName, index);
            file = new File(name);
            if (!file.exists()) {
                return name;
            }
            index++;
        }
    }

    public static String getName(String fileName, int index) {
        return directories + fileName + "(" + index + ").json";
    }

    public static File createDir() {
        File file = new File(directories);
        if (file.exists()) {
            if (file.isDirectory()) {
                return file;
            } else {
                file.delete();
                file.mkdir();
                return file;
            }
        } else {
            file.mkdirs();
        }
        return file;
    }

    public static File[] getList() {
        File file = new File(directories);
        File[] files = null;
        if (file.exists() && file.isDirectory()) {
            files = file.listFiles();
        }
        return files;
    }

    public static String getId() {
        File[] files = getList();
        String ID;
        long id = 16 * (files == null ? 0 : files.length);
        ID = String.valueOf(id + directories.hashCode()+System.currentTimeMillis());
        return ID;
    }

    public static void deleteAll() {
        File[] files = getList();
        for (File file : files) {
            file.delete();
        }
    }

    public static void delete(String fileName) {

        if (fileName == null || fileName.trim().length()<=0) return;

        File file = new File(fileName);
        if (file.exists()){
            file.delete();
        }
    }

}
