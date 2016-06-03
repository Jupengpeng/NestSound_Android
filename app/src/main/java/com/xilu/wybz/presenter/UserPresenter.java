package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.AppStringCallback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IUserView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class UserPresenter extends BasePresenter<IUserView> {
    public UserPresenter(Context context, IUserView iView) {
        super(context, iView);
    }
    /*
    * userType 1自己 2别人
    * type null/0/1=歌曲，2=歌词，3=收藏,4=灵感记录
     */
    public void loadData(int userId, int type, int page) {
        int userType = (userId==PrefsUtil.getUserId(context))?1:2;
        if(type==0){
            type=4;
        }
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context)+"");//自己的id
        if(userType==2){
            params.put("otherid", userId+"");
        }
        params.put("type",type+"");
        params.put("page",page+"");
        httpUtils.get(userType==1?MyHttpClient.getUserCenter():MyHttpClient.getOtherCenter(),params,new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                MineBean mineBean = ParseUtils.getMineBean(context,response);
                if(mineBean!=null){
                    iView.setUserInfo(mineBean.user,mineBean.fansnum,mineBean.gznum);
                    if(mineBean.list!=null){
                        if (mineBean.list.size() == 0) {
                            if (page == 1) {
                                iView.loadNoData();
                            } else {
                                iView.loadNoMore();
                            }
                        } else {
                            iView.showWorksData(mineBean.list);
                        }
                    }
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
        });
    }
}
