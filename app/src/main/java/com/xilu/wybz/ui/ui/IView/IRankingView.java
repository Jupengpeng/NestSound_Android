package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/5/8.
 */
public interface IRankingView extends com.xilu.wybz.ui.IView.IBaseView {
    void showRankingSong(List<WorksData> songWorksDatas);

    void showRankingLyrics(List<WorksData> lyricsWorksDatas);

    void showErrorView();
}
