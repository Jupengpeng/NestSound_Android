package com.xilu.wybz.ui.IView;

import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.bean.SongAlbum;

import java.util.List;

/**
 * Created by June on 16/4/5.
 */
public interface IMusicTalkMoreView extends IBaseView{
    void showMusicTalkData(List<MusicTalk> musicTalks);
    void loadFail();
    void loadNoData();
    void loadNoMore();
}
