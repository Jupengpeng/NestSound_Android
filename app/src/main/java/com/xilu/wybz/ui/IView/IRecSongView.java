package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.MusicBean;
import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * 基础View接口
 * Created by June on 16/4/5.
 */
public interface IRecSongView extends IBaseView{
    void showMusicList(List<WorksData> musicBeanList);
    void showProgressBar();
    void hideProgressBar();
    void showErrorView();
}