package com.xilu.wybz.bean;

/**
 * Created by June on 16/4/6.
 */
public class RhymeBean {
    public String yunjiao;
    public String text;

    public RhymeBean() {
    }

    public RhymeBean(String yunjiao, String text) {
        this.yunjiao = yunjiao;
        this.text = text;
    }

    public String getYunjiao() {
        return yunjiao;
    }

    public void setYunjiao(String yunjiao) {
        this.yunjiao = yunjiao;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
