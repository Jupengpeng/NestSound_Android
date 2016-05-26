package com.xilu.wybz.user;

import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Created by Administrator on 2016/5/22.
 */
public class UnitTest {

    @Test
    public void addition_isCorrect() throws Exception {
        String time = "2014-04-05";


        List<Tim> l = new ArrayList<>(20);
        l.add(new Tim(1));
        l.add(new Tim(2));
        l.add(new Tim(3));

        Tim t4 = new Tim(4);
//        l.add(t4);

        l.set(0,t4);



        assertEquals(4, 2 + 2);



    }



    @Test
    public void testRect() throws Exception {

//        Rect rect = new Rect(0,0,720,180);

//        System.out.println("centerX:"+rect.centerX());

        assertEquals(4, 2 + 2);
    }

    @Test
    public void testJson() throws Exception {

        String jsonString = "message";

        String data = new Gson().fromJson(jsonString,String.class);
        System.out.println("data:"+data);

        assertEquals(4, 2 + 2);
    }



    static class Tim{
        public int a = 0;

        Tim(int a){
            this.a = a;
        }
    }

}

