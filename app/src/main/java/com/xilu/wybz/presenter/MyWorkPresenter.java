package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IMyWorkView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 2016/4/28.
 */
public class MyWorkPresenter extends BasePresenter<IMyWorkView> {
    public MyWorkPresenter(Context context, IMyWorkView iView) {
        super(context, iView);
    }

    public void loadData(int page,int type) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("page", page + "");
        httpUtils.get(type==0?MyHttpClient.getUserMusicListUrl():MyHttpClient.getUserLyricsListUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
            @Override
            public void onResponse(String response) {
                List<WorksData> mList = ParseUtils.getWorksData(context, response);
                if (mList != null) {
                    if (mList.size() == 0) {
                        if (page == 1) {
                            iView.loadNoData();
                        } else {
                            iView.loadNoMore();
                        }
                    } else {
                        iView.showData(mList);
                    }
                }
            }
        });
    }
    public void attend(String aid, String workid) {
        params = new HashMap<>();
        params.put("userid", PrefsUtil.getUserId(context) + "");
        params.put("activityid", aid + "");
        params.put("workid", workid);
        httpUtils.post(MyHttpClient.getAttendUrl(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.attendFail();
            }
            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context,response);
                if(dataBean.code==200){
                    iView.attendSuccess();
                }else{
                    iView.attendFail();
                }
            }
        });
    }
}
