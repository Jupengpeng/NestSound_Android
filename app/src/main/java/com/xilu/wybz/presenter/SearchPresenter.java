package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.ISearchView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by June on 16/4/5.
 */
public class SearchPresenter extends BasePresenter<ISearchView> {

    public SearchPresenter(Context context, ISearchView iView) {
        super(context, iView);
    }

    public void searchWorkData(String keyWord, int type, int page) {
        params = new HashMap<>();
        params.put("fansid", PrefsUtil.getUserId(context)+"");;
        params.put("name", keyWord);
        params.put("type", type+"");//1=歌曲，2=歌词，3=用户
        params.put("page", page+"");
        httpUtils.get(MyHttpClient.getSearchList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
            @Override
            public void onResponse(String response) {
                List<WorksData> mList = ParseUtils.getWorksData(context,response);
                if (mList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showWorksData(mList);
                }
            }
        });
    }
    public void searchUserData(String keyWord, int type, int page) {
        params = new HashMap<>();
        params.put("fansid", PrefsUtil.getUserId(context)+"");;
        params.put("name", keyWord);
        params.put("type", type+"");//1=歌曲，2=歌词，3=用户
        params.put("page", page+"");
        httpUtils.get(MyHttpClient.getSearchList(), params, new MyStringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
            @Override
            public void onResponse(String response) {
                List<FansBean> mList = ParseUtils.getFansData(context,response);
                if (mList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showUserData(mList);
                }
            }
        });
    }
}
