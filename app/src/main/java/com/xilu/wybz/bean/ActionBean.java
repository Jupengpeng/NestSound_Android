package com.xilu.wybz.bean;

/**
 * Created by June on 16/5/6.
 */
public class ActionBean {
    String title;
    String type;
    public ActionBean(){

    }
    public ActionBean(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
