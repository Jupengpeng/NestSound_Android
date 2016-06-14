package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.WorksData;

import java.util.List;

/**
 * Created by June on 16/5/8.
 */
public interface IRankingView extends IBaseView {
    void showRankingSong(List<WorksData> songWorksDatas);

    void showRankingLyrics(List<WorksData> lyricsWorksDatas);

    void showErrorView();

    void showNoData(int type);

    void loadFinish();
}
