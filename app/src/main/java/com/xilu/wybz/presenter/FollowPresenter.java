package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CommentBean;
import com.xilu.wybz.bean.FansBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IFollowAndFansView;
import com.xilu.wybz.utils.ParseUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/24.
 */
public class FollowPresenter extends BasePresenter<IFollowAndFansView> {

    public FollowPresenter(Context context, IFollowAndFansView iView) {
        super(context, iView);
    }

    public void loadData(int userId,int type,int page){
        params = new HashMap<>();
        params.put("userid",userId+"");
        params.put("type",type+"");
        params.put("page",page+"");
        httpUtils.get(MyHttpClient.getFansList(),params,new MyStringCallback(){
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }

            @Override
            public void onResponse(String response) {
                List<FansBean> mList = ParseUtils.getFansData(context,response);
                if(mList!=null) {
                    if (mList.size() == 0) {
                        if (page == 1) {
                            iView.loadNoData();
                        } else {
                            iView.loadNoMore();
                        }
                    } else {
                        iView.showFansData(mList);
                    }
                }
            }

        });
    }
    public void follow(){

    }
}
