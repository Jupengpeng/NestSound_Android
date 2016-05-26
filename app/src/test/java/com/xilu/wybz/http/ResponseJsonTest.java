package com.xilu.wybz.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.Response;
import com.xilu.wybz.bean.TokenBean;
import com.xilu.wybz.http.callback.AppStringCallback;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2016/5/25.
 */
public class ResponseJsonTest {

    @Test
    public void test() throws Exception {


        Gson json = new Gson();

        String jsonString = "{\"message\":\"test\",\"code\":200,\"data\":\"{name is zhangjun}\"}";


        Response<String> result = json.fromJson(jsonString, new TypeToken<Response<String>>(){}.getType());

        System.out.println(result);

        assertEquals(1,1);

    }


    @Test
    public void testResponse() throws Exception {


        System.out.println("testResponse:");

        String jsonString = "{\"message\":\"test\",\"code\":200,\"data\":[\"a:s\",bs] }";
        System.out.println("json:"+jsonString);
        AppStringCallback app = new AppStringCallback(){
            @Override
            public void onResponse(Response<? extends Object> response) {
                System.out.println("data:"+response.toString());
                String name = response.getData().getClass().getSimpleName();
                System.out.println("name:"+name);
                List<String> data = (List<String>)response.getData();
                System.out.println("data:"+data.get(0));
            }

            @Override
            public Type getDataType() {
                return new TypeToken<Response<List<String>>>(){}.getType();
            }
        };

        app.onResponse(jsonString);

        assertEquals(1,1);
    }


    @Test
    public void testError() throws Exception {
//        String json = "{\"data\":\"{\"filename\":\"lyrcover\\/2016052516553094741175\",\"token\":\"nUdQcDzxiYQzP9tClx85tec8GWq7ZMMqvqufWd8f:d85OOPuqm3FFz6d0MObkdk6m9RM=:eyJzY29wZSI6InVwbG9hZGZpbGVzIiwiZGVhZGxpbmUiOjE0NjQxNzAxMzB9\",\"domain_qiliu\":\"http:\\/\\/7xru8x.com2.z0.glb.qiniucdn.com\"}\",\"message\":\"\",\"code\":200}";
        String json = "{\"data\":{\"filename\":\"75\",\"token\":\"nUdQczB9\",\"domain_qiliu\":\"iucdncom\"},\"message\":\"qwe\",\"code\":200}";
        System.out.println("testError:");


        AppStringCallback app = new AppStringCallback(){
            @Override
            public void onResponse(Response<? extends Object> response) {
                System.out.println("data:"+response.toString());
                String name = response.getData().getClass().getSimpleName();
                System.out.println("name:"+name);
//                List<String> data = (List<String>)response.getData();
//                System.out.println("data:"+data.get(0));
            }

            @Override
            public Type getDataType() {
                return new TypeToken<Response<TokenBean>>(){}.getType();
            }
        };

        app.onResponse(json);

        assertEquals(1,1);




    }


}
