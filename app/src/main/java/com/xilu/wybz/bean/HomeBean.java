package com.xilu.wybz.bean;

import java.util.List;

/**
 * Created by Zning on 2015/9/2.
 */
public class HomeBean {

    List<MusicBean> bannerMusicList;
    List<MusicBean> hotMusicList;
    List<MusicBean> newestMusicList;
    String neweststart;

    public List<MusicBean> getBannerMusicList() {
        return bannerMusicList;
    }

    public void setBannerMusicList(List<MusicBean> bannerMusicList) {
        this.bannerMusicList = bannerMusicList;
    }

    public List<MusicBean> getHotMusicList() {
        return hotMusicList;
    }

    public void setHotMusicList(List<MusicBean> hotMusicList) {
        this.hotMusicList = hotMusicList;
    }

    public List<MusicBean> getNewestMusicList() {
        return newestMusicList;
    }

    public void setNewestMusicList(List<MusicBean> newestMusicList) {
        this.newestMusicList = newestMusicList;
    }

    public String getNeweststart() {
        return neweststart;
    }

    public void setNeweststart(String neweststart) {
        this.neweststart = neweststart;
    }
}
