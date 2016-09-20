package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.HotBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IHotCatalogView;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/8/8.
 */
public class HotCatalogPresenter extends BasePresenter<IHotCatalogView>{
    public HotCatalogPresenter(Context context, IHotCatalogView iView) {
        super(context, iView);
    }
    public void loadData(int page){
        params = new HashMap<>();
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getHotCatalogUrl(),params,new AppJsonCalback(context){
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadFail();
            }

            @Override
            public Type getDataType() {
                return HotBean.class;
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                HotBean hotBean = response.getData();
                if(hotBean!=null&&hotBean.list!=null)
                    if(hotBean.list.size()>0){
                        iView.showHotCatalog(hotBean);
                    }else{
                        iView.loadNoMore();
                    }
                else
                    iView.loadNoData();
            }
        });
    }
}
