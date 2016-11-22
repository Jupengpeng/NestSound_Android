package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IOtherCenterListView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class OtherCenterListPresenter extends BasePresenter<IOtherCenterListView> {
    public OtherCenterListPresenter(Context context, IOtherCenterListView iView) {
        super(context, iView);
    }
    /*
    * userType 1自己 2别人
    * type null/0/1=歌曲，2=歌词，3=收藏,4=灵感记录
     */
    public void loadData(int otherid, int type, int page) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");//自己的id
        params.put("otherid", otherid + "");
        params.put("type", type + "");
        params.put("page", page + "");
        httpUtils.get( MyHttpClient.getOtherCenterList(), params, new AppJsonCalback(context) {
            @Override
            public Type getDataType() {
                return new TypeToken<List<WorksData>>(){}.getType();
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<WorksData> worksDatas = response.getData();
                if(worksDatas!=null){
                    if(worksDatas.size()==0){
                        if(page==1){
                            iView.loadNoData();
                        }else{
                            iView.loadNoMore();
                        }
                    }else{
                        iView.showWorksData(worksDatas);
                    }
                }else{
                    iView.loadFail();
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.loadFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadFail();
            }
        });
    }
}
