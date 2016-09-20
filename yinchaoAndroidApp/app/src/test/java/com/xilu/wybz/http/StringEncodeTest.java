package com.xilu.wybz.http;

import com.xilu.wybz.http.rsa.RSAUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/20.
 */
public class StringEncodeTest {


    @Test
    public void test() throws Exception {

        String s1 = new String("测试".getBytes(), "utf-8");

        String s2 = new String(s1.getBytes("gb2312"), "gb2312");

        String s3 = new String(s2.getBytes("utf-8"), "utf-8");

        System.out.println("s1 = " + s1);
        System.out.println("s1 = " + s2);
        System.out.println("s1 = " + s3);


        assertEquals(1, 1);

    }


    @Test
    public void test2() throws Exception {

//        String text = "根据口味别估计额外功能为欧冠我见过过偶尔我后宫过和我饿哦个文件欧冠和围殴文件柜过欧文欧冠和我玩微话题偶尔我火攻我刚好伪皇宫过委会偶尔我会更根据口味别估计额外功能为欧冠我见过过偶尔我后宫过和我饿哦个文件欧冠和围殴文件柜过欧文欧冠和我玩微话题偶尔我火攻我刚好伪皇宫过委会偶尔我会更根据口味别估计额外功能为欧冠我见过过偶尔我后宫过和我饿哦个文件欧冠和围殴文件柜过欧文欧冠和我玩微话题偶尔我火攻我刚好伪皇宫过委会偶尔我会更";
//        String text = "结果高偶尔安海鸥结果高偶尔安海鸥让我难过高如果高欧文还工委结果高偶尔安海鸥让我难过高如果高欧文还工委结果高偶尔安海鸥让我难过高如果高欧文还工委结果高偶尔安海鸥让我难过高如果高欧文还工委结果高偶尔安海鸥让我难过高如果高欧文还工委结果高偶尔安海鸥让我难过高如果高欧文还工委结果高偶尔安海鸥让我难过高如果高欧文还工委结果高偶尔安海鸥让我难过高如果高欧文还工委让我难过高如果高欧文还工委";
//        String text = "{\"expiretime\":\"146640559034799000619879258\",\"token\":\"e638ac72b8983df9b1e88395838d1dce\",\"comment\":\"哦了了了咯哦哦哦哦哦哦\",\"uid\":\"26516\",\"target_uid\":\"0\",\"type\":\"1\",\"itemid\":\"8236\",\"comment_type\":\"1\"}";

        String text = "TEpsYmhBMXkyQ09GL05RRE9rSTFRTXVOVkJLSTVaZHRpSURXVUdCdkdkRm95eWpndng5YnpyQWVQOUpDV0Z4dnB6bkRyaTl3aDFEVzlDRDhiOG11cUVqcW12K2N1ZTNLd1NWQ3JHcU11N1JFdTJVOHJraGZXSWVvcTN1QS9pU1luYm0xUmJJbkZLL25ONGZjTHRsWUJSTHpTUWI2TGNZUWdQalhZazFtcm5NPQ==";
        String s1 = RSAUtils.encryptByPublicKey(text);
        String s2 = RSAUtils.decryptByPrivateKey(s1);


        System.out.println("s0 = " + RSAUtils.decryptByPublicKey(new String(RSAUtils.decodeConvert(text), "UTF-8")));
        System.out.println("s1 = " + s1);
        System.out.println("s2 = " + s2);

        System.out.println("sk = " + RSAUtils.decrypt(RSAUtils.encrypt(text)));

        assertEquals(1, 1);



    }


    @Test
    public void test3() throws Exception {
        String text = "{\"expiretime\":\"146640559034799000619879258\",\"token\":\"e638ac72b8983df9b1e88395838d1dce\",\"comment\":\"哦了了了咯哦哦哦哦哦哦\",\"uid\":\"26516\",\"target_uid\":\"0\",\"type\":\"1\",\"itemid\":\"8236\",\"comment_type\":\"1\"}\n";

        byte[] data = text.getBytes();
        System.out.println("s1 = "+data.length);
        for (int i=0;i<data.length;i++){

            System.out.print(data[i]);
        }
        System.out.println();
        byte[] data2 = ArrayUtils.subarray(data,0,117);
//        System.out.println("\ns2 = ");
        for (int i=0;i<data2.length;i++){
            System.out.print(data2[i]);
        }

        byte[] data3 = ArrayUtils.subarray(data,117,211);
        for (int i=0;i<data3.length;i++){

            System.out.print(data3[i]);
        }
        System.out.println();
        byte[] data4 = ArrayUtils.addAll(data2,data3);
        for (int i=0;i<data4.length;i++){

            System.out.print(data4[i]);
        }

    }

    @Test
    public void test4() throws Exception {
        String match = "1234\n\r1124\n1245\\n";
        System.out.println(":"+match);

        match = match.replaceAll("\\\\n","@");
        System.out.println(":"+match);
        match = match.replaceAll("[\\n\\r\\t]+","@");

        System.out.println(":"+match);
        System.out.println(":"+match.split("@").length);
        System.out.println(":"+new int[2].length);

    }

    public String replaceAll(String s,String regularExpression,String replacement ){
        return Pattern.compile(regularExpression).matcher(s).replaceAll(replacement);
    }

}
