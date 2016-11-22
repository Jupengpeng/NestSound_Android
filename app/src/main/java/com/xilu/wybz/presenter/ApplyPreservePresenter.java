package com.xilu.wybz.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.PersonInfo;
import com.xilu.wybz.bean.PreserveInfoBean;
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


    /**
     *
     * @param productInfo
     * @param personInfo
     */
    public void applyOrder(ProductInfo productInfo, PersonInfo personInfo){

        params = new HashMap<>();

        params.put("uid",""+ PrefsUtil.getUserId(context));
        params.put("itemid",""+productInfo.id);
        params.put("type",""+productInfo.type);
        params.put("cType",""+productInfo.cType);
        params.put("cUsername",personInfo.cUserName);
        params.put("cCardId",personInfo.cCardId);
        params.put("cPhone",personInfo.cPhone);

        iView.updateSubmitView(2);

        httpUtils.post(MyHttpClient.getPaypalOrder(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);

                orderNumber = response.getData();
                Log.e("pay","pay ok:data="+orderNumber);
//                iView.showLoading(orderNumber);
                applyPay(orderNumber);
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.updateSubmitView(1);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.updateSubmitView(1);
            }

        });

    }

    /**
     * applyPay.
     * @param orderNumber
     */
    public void applyPay(String orderNumber){

        params = new HashMap<>();

        params.put("orderNo",orderNumber);

        httpUtils.post(MyHttpClient.getPaypalPay(),params,new AppJsonCalback(context){

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
                iView.updateSubmitView(1);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.updateSubmitView(1);
            }

        });
    }


    /**
     * 回调支付状态.
     * @param status
     */
    public void applyPayCallback(int status){

        params = new HashMap<>();

        params.put("orderNo",orderNumber);
        params.put("status",""+status);

        httpUtils.post(MyHttpClient.getPaypalStatus(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                Log.i("pay","applyPayCallback");

            }





            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

        });
    }


    public void getApplyProdutInfo(String id,int type){

        params = new HashMap<>();

        params.put("uid",""+PrefsUtil.getUserId(context));
        params.put("id",""+id);
        params.put("sort_id",""+type);

        httpUtils.post(MyHttpClient.getPreservePreserveInfo(),params,new AppJsonCalback(context){

            @Override
            public void onResult(JsonResponse<? extends Object> response) {

                PreserveInfoBean bean = response.getData();
                if (bean == null){
                    iView.showError();
                    return;
                }
                try{
                    iView.showPage();
                    iView.updateProductInfo(bean.productInfo);
                    iView.updatePersonInfo(bean.personInfo);
                    iView.updateUsePrice(bean.orderPrice,bean.productInfo.type);
                } catch (Exception e){
                    iView.showError();
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.showError();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.showError();
            }

            @Override
            public Type getDataType() {
                return new TypeToken<PreserveInfoBean>(){}.getType();
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
