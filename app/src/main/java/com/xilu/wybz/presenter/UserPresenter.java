package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IUserView;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class UserPresenter extends BasePresenter<IUserView> {

    public static final int TYPE_SONG = 1;
    public static final int TYPE_LYRIC = 2;
    public static final int TYPE_COLLECTION = 3;
    public static final int TYPE_INSPIRATION = 4;


    public static final int TYPE_USER_CENTER = 5;
    public static final int TYPE_OTHER_CENTER = 6;


    public int userType;


    public UserPresenter(Context context, IUserView iView, int userType) {
        super(context, iView);
        this.userType = userType;
    }


    public void requestUserInfo(int userId) {

        request(userId,1,1,new AppStringCallback(context){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
            }
        });
    }


    public void request(int userId, int type, int page, MyStringCallback callback) {
        if (userType == TYPE_USER_CENTER) {
            executeUser(userId, type, page, callback);
        } else if (userType == TYPE_OTHER_CENTER) {
            executeOther(userId, type, page, callback);
        }
    }

    protected void executeUser(int userId, int type, int page, MyStringCallback callback) {
        execute(MyHttpClient.getUserCenter(), userId, type, page, callback);
    }

    protected void executeOther(int userId, int type, int page, MyStringCallback callback) {
        execute(MyHttpClient.getOtherCenter(), userId, type, page, callback);
    }

    protected void execute(String url, int userId, int type, int page, MyStringCallback callback) {
        params = new HashMap<>();
        params.clear();
        params.put("uid",userId+"");;
        params.put("type", "" + type);
        params.put("page", "" + page);
        httpUtils.get(url, params, callback);
    }


}
