package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ILoginView;
import com.xilu.wybz.ui.IView.IRankingView;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/5/4.
 */
public class RankingPresenter extends BasePresenter<IRankingView>{
    public RankingPresenter(Context context, IRankingView iView) {
        super(context, iView);
    }

}
