package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;

/**
 * Created by June on 16/4/5.
 */
public interface ILyricsView extends IBaseView {
    void showProgressBar();
    void hideProgressBar();
    void showErrorView();
    void loadLyrics(WorksData worksData);
    void zanStart();
    void zanSuccess();
    void zanFail();
    void zanFinish();
    void favStart();
    void favSuccess();
    void favFail();
    void favFinish();
    void loadPicSuccess(String pic);

}
