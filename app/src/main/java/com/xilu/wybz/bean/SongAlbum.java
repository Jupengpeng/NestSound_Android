package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by June on 16/5/3.
 */
public class SongAlbum implements Serializable{
    public String itemid;
    public String pic;
    public String name;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
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
