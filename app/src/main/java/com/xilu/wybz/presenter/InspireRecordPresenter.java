package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IBaseView;
import com.xilu.wybz.ui.IView.IInspireRecordView;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;


/**
 * 基础presenter
 * Created by june on 2016/5/1.
 */
public class InspireRecordPresenter extends BasePresenter<IInspireRecordView>{
    public InspireRecordPresenter(Context context, IInspireRecordView iView) {
        super(context, iView);
    }
    public void publishData(WorksData worksData){
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");
        params.put("spirecontent",worksData.spirecontent);
        params.put("pics",worksData.pics);
        params.put("audio",worksData.audio);
        httpUtils.post(MyHttpClient.getSaveInspireUrl(),params,new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean.code==200){
                    iView.pubSuccess();
                }else{
                    iView.pubFail();
                }
            }
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.pubFail();
            }
        });
    }
}