package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.ui.IView.IRankingView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class RankingPresenter extends BasePresenter<IRankingView>{
    public RankingPresenter(Context context, IRankingView iView) {
        super(context, iView);
    }
    public void loadRankingData(int modelType){
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("modelType",modelType+"");
        params.put("page","1");
        httpUtils.get(MyHttpClient.getRankingList(),params,new AppStringCallback(){
            @Override
            public void onResponse(JsonResponse<? extends Object> response) {
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
