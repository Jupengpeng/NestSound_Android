package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.Response;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.ui.IView.IRankingView;
import com.xilu.wybz.utils.ParseUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class RankingPresenter extends BasePresenter<IRankingView>{
    public RankingPresenter(Context context, IRankingView iView) {
        super(context, iView);
    }
    public void loadRankingData(int userId,int modelType){
        params = new HashMap<>();
        params.put("uid",userId+"");
        params.put("modelType",modelType+"");
        params.put("page","1");
        httpUtils.get(MyHttpClient.getRankingList(),params,new AppStringCallback(){
            @Override
            public void onResponse(Response<? extends Object> response) {
                super.onResponse(response);
                List<WorksData> worksDatas = response.getData();
                if(worksDatas!=null){
                    switch (modelType){
                        case 1:
                            iView.showRankingSong(worksDatas);
                            break;
                        case 2:
                            iView.showRankingLyrics(worksDatas);
                            break;
                    }
                }
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<WorksData>>(){}.getType();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.showErrorView();
            }
        });
    }
}
