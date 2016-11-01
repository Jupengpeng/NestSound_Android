package com.xilu.wybz.presenter;

import android.content.Context;

import com.xilu.wybz.bean.CooperaLyricBean;
import com.xilu.wybz.ui.IView.ICooperaLyricView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/25.
 */

public class CooperaLyricPresenter extends BasePresenter<ICooperaLyricView> {


    public CooperaLyricPresenter(Context context, ICooperaLyricView iView) {
        super(context, iView);
    }

    public void getCooperaLyricList() {
        List<CooperaLyricBean> cooperaLyricBeen = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CooperaLyricBean lyricBean = new CooperaLyricBean();
            lyricBean.setStatus(i);
            lyricBean.setTitle("我是大哥我怕谁"+i);
            lyricBean.setCreatetime(1333333333);
            lyricBean.setAuthor("请问王企鹅请问" + i);
            lyricBean.setLyrics("我期待着我的梦想\n希望有一天能发出光芒\n\n我思念着我的故乡\n不知何时才能回到它身旁\n我思念着我的亲人\n多么希望他们身体安康");
            cooperaLyricBeen.add(lyricBean);
        }
        iView.showCooperaLyricList(cooperaLyricBeen);
    }

}
