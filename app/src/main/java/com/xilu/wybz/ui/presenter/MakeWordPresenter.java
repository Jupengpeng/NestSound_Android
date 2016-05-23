package com.xilu.wybz.ui.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.presenter.*;
import com.xilu.wybz.ui.IView.ILyricsView;
import com.xilu.wybz.ui.IView.IMakeWordView;
import com.xilu.wybz.utils.ParseUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by June on 16/4/5.
 */
public class MakeWordPresenter extends com.xilu.wybz.presenter.BasePresenter<IMakeWordView> {
    public MakeWordPresenter(Context context, IMakeWordView iView) {
        super(context, iView);
    }


}
