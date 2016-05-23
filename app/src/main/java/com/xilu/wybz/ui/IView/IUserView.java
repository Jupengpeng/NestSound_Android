package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by hujunwei on 16/4/5.
 * 用户主页 也可以是我的主页
 */
public interface IUserView extends IBaseView {


    void addInspirationDatas(List<WorksData> datas);
    void addSongDatas(List<WorksData> datas);
    void addLyricDatas(List<WorksData> datas);
    void addCollectionDatas(List<WorksData> datas);


    void showInspirationNoData();
    void showSongNoData();
    void showLyricNoData();
    void showCollectionNoData();

    void showInspirationNoNet();
    void showSongNoNet();
    void showLyricNoNet();
    void showCollectionNoNet();



}
