package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CollectBean;
import com.xilu.wybz.ui.IView.ICollectView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public class CollectPresenter extends BasePresenter<ICollectView> {

    public CollectPresenter(Context context, ICollectView iView) {
        super(context, iView);
    }

    public void getCollectList() {
        List<CollectBean> collectBeanList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            CollectBean collectBean = new CollectBean();
            collectBean.setCreatetime(1473665904+i);
            collectBean.setNickname("qwewqewqe"+i);
            collectBean.setWorknum(i);
            collectBean.setTitle("按实际开"+i);
            if(i==1){
                collectBean.setStatus(1);
            }else if(i==2){
                collectBean.setStatus(3);
            }else if(i==3){
                collectBean.setStatus(4);
            }else if(i==4){
                collectBean.setStatus(8);
            }
            collectBeanList.add(collectBean);
        }
        iView.showCollectList(collectBeanList);
    }
}
