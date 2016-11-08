package com.xilu.wybz.presenter;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.JsonResponse;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.AppJsonCalback;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IUserCenterListView;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class UserCenterListPresenter extends BasePresenter<IUserCenterListView> {
    public UserCenterListPresenter(Context context, IUserCenterListView iView) {
        super(context, iView);
    }
    /*
    * userType 1自己 2别人
    * type null/0/1=歌曲，2=歌词，3=收藏,4=灵感记录
     */
    public void loadData(int type, int page) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");//自己的id
        params.put("type", type + "");
        params.put("page", page + "");
        httpUtils.get(MyHttpClient.getUserCenterList(), params, new AppJsonCalback(context) {
            @Override
            public Type getDataType() {
                return new TypeToken<List<WorksData>>(){}.getType();
            }

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                super.onResult(response);
                List<WorksData> worksDatas = response.getData();
                if(worksDatas!=null){
                    if(worksDatas.size()==0){
                        if(page==1){
                            iView.loadNoData();
                        }else{
                            iView.loadNoMore();
                        }
                    }else{
                        iView.showWorksData(worksDatas);
                    }
                }else{
                    iView.loadFail();
                }
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.loadFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.loadFail();
            }
        });
    }
    //删除作品
    public void delete(String id, int type) {
        params = new HashMap<>();
        params.put("id", id + "");
        params.put("type", type + "");
        httpUtils.post(MyHttpClient.getDeleteWorksUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                DataBean dataBean = ParseUtils.getDataBean(context, response);
                if (dataBean != null && dataBean.code == 200) {
                    iView.deleteSuccess();
                } else {
                    iView.deleteFail();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.deleteFail();
            }
        });
    }
    //删除作品
    public void deleteCooperate(String id) {
        params = new HashMap<>();
        params.put("uid", ""+PrefsUtil.getUserId(context));
        params.put("itemid", ""+id);
        httpUtils.post(MyHttpClient.getDeleteCooprateWorksUrl(), params, new AppJsonCalback(context) {

            @Override
            public void onResult(JsonResponse<? extends Object> response) {
                iView.deleteSuccess();
            }

            @Override
            public void onResultError(JsonResponse<? extends Object> response) {
                super.onResultError(response);
                iView.deleteFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                super.onError(call, e);
                iView.deleteFail();
            }
        });
    }

    //取消收藏
    public void unfav(String itemid, int target_uid, int wtype) {
        params = new HashMap<>();
        params.put("user_id", PrefsUtil.getUserId(context) + "");
        params.put("work_id", itemid);
        params.put("target_uid", target_uid + "");
        params.put("wtype", "" + wtype);
        httpUtils.post(MyHttpClient.getWorkFovUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context, response);
                if (dataBean != null && dataBean.code == 200)
                    iView.deleteSuccess();
                else
                    iView.deleteFail();
            }

            @Override
            public void onError(Call call, Exception e) {
                iView.deleteFail();
            }
        });
    }
    /* 取消收藏
     * id 作品id
     * type 1歌曲 2歌词
     * status 0不公开 1公开
    */
    public void updateWorksState(String id, int type, int status) {
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");
        params.put("id", id );
        params.put(type==1?"is_issue":"status", status + "");
        httpUtils.post(type==1?MyHttpClient.getUpdateMusicUrl():MyHttpClient.getUpdateLyricsUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                DataBean dataBean = ParseUtils.getDataBean(context, response);
                if (dataBean != null && dataBean.code == 200) {
                    iView.updateSuccess();
                }else
                    iView.updateFail();
            }
            @Override
            public void onError(Call call, Exception e) {
                iView.updateFail();
            }
        });
    }






}
