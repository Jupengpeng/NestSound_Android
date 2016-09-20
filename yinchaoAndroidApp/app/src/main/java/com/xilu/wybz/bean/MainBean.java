package com.xilu.wybz.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunwei on 16/5/30.
 */
public class MainBean {
    public List<Banner> bannerList;
    public List<WorksData> mTuijianList;
    public List<SongAlbum> recommendsonglist;
    public List<WorksData> newList;
    public List<MusicTalk> yueshuoList;


    public List<Banner> getBannerList() {
        if (bannerList == null){
            bannerList = new ArrayList<>();
        }
        return bannerList;
    }

    public void setBannerList(List<Banner> bannerList) {
        this.bannerList = bannerList;
    }

    public List<WorksData> getmTuijianList() {
        if (mTuijianList == null){
            mTuijianList = new ArrayList<>();
        }
        return mTuijianList;
    }

    public void setmTuijianList(List<WorksData> mTuijianList) {
        this.mTuijianList = mTuijianList;
    }

    public List<SongAlbum> getRecommendsonglist() {
        if (recommendsonglist == null){
            recommendsonglist = new ArrayList<>();
        }
        return recommendsonglist;
    }

    public void setRecommendsonglist(List<SongAlbum> recommendsonglist) {
        this.recommendsonglist = recommendsonglist;
    }

    public List<WorksData> getNewList() {
        if (newList == null){
            newList = new ArrayList<>();
        }
        return newList;
    }

    public void setNewList(List<WorksData> newList) {
        this.newList = newList;
    }

    public List<MusicTalk> getYueshuoList() {
        if (yueshuoList == null){
            yueshuoList = new ArrayList<>();
        }
        return yueshuoList;
    }

    public void setYueshuoList(List<MusicTalk> yueshuoList) {
        this.yueshuoList = yueshuoList;
    }
}
