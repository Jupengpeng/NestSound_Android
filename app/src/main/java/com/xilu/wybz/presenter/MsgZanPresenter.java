package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.ZambiaBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.interfaces.HttpLinstener;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IZanView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/4/5.
 */
public class MsgZanPresenter extends BasePresenter<IZanView> {

    public MsgZanPresenter(Context context, IZanView iView) {
        super(context, iView);
    }

    public void loadData(int page) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getMsgZanList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                if(ParseUtils.checkCode(response)){
                    try {
                        String resultlist = new JSONObject(response).getString("data");
                        List<ZambiaBean> mList = new Gson().fromJson(resultlist, new TypeToken<List<ZambiaBean>>() {}.getType());
                        if(mList.size()==0){
                            if(page==1){
                                iView.loadNoData();
                            }else{
                                iView.loadNoMore();
                            }
                        }else{
                            iView.showZambiaData(mList);
                        }

                    } catch (JSONException e) {
                        iView.loadNoData();
                    }

                }else{
                    iView.loadFail();
                }
            }
        });
    }
}
