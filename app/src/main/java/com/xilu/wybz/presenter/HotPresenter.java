package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.FileCallBack;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.ParseUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class HotPresenter extends BasePresenter<IHotView> {
    public HotPresenter(Context context, IHotView iView) {
        super(context, iView);
    }
    /*
    * name 搜索的关键词
    * type 1=最新伴奏列表 2=最热伴奏列表 默认为最新
     */
    public void loadHotData(String title, int type, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("title", title);
        params.put("type", type + "");
        params.put("page", page + "");
        httpUtils.get(MyHttpClient.getHotUrl(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                List<TemplateBean> mList = ParseUtils.getTemplatesData(context, response);
                if (mList == null || mList.size() == 0) {
                    if (page == 1) {
                        iView.loadNoData();
                    } else {
                        iView.loadNoMore();
                    }
                } else {
                    iView.showHotData(mList,type);
                }
            }
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
        });
    }
    public void downHot(String fileDir,String fileName,String url){
        httpUtils.getFile(url, new FileCallBack(fileDir,fileName+".temp") {
            @Override
            public void inProgress(float progress, long total) {

            }
            @Override
            public void onError(Call call, Exception e) {

            }
            @Override
            public void onResponse(File file) {
                try {
                    FileUtils.renameFile(fileDir+fileName+".temp",fileDir+fileName);
                }catch (Exception e){
                }
                iView.downloadSuccess();
            }
        });
    }
}
