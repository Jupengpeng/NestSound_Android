package com.xilu.wybz.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.RhymeBean;

import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/9.
 */
public class RhymeJsonTest {


    @Test
    public void test()throws Exception{

        File file = new File("src/main/assets/rhyme.json");


        FileInputStream in = new FileInputStream(file);


        byte[] bf = new byte[1024*10];
        int size = in.read(bf);

        String s1 = new String(bf,0,size);
        String s2 = new String(s1.getBytes(Charset.forName("utf-8")));
        String s3 = new String(s1.getBytes(Charset.forName("utf-8")),"utf-8");


        JSONObject jsonObject = new JSONObject(s1);
//        jsonObject.getString("yunjiaolist");

//        String list = jsonObject.getJSONObject("data").getJSONObject("info").getString("yunjiaolist");

//        System.out.println(list);
        System.out.println(size);
        System.out.println(s1+"len:"+s1.length());
//        System.out.println(s2);
//        System.out.println(s3);
        Gson gson = new Gson();

        gson.fromJson(s1, new TypeToken<ArrayList<RhymeBean>>() { }.getType());
//
        String t1 = gson.toJson(new RhymeBean("asdsad","asdasd"));
        System.out.println(t1);
//        gson.fromJson(s1, new TypeToken<RhymeBean>(){ }.getType());

//        System.out.print(""+file.getCanonicalPath()+file.exists());
    }
}
