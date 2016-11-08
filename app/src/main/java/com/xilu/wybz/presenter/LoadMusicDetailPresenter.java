package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IMusicDetailView;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by June on 16/5/4.
 */
public class LoadMusicDetailPresenter extends BasePresenter<IMusicDetailView> {
    int count;
    public LoadMusicDetailPresenter(Context context, IMusicDetailView iView) {
        super(context, iView);
    }

    public void loadMusicDetail( String id, String type) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("id", id);
        if (StringUtils.isNotBlank(type)){
            params.put("type", type);
        } else {
//            params.put("type", "");
        }
        httpUtils.get(MyHttpClient.getMusicWorkUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                if(NetWorkUtil.isNetworkAvailable(context)){
                    if(count<2) {
                        loadMusicDetail(id,type);
                        count++;
                    }else{
                        count=0;
                        iView.loadFail();
                    }
                }else{
                    iView.loadFail();
                }
            }
            @Override
            public void onResponse(String response) {
                WorksData currMdb = ParseUtils.getWorkData(context, response);
                if (currMdb != null) {
                    iView.showMusicDetail(currMdb);
                }else{
                    iView.loadFail();
                }
            }
        });
    }

}
