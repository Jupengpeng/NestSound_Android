package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.ui.IView.IActView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;

/**
 * Created by June on 16/5/7.
 */
public class ActPresenter extends BasePresenter<IActView>{
    public ActPresenter(Context context, IActView iView) {
        super(context, iView);
    }
    public void getActList(int page){
        params = new HashMap<>();
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getFindActivityList(),params, new AppStringCallback(context){
            @Override
            public void onResponse(JsonResponse<? extends Object> response) {
                super.onResponse(response);
                List<ActBean> actBeens = response.getData();
                if(actBeens!=null){
                    if(actBeens.size()==0){
                        if(page==1){
                            iView.showNoData();
                        }else{
                            iView.showNoMore();
                        }
                    }else{
                        iView.showActList(actBeens);
                    }
                }
            }
            @Override
            public Type getDataType() {
                return new TypeToken<JsonResponse<List<ActBean>>>(){}.getType();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.showErrorView();
            }
        });
    }
}
