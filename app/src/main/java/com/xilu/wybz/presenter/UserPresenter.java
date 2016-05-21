package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IUserView;
import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class UserPresenter extends BasePresenter<IUserView> {

    public UserPresenter(Context context, IUserView iView) {
        super(context, iView);
    }

    public void getFocusFansCount(Context context, String userId) {
        httpUtils.get(MyHttpClient.getfocusfansnum(userId), new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                iView.loadFocusFansCountSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadFocusFansCountFail(e.getMessage());
            }
        });
    }

    public void getUserInfo(Context context, String userId) {
        httpUtils.get(MyHttpClient.getMineUrl(userId), new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                iView.loadUserInfoSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadUserInfoFail(e.getMessage());
            }
        });
    }

    public void delItem(final Context context, String userId, String ids, int which) {
        String url = "";
        switch (which) {
            case 0:
                url = MyHttpClient.getDelMusicUrl(ids, userId);
                break;
            case 1:
                url = MyHttpClient.getDelLyricsUrl(ids, userId);
                break;
            case 2:
                url = MyHttpClient.getRemoveSomeFavUrl(ids, userId);
                break;
        }
        httpUtils.get(url, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                iView.delSuccess(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.delFail(e.getMessage());
            }
        });
    }
}
