package com.xilu.wybz.ui.bean;

import com.xilu.wybz.bean.*;

import java.util.List;

/**
 * Created by June on 16/5/3.
 */
public class HomeItem {
    public ItemType itemType;
    public String tagName;
    public List<com.xilu.wybz.bean.Banner> bannerList;//banner
    public List<WorksData> recommendWorkList;//推荐作品
    public List<SongAlbum> songAlbumList;//歌单
    public List<WorksData> newWorkList;//最新作品
    public List<MusicTalk> musicTalkList;//乐说
}
