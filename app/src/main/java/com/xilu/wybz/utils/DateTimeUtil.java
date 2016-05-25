package com.xilu.wybz.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by June on 2015/8/11.
 */
public class DateTimeUtil {
    //String2Date
    public static Date str2Date(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            return new Date();
        }
    }

    //时间戳转化为Datetime
    public static String timestamp2DateTime(long timestamp) {
        if (timestamp == 0) return getDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    public static String timestamp2Date(long timestamp) {
        if (timestamp == 0) return getDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    public static String timestamp2Time(long timestamp) {
        if (timestamp == 0) return getDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    public static String timestamp2DateTime2(long timestamp) {
        if (timestamp == 0) return getDateTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(timestamp));
        return date;
    }

    //Datetime转化为时间戳
    public static long dteTime2Timestamp(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(datetime);
            return date.getTime() / 1000;
        } catch (Exception e) {
            return new Date().getTime() / 1000;
        }
    }

    //
    public static String getDateTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        return df.format(new Date());
    }

    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date());
    }

    //获取当前年份
    public static int getCurrentYear() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    //获取当前年份
    public static int getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    //获取当前日
    public static int getCurrentDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    //获取当前年份
    public static int getCurrentYear(String date) {
        return Integer.valueOf(date.substring(0, 4));
    }

    //获取当前月
    public static int getCurrentMonth(String date) {
        return Integer.valueOf(date.substring(5, 7));
    }

    //获取当前日
    public static int getCurrentDay(String date) {
        return Integer.valueOf(date.substring(8, 10));
    }

    //获取当前日
    public static int getCurrentHour(String time) {
        return Integer.valueOf(time.substring(0, 2));
    }

    //获取当前日
    public static int getCurrentMinute(String time) {
        return Integer.valueOf(time.substring(3, 5));
    }

    //比较时间的大小
    public static int compareDateTime(String datetime1, String datetime2) {
        try {
            long time1 = dteTime2Timestamp(datetime1);
            long time2 = dteTime2Timestamp(datetime2);
            if (time1 == time2)
                return 0;
            else if (time1 < time2)
                return 1;
            else
                return 2;
        } catch (Exception e) {
            return -1;
        }

    }

    public static String getWeekOfDate(String dateString) {
        Date dt = str2Date(dateString);
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    //比较时间的大小
    public static int compareDate(String datetime1, String datetime2) {
        java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(datetime1));
            c2.setTime(df.parse(datetime2));
        } catch (ParseException e) {
            return -1;
        }
        int result = c1.compareTo(c2);
        if (result == 0)
            return 0;//   c1=c2
        else if (result < 0)
            return 1;//   c1<c2
        else
            return 2;//   c1>c2
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     */
    public static String getTime(String d) {
        String str = "";
        Date date = new Date();
        long a = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newDate = sdf.parse(d);
            long b = sdf.parse(d).getTime();
            @SuppressWarnings("deprecation")
            int days = date.getDate() - newDate.getDate(); // 1000毫秒*60分钟*60秒*24小时
            int hours = (int) ((a - b) % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            int minutes = (int) (((a - b) % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) / (1000 * 60);
            int year = date.getYear() - newDate.getYear();
            int month = date.getMonth() - newDate.getMonth();
            if (year >= 1) {
                str = (newDate.getYear() + 1900) + "年" + (newDate.getMonth() + 1) + "月" + newDate.getDate() + "日";
                System.out.println("year" + newDate.getYear());
            } else if (year == 0) {
                if (days > 2 || month > 0) {
                    str = (newDate.getMonth() + 1) + "月" + newDate.getDate() + "日";
                } else if (days == 2) {
                    str = "前天";
                } else if (days == 1) {
                    str = "昨天";
                } else if (days == 0) {
                    if (hours > 0) {
                        str = hours + "小时前";
                    } else {
                        if (minutes > 0) {
                            str = minutes + "分钟前";
                        } else {
                            str = "刚刚";
                        }
                    }
                }
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     */
    public static String getTimes(String d) {
        String str = "";
        Date date = new Date();
        long a = date.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date newDate = sdf.parse(d);
            long b = sdf.parse(d).getTime();
            int hours = (int) ((a - b) % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            int minutes = (int) (((a - b) % (1000 * 60 * 60 * 24)) % (1000 * 60 * 60)) / (1000 * 60);
            int days = date.getDate() - newDate.getDate(); // 1000毫秒*60分钟*60秒*24小时
            int year = date.getYear() - newDate.getYear();
            int month = date.getMonth() - newDate.getMonth();
            String hoursAndMinutes = new SimpleDateFormat("HH:mm").format(newDate).toString();
            if (year >= 1) {
                str = (newDate.getYear() + 1900) + "-" + (newDate.getMonth() + 1) + "-" + newDate.getDate();
            } else if (year == 0) {
                if (days > 2 || month > 0) {
                    str = (newDate.getMonth() + 1) + "-" + newDate.getDate() + " " + hoursAndMinutes;
                } else if (days == 2) {
                    str = "前天 " + hoursAndMinutes;
                } else if (days == 1) {
                    str = "昨天 " + hoursAndMinutes;
                } else if (days == 0) {
                    if (hours > 0) {
                        str = hours + "小时前";
                    } else {
                        if (minutes > 0) {
                            str = minutes + "分钟前";
                        } else {
                            str = "刚刚";
                        }
                    }
                }

            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    public static String getNewDate(String date) {
        return date.substring(0, 4) + "年" + date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
    }
}
