package com.xilu.wybz.user;

import com.xilu.wybz.utils.LyricsDraftUtils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2016/6/1.
 */
public class FilesTest {

    @Test
    public void testFiles() throws Exception{


        File file = new File(".");

        String[] strings = file.list();
        for(String e:strings){
            println(e);
        }
//        println(file.getCanonicalPath());


        assertEquals(1, 1);

    }

    @Test
    public void testDraft() throws Exception{

        LyricsDraftUtils.directories = "draftDir/";
        File file = new File("src/main/assets/rhyme.json");
//        println(file.getCanonicalPath());

//        String text = LyricsDraftUtils.get(file);
//        println("text:"+text);

//        LyricsDraftBean bean = new LyricsDraftBean();
//        bean.id = "111";
//        bean.name = "111";
//        bean.file = "teest";
//        LyricsDraftUtils.save(bean);

//        List<LyricsDraftBean> files =LyricsDraftUtils.getAllDraft();
//        for(LyricsDraftBean e:files){
//            println(e.name);
//        }

//        LyricsDraftUtils.deleteAll();

        assertEquals(1, 1);

    }


    @Test
    public void test2() throws Exception{


//        String fileName = "E:\\test.text";
//        RandomAccessFile rf = new RandomAccessFile(fileName, "rw");
//        for (int i = 0; i < 10; i++) {
//            //写入基本类型double数据
//            rf.writeDouble(i * 1.414);
//        }
//        rf.close();
//        rf = new RandomAccessFile(fileName, "rw");
//        //直接将文件指针移到第5个double数据后面
//        rf.seek(5 * 8);
//        //覆盖第6个double数据
//        rf.writeDouble(47.0001);
//        rf.close();
//        rf = new RandomAccessFile(fileName, "r");
//        for (int i = 0; i < 10; i++) {
//            System.out.println("Value " + i + ": " + rf.readDouble());
//        }
//        rf.close();
    }

    @Test
    public void test3() throws Exception {
//        int length = 0x8000000; // 128 Mb
//        String fileName = "E:\\test3.text";
//        // 为了以可读可写的方式打开文件，这里使用RandomAccessFile来创建文件。
//        FileChannel fc = new RandomAccessFile(fileName, "rw").getChannel();
//        //注意，文件通道的可读可写要建立在文件流本身可读写的基础之上
//        MappedByteBuffer out = fc.map(FileChannel.MapMode.READ_WRITE, 0, length);
//        //写128M的内容
//        for (int i = 0; i < length; i++) {
//            out.put((byte) 'x');
//        }
//        System.out.println("Finished writing");
//        //读取文件中间6个字节内容
//        for (int i = length / 2; i < length / 2 + 6; i++) {
//            System.out.print((char) out.get(i));
//        }
//        fc.close();
    }

    private void println(String s) {
        System.out.println(s);
    }

}
