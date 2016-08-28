package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.GleeDetailBean;

/**
 * 基础View接口
 * Created by June on 16/4/5.
 */
public interface IRecSongView extends IBaseView{
    void showSongDetail(GleeDetailBean gleeDetailBean);
    void showProgressBar();
    void hideProgressBar();
    void showErrorView();
}