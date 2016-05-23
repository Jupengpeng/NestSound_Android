package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CollectionBean;
import com.xilu.wybz.bean.MineBean;
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


    public UserPresenter(Context context, IUserView iView) {
        super(context, iView);
    }


    public void getInspirationList(String userId,int page){
        Map<String,String> param = new HashMap<>();

        param.put("uid",userId);
        param.put("type","4");
        param.put("page",""+page);

//        httpUtils.post(MyHttpClient.getfocusfansnum(userId),param, new MyStringCallback() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    if (ParseUtils.checkCode(response)) {
//                        JSONObject jsonObject = new JSONObject(response);
//                        String dataString = jsonObject.getString("data");
//
//                        MineBean mineBean= ParseUtils.parseMineBean(dataString);
//                        List<WorksData> dataList = mineBean.getList();
//
//                        if (dataList.size() == 0){
//                            if (page == 1){
//
//                            }
//
//                        }else {
////                            iView.loadUserInfoSuccess("");
//                        }
//
//
//                    } else {
//                        ParseUtils.showMsg(context, response);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
////                    iView.showErrorView();
//                }
//            }

//            @Override
//            public void onError(Call call, Exception e) {
//                super.onError(call, e);
//            }
//        });

    }
    public void getSongList(String userId,int page){

    }
    public void getLyricList(String userId,int page){

    }
    public void getCollectionList(String userId,int page){

    }








}
