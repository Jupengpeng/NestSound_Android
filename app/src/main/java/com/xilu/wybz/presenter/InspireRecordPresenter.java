package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.HttpUtils;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IBaseView;
import com.xilu.wybz.ui.IView.IInspireRecordView;
import com.xilu.wybz.ui.IView.ILoginView;

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
    public void publishData(String userId, WorksData worksData){

    }
}