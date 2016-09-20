package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.SongAlbum;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface ISongablumMoreView extends IBaseView{
    void showSongAblumData(List<SongAlbum> songAlbumList);
    void loadFail();
    void loadNoData();
    void loadNoMore();
}
