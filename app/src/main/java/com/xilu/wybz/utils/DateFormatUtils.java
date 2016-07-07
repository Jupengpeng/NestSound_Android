package com.xilu.wybz.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DateFormatUtils {

    public static final String[] MONTH_MASK = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};



    public static String formatTime(int time){
        if (time == 0){
            return String.format("00:00");
        }
        if (time <= 0){
            time = -time;
            return String.format("-%02d:%02d",(time/60)%60,time%60);
        }

        return String.format("%02d:%02d",(time/60)%60,time%60);
    }


    public static String formatTime(long time){
        long hour;
        long minute;
        long second;

        if (time <= 0){
            return String.format("00:00:00");
        }
        hour = time/60/60;
        minute = (time/60)%60;
        second = time%60;

        return String.format("%02d:%02d:%02d",hour,minute,second);

    }

    public static String formatVolumeTime(int size){
        long hour;
        long minute;
        long second;
        long time;

        time = size/20;
        if (size%20>1){
            time++;
        }

        if (time <= 0){
            return String.format("00:00:00");
        }
        hour = time/60/60;
        minute = (time/60)%60;
        second = time%60;

        return String.format("%01d:%02d:%02d",hour,minute,second);

    }


//    public static DateText prase2Text(String timeString){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date date = format.parse(timeString);
//            DateText t = new DateText();
//            t.month = ""+MONTH_MASK[date.getMonth()];
//            t.day = ""+date.getDate();
//            t.year = ""+(date.getYear()+1900);
//            return t;
//        } catch (Exception e) {
//            return new DateText();
//        }
//
//    }

    public static DateText prase2DateText(long timestamp){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(new Date(timestamp));
        return prase2DateText(dateString);
    }

    public static DateText prase2DateText(String timeString){

        try {

            DateText dateText = new DateText();

            dateText.year = timeString.substring(0,4);
            dateText.month = MONTH_MASK[Integer.valueOf(timeString.substring(5,7))-1];
            dateText.day = timeString.substring(8,10);

            return dateText;
        } catch (Exception e) {
            Log.d("utils","DateFormatUtils.prase2DateText() is faild.");
            return new DateText();
        }

    }


    public static class DateText{

        private String year;
        private String month;
        private String day;

        public DateText(){

        }

        public DateText(String year ,String month, String day){
            this.year = new String(year);
            this.month = new String (month);
            this.day = new String (day);
        }

        public String getYearMonth(){
            String text = month+"\n"+year;
            return text;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        @Override
        public String toString() {
            return "DateText{" +
                    "year='" + year + '\'' +
                    ", month='" + month + '\'' +
                    ", day='" + day + '\'' +
                    '}';
        }
    }
}
