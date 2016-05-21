package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.ActBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IActView;
import com.xilu.wybz.utils.ParseUtils;
import org.json.JSONObject;
import java.util.List;
import okhttp3.Call;

/**
 * Created by June on 16/5/7.
 */
public class ActPresenter extends BasePresenter<IActView>{
    public ActPresenter(Context context, IActView iView) {
        super(context, iView);
    }
    public void getActList(){
        httpUtils.get(MyHttpClient.getEventUrl(),new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                if (ParseUtils.checkCode(response)) {
                    try {
                        String newitems = new JSONObject(response).getJSONObject("data")
                                .getJSONObject("info").getJSONObject("neweventlist").getString("items");
                        List<ActBean> actBeans = new Gson().fromJson(newitems, new TypeToken<List<ActBean>>() {
                        }.getType());
                       iView.showActList(actBeans);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.showErrorView();
            }
        });
    }
}
