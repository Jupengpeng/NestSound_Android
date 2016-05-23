package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.IView.*;

/**
 * Created by June on 16/4/5.
 */
public interface ILyricsView extends com.xilu.wybz.ui.IView.IBaseView {
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

}
