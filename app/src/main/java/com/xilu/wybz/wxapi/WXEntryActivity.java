package com.xilu.wybz.wxapi;


import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends WXCallbackActivity {


    @Override
    public void onResp(BaseResp resp) {
        Log.d("WXEntryActivity","onResp()");
        super.onResp(resp);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.d("WXEntryActivity","onReq()");
        super.onReq(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("WXEntryActivity","onCreate()");
        super.onCreate(savedInstanceState);
    }
}
