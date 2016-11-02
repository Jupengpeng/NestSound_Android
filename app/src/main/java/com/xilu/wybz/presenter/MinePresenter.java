package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.MineBean;
import com.xilu.wybz.ui.IView.IMineView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public class MinePresenter extends BasePresenter<IMineView> {

    public MinePresenter(Context context, IMineView iView) {
        super(context, iView);
    }

    public void getMineList() {
        List<MineBean> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            MineBean mineBean = new MineBean();
            mineBean.setWorknum(i);
            mineBean.setTitle("qwewqewq" + i);
            if(i==2){
                mineBean.setStatus(2);
            }else if(i==3){
                mineBean.setStatus(3);
            }else {
                mineBean.setStatus(1);
            }
            mineBean.setCreatetime(444444444);
            list.add(mineBean);
        }

        iView.showMineList(list);
    }

}
