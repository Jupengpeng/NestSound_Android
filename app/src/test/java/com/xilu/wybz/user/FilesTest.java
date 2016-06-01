package com.xilu.wybz.user;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/1.
 */
public class FilesTest {

    @Test
    public void testFiles() throws Exception{


        File file = new File("E:\\bookmark\\1.txt");

//        println("mkdir:"+file.mkdir());
//        println("isFile:"+file.isFile());
//        println("isDirectory:"+file.isDirectory());
//
//        println("exists:"+file.exists());


        if (!file.isFile()) {
            file.createNewFile();
        }
        
        if (!file.exists()) {
            file.mkdirs();
        }
        println(file.toString());

        assertEquals(1, 1);

    }

    private void println(String s) {
        System.out.println(s);
    }
}
