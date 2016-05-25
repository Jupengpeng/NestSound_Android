package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.ui.IView.IFindMoreWorkView;
import com.xilu.wybz.utils.ParseUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by hujunwei on 16/4/5.
 */
public class FindMoreWorkPresenter extends BasePresenter<IFindMoreWorkView> {
    public FindMoreWorkPresenter(Context context, IFindMoreWorkView iView) {
        super(context, iView);
    }
    public void findMoreWork(int workType, int orderType, int page) {
        params = new HashMap<>();
        params.put("orderType",orderType+"");
        params.put("page",page+"");
        httpUtils.get(workType==1?MyHttpClient.getFindMoreSongList():MyHttpClient.getFindMoreLyricsList(), params, new MyStringCallback() {
            @Override
            public void onResponse(String response) {
                List<WorksData> worksDatas = ParseUtils.getWorksData(context,response);
                if(worksDatas!=null){
                    if(worksDatas.size()==0){
                        if(page==1){
                            iView.loadNoData();
                        }else{
                            iView.loadNoMore();
                        }
                    }else{

                        iView.showWorkData(worksDatas);

                    }
                }else{
                    iView.loadFail();
                }
            }
            @Override
            public void onError(Call call, Exception e) {
                iView.loadFail();
            }
        });
    }
}
