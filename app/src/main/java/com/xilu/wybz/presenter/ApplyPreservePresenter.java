package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.bean.ProductInfo;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.ui.IView.IApplyPreservView;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/29.
 */

public class ApplyPreservePresenter extends BasePresenter<IApplyPreservView>{




    private String orderNumber ;



    public ApplyPreservePresenter(Context context, IApplyPreservView iView) {
        super(context, iView);
    }


//    uid	是	long	登录用户id
//    itemid	是	long	需要保权的作品id
//    type	是	int	作品类型 1歌曲 2歌词
//    ctype	是	int	保权类型 1歌曲 2歌词 3歌曲+歌词
//    cusername	是	String	保权用户名
//    ccard	是	String	保权用户名对应身份证号
//    cphone

    public void applyOrder(ProductInfo productInfo, PersonInfo personInfo){

        params = new HashMap<>();

        params.put("uid",""+ PrefsUtil.getUserId(context));
        params.put("itemid",""+productInfo.id);
        params.put("type",""+productInfo.typeId);
        params.put("ctype",""+productInfo.typeId);
        params.put("cusername",personInfo.cUserName);
        params.put("ccard",personInfo.cCardId);
        params.put("cphone",personInfo.cPhone);

        httpUtils.postHost(MyHttpClient.getPaypalOrder(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);

                orderNumber = response.getData();
                Log.e("pay","pay ok:data="+orderNumber);

                applyPay(orderNumber);
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                Log.e("pay",response.getData());
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                Log.e("pay","onError");
            }

//            @Override
//            public Type getDataType() {
//                return super.getDataType();
//            }

        });

    }

    /**
     * applyPay.
     * @param orderNumber
     */
    public void applyPay(String orderNumber){

        params = new HashMap<>();

        params.put("orderNo",orderNumber);

        httpUtils.postHost(MyHttpClient.getPaypalPay(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                Log.e("pay","applyPay:"+response.getData());

                String  data= response.getData();
                iView.startPay(data);

            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

//            @Override
//            public Type getDataType() {
//            }

        });
    }


    public void applyProdutInfo(int id){

        params = new HashMap<>();

        params.put("id",""+id);

        httpUtils.post(MyHttpClient.getPaypalOrder(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public Type getDataType() {
                return super.getDataType();
            }

        });
    }


    /**
     *
     */
    public void getPersonInfo(){

        params = new HashMap<>();

        params.put("uid",""+PrefsUtil.getUserId(context));

        httpUtils.post(MyHttpClient.getPaypalOrder(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public Type getDataType() {
                return super.getDataType();
            }

        });

    }

/*    bq_username	是	String	保全人姓名
    bq_email	是	String	保全人邮箱
    bq_phone	是	String	保全人电话
    bq_creditID*/

    /**
     *
     * @param personInfo
     */
    public void savePersonInfo( PersonInfo personInfo){

        params = new HashMap<>();

        params.put("uid",""+PrefsUtil.getUserId(context));
        params.put("bq_username",personInfo.cUserName);
        params.put("bq_email","");
        params.put("bq_phone",personInfo.cPhone);
        params.put("bq_creditID",personInfo.cCardId);

        httpUtils.post(MyHttpClient.getPaypalOrder(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public Type getDataType() {
                return super.getDataType();
            }

        });

    }







}
