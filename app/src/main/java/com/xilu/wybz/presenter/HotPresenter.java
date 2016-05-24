package com.xilu.wybz.presenter;

import android.content.Context;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IHotView;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class HotPresenter extends BasePresenter<IHotView> {
    public HotPresenter(Context context, IHotView iView) {
        super(context, iView);
    }
    public void loadHotData(String name, int type, int page) {
        Map<String,String> params = new HashMap<>();
        params.put("name",name);
        params.put("type",type+"");
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getHotUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                iView.loadSuccess(response);
            }
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail(e.getMessage());
            }
        });
    }
}
