package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by June on 16/5/3.
 */
public class SongAlbum implements Serializable{
    public String id;
    public String pic;
    public String name;
    public String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
