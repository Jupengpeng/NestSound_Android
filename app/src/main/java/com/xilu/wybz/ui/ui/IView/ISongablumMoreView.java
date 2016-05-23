package com.xilu.wybz.ui.ui.IView;

import com.xilu.wybz.bean.SongAlbum;
import com.xilu.wybz.ui.IView.*;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface ISongablumMoreView extends com.xilu.wybz.ui.IView.IBaseView {
    void showSongAblumData(List<SongAlbum> songAlbumList);
    void loadFail();
    void loadNoData();
    void loadNoMore();
}
