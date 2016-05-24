package com.xilu.wybz.utils;

/**
 * Created by Administrator on 2016/5/24.
 */
public class NumberUtil {


    /**
     * 将浏览及赞的数量格式化.
     * @param number
     * @return
     */
    public static String format(int number){
        String numberText;
        if (number < 10000){
            numberText = String.valueOf(number);
        } else {
            numberText = String.format("%.1f%s",1.0*number/10000,"万");
        }
        return numberText;
    }
}
