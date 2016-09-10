package com.xilu.wybz.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xilu.wybz.utils.WeChatPayUtils;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, WeChatPayUtils.APP_ID);
        api.handleIntent(getIntent(), this);
    }

	/**
	 *
	 * @param intent
     */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		Log.d("WXPayEntryActivity","onReq()");
	}

	@Override
	public void onResp(BaseResp baseResp) {
		Log.d("WXPayEntryActivity","onResp(BaseResp baseResp)");
		if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (baseResp instanceof PayResp){
				WeChatPayUtils.onResp((PayResp)baseResp);
			}
		}
		finish();
	}
}