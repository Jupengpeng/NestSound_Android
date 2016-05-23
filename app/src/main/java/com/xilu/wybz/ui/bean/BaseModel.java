package com.xilu.wybz.ui.bean;

import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

public class BaseModel implements Serializable {
 
    // 设置为主键,自增
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    public int id;
 
    public int getId() {
        return id;
    }
 
    public void setId(int id) {
        this.id = id;
    }
}