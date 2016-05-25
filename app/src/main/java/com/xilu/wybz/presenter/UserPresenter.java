package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.utils.ParseUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class UserPresenter extends BasePresenter<IUserView> {

    public static final int TYPE_SONG           = 1;
    public static final int TYPE_LYRIC          = 2;
    public static final int TYPE_COLLECTION     = 3;
    public static final int TYPE_INSPIRATION    = 4;


    public UserPresenter(Context context, IUserView iView) {
        super(context, iView);


    }


    public void getInspirationList(String userId, int page) {

        executeUser(userId, 1, page, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                Log.d("http", response);
                if (ParseUtils.checkCode(response)) {
                    List<WorksData> dataList = getDataList(response);

                } else {
                    ParseUtils.showMsg(context, response);
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });

    }

    public void getSongList(String userId, int page) {
        executeUser(userId, 1, page, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                Log.d("http", response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });

    }

    public void getLyricList(String userId, int page) {
        executeUser(userId, 1, page, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                Log.d("http", response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }

    public void getCollectionList(String userId, int page) {
        executeUser(userId, 1, page, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                Log.d("http", response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }



    protected List<WorksData> getDataList(String json){
        MineBean mineBean = ParseUtils.parseMineBean(json);
        if (mineBean == null){
            return null;
        }
        return mineBean.getList();
    }
    protected UserBean getUserInfo(String json){
        MineBean mineBean = ParseUtils.parseMineBean(json);
        if (mineBean == null){
            return null;
        }
        return mineBean.getUser();
    }

    protected void executeUser(String userId,int type, int page, MyStringCallback callback ){
       execute(MyHttpClient.getUserCenter(),userId,type,page,callback);
    }
    protected void executeOther(String userId,int type, int page, MyStringCallback callback ){
        execute(MyHttpClient.getOtherCenter(),userId,type,page,callback);
    }

    protected void execute(String url, String userId,int type, int page, MyStringCallback callback ){
        Map<String, String> param = new HashMap<>();
        param.put("uid", userId);
        param.put("type", "" + type);
        param.put("page", "" + page);
        httpUtils.get(url,param,callback);
    }



}
