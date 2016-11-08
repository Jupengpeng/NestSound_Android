package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.Invitation;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IInvitationView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/26.
 */

public class InvitationPresenter extends BasePresenter<IInvitationView> {


    public InvitationPresenter(Context context, IInvitationView iView) {
        super(context, iView);
    }

    public void getInvitationList(int did, int page, String key) {
        params = new HashMap<>();
        params.put("did", did + "");
        params.put("page", page + "");
        params.put("key", key);
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        params.put("uid", "" + PrefsUtil.getUserId(context));
        httpUtils.post(MyHttpClient.getInvitelist(), params, new AppJsonCalback(context) {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<Invitation> invitationBeanList = response.getData();
                if (key != "" ) {
                    if (page == 1) {
                        if (invitationBeanList.size() == 0) {
                            iView.noSerachData();
                        }
                    } else {
                        if (invitationBeanList.size() == 0 || invitationBeanList.size()<=10) {
                            iView.noSerachMoreData();
                        }
                    }
                    if(invitationBeanList.size()>0){
                        iView.serachSuccess();
                    }
                } else {
                    if (page == 1) {
                        if (invitationBeanList.size() == 0) {
                            iView.noData();
                        }
                    } else {
                        if (invitationBeanList.size() == 0) {
                            iView.noMoreData();
                        }
                    }
                }
                iView.showInvitationList(invitationBeanList);
            }

            @Override
            public Type getDataType() {
                return new TypeToken<List<Invitation>>() {
                }.getType();
            }


        });


    }

    public void sendInvitation(int target_uid, int did) {
        params = new HashMap<>();
        params.put("uid", "" + PrefsUtil.getUserId(context));
        params.put("target_uid", target_uid + "");
        params.put("did", did + "");
        params.put("token", PrefsUtil.getUserInfo(context).loginToken);
        httpUtils.post(MyHttpClient.sendInvite(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                int commentId = ParseUtils.getCommentId(context, response);
                if (commentId == 200) {
                    iView.sendSuccess();
                }

            }

        });
    }
}
