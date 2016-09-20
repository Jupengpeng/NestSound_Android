package com.xilu.wybz.bean;

/**
 * Created by cfanr on 2015/12/4.
 */
public enum ItemType {
    RECOMMENDWORK(0),
    SONGALBUM(1),
    NEWWORK(2),
    MUSICTALK(3);

    public int getValue(){
        return value;
    }

    private int value;
    ItemType(int value){
        this.value = value;
    }

}
