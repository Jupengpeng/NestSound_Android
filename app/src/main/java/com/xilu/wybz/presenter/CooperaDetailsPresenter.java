package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.PreinfoBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ICooperaDetailsView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/26.
 */

public class CooperaDetailsPresenter extends BasePresenter<ICooperaDetailsView> {

    public CooperaDetailsPresenter(Context context, ICooperaDetailsView iView) {
        super(context, iView);
    }

    public void getCooperaDetailsBean(int did, int page) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("page", page + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.getCooperaDetail(), params, new AppJsonCalback(context) {
            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                CooperaDetailsBean cooperaDetailsBean = response.getData();
                if (page == 1) {
                    if (cooperaDetailsBean.getCompleteList().size() == 0) {
                        iView.noCompleteData();
                    }
                    iView.showCooperaCompleteList(cooperaDetailsBean.getCompleteList());
                    iView.showCooperaDetailsBean(cooperaDetailsBean);
                    iView.showCooperaCommentList(cooperaDetailsBean.getCommentList());

                }
                if (page != 1) {
                    iView.showCooperaCompleteList(cooperaDetailsBean.getCompleteList());
                }


            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<CooperaDetailsBean>() {
                }.getType();
            }
        });
    }

    public void getPreinfo(int did) {
        params = new HashMap<>();
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("did", did + "");
        httpUtils.post(MyHttpClient.getPreinfo(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);

            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                PreinfoBean preinfoBean = response.getData();
                iView.showpreinfoBean(preinfoBean);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<PreinfoBean>() {
                }.getType();
            }
        });

    }

    public void collect(int did, int type) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("type", type + "");
        httpUtils.post(MyHttpClient.getFov(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.collectSuccess(type);
                }
            }
        });
    }

    public void accept(int did, int itemid, int pos) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("itemid", itemid + "");
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);

        httpUtils.post(MyHttpClient.getAccept(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.acceptSuccess(pos);
                }
            }
        });
    }

}
