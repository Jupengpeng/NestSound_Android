package com.xilu.wybz.user;


import com.xilu.wybz.utils.DateFormatUtils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2016/5/23.
 */
public class DateFormatUtilTest {

    @Test
    public void testPrase2DateText() throws Exception {

        System.out.println("this is testPrase2DateText...");

        String time1 = "2015-01-25";
        String time2 = "2014-04-15";
        String time3 = "2019-01-30";
        String time4 = "2014-12-05";


        System.out.println(time1+": "+DateFormatUtils.prase2DateText(time1));
        System.out.println(time2+": "+DateFormatUtils.prase2DateText(time2));
        System.out.println(time3+": "+DateFormatUtils.prase2DateText(time3));
        System.out.println(time4+": "+DateFormatUtils.prase2DateText(time4));


        DateFormatUtils.DateText date1 = DateFormatUtils.prase2DateText(time1);
        DateFormatUtils.DateText date2 = DateFormatUtils.prase2DateText(time2);
        DateFormatUtils.DateText date3 = DateFormatUtils.prase2DateText(time3);
        DateFormatUtils.DateText date4 = DateFormatUtils.prase2DateText(time4);

        assertEquals(date1.getYear(), "2015");
        assertEquals(date1.getMonth(), "一月");
        assertEquals(date1.getDay(), "25");
        assertEquals(date1.getYearMonth(), "一月\n2015");

    }


    @Test
    public void testFormatTime() throws Exception {
        System.out.println("this is testFormatTime...");


        System.out.println(""+DateFormatUtils.formatTime(1));
        System.out.println(""+DateFormatUtils.formatTime(67));
        System.out.println(""+DateFormatUtils.formatTime(72));
        System.out.println(""+DateFormatUtils.formatTime(756));


        System.out.println(""+DateFormatUtils.formatTime(1l));
        System.out.println(""+DateFormatUtils.formatTime(67l));
        System.out.println(""+DateFormatUtils.formatTime(72l));
        System.out.println(""+DateFormatUtils.formatTime(756l));

        assertEquals(1,1);

    }

    @Test
    public void testPrintln() throws Exception {

        println("this is testPrintln...");

        assertEquals(1,1);

    }


    public void println(String text){
        System.out.println(text);
    }


}
