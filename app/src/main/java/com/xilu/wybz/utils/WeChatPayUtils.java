package com.xilu.wybz.utils;

import android.content.Context;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.modelpay.PayResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xilu.wybz.common.MyCommon;

/**
 * Created by Administrator on 2016/9/10.
 */
public class WeChatPayUtils {


    public final static String APP_ID = MyCommon.WECHAT_APP_ID;
    public static IWXAPI wechatApi;


    public final static int TYPE_OK = 0;
    public final static int TYPE_ERROR = -1;
    public final static int TYPE_CANCLE = -2;

    /**
     * register.
     * @param context
     */
    public static boolean register(Context context) {

        final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

        if (!api.isWXAppInstalled()){
            return false;
        }

        api.registerApp(APP_ID);
        wechatApi = api;
        return true;
    }

    /**
     * unregister.
     * @param context
     */
    public static void unregister(Context context) {
        if (wechatApi != null){
            wechatApi.unregisterApp();
            wechatApi = null;
        }
    }


    public static boolean pay(){
        if (wechatApi == null){
            return false;
        }
        wechatApi.sendReq(createPayReq());

        return true;
    }



//    IWXAPI api;
//    PayReq request = new PayReq();
//    request.appId = "wxd930ea5d5a258f4f";
//    request.partnerId = "1900000109";
//    request.prepayId= "1101000000140415649af9fc314aa427",;
//    request.packageValue = "Sign=WXPay";
//    request.nonceStr= "1101000000140429eb40476f8896f4c9";
//    request.timeStamp= "1398746574";
//    request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";
//    api.sendReq(req);

    public static PayReq createPayReq(){
        PayReq payReq = new PayReq();
//        payReq.appId = result.getPayInfo().getAppid();
//        payReq.partnerId =result.getPayInfo().getPartnerId();
//        payReq.prepayId = result.getPayInfo().getPrepayId();
//        payReq.packageValue =result.getPayInfo().getPackageValue();
//        payReq.nonceStr = result.getPayInfo().getNoncestr();
//        payReq.timeStamp = result.getPayInfo().getTimeStamp();
//        payReq.extData = new Gson().toJson(vo);
//        payReq.sign = result.getPayInfo().getSign();
        return payReq;
    }


    /**
     * 处理wechat支付回调.
     * @param payResp
     */
    public static void onResp(PayResp payResp){
        switch (payResp.getType()){
            case TYPE_OK:
                break;
            case TYPE_ERROR:
                break;
            case TYPE_CANCLE:
                break;
        }
    }

}
