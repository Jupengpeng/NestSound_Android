package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.DataBean;
import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.common.MyHttpClient;
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
        if (userId <= 0) return;
        int userType = (userId == PrefsUtil.getUserId(context)) ? 1 : 2;
        params = new HashMap<>();
        params.put("uid", PrefsUtil.getUserId(context) + "");//自己的id
        if (userType == 2) {
            params.put("otherid", userId + "");
        }
        params.put("type", type + "");
        params.put("page", page + "");
        httpUtils.get(userType == 1 ? MyHttpClient.getUserCenter() : MyHttpClient.getOtherCenter(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                MineBean mineBean = ParseUtils.getMineBean(context, response);
                if (mineBean != null) {
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.fansnum = mineBean.fansnum;
                    userInfoBean.gznum = mineBean.gznum;
                    userInfoBean.isFocus = mineBean.isFocus;
                    userInfoBean.lyricsnum = mineBean.lyricsnum;
                    userInfoBean.worknum = mineBean.worknum;
                    userInfoBean.inspirenum = mineBean.inspirenum;
                    userInfoBean.fovnum = mineBean.fovnum;
                    iView.setUserInfo(mineBean.user);
                    iView.setUserInfoBean(userInfoBean);
                    PrefsUtil.saveUserInfoNum(context,userInfoBean);
                    if (mineBean.list != null) {
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
                } else {
                    iView.setUserInfo(null);
                    iView.loadNoData();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
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
