package com.xilu.wybz.user;
import com.xilu.wybz.utils.NumberUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Created by Administrator on 2016/5/24.
 */
public class NumberUtilTest {

    @Test
    public void testFormat() throws Exception {

        int n1 = 6090;
        int n2 = 12200;
        int n3 = 2312200;

        String t1 = NumberUtil.format(n1);
        String t2 = NumberUtil.format(n2);
        String t3 = NumberUtil.format(n3);
        System.out.println(t1+"-->"+n1);
        System.out.println(t2+"-->"+n2);
        System.out.println(t3+"-->"+n3);


        assertEquals(t1,"6090");
        assertEquals(t2,"1.2万");
        assertEquals(t3,"231.2万");

    }


    @Test
    public void test1() throws Exception {
        int x = -100;
        int s = 40;

        x -= 2*s*(x/(2*s));

        System.out.println("s:"+"-->"+x);



    }



}
