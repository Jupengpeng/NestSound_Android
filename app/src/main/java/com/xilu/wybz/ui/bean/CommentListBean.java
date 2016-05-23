package com.xilu.wybz.ui.bean;

import com.xilu.wybz.bean.*;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/3/21.
 */
public class CommentListBean implements Serializable {
    int listcount;
    List<com.xilu.wybz.bean.CommentBean> items;

    public int getListcount() {
        return listcount;
    }

    public void setListcount(int listcount) {
        this.listcount = listcount;
    }

    public List<com.xilu.wybz.bean.CommentBean> getItems() {
        return items;
    }

    public void setItems(List<com.xilu.wybz.bean.CommentBean> items) {
        this.items = items;
    }
}
