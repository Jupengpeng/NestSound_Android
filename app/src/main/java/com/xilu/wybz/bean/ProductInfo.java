package com.xilu.wybz.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ProductInfo implements Parcelable {

    public String id;

    public String name;
    public long time;
    public String typeId;

    /**
     *
     * @param name
     * @param time
     */
    public ProductInfo(String name, long time) {
        this.name = name;
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.time);
        dest.writeString(this.typeId);
    }

    public ProductInfo() {
    }

    protected ProductInfo(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.time = in.readLong();
        this.typeId = in.readString();
    }

    public static final Creator<ProductInfo> CREATOR = new Creator<ProductInfo>() {
        @Override
        public ProductInfo createFromParcel(Parcel source) {
            return new ProductInfo(source);
        }

        @Override
        public ProductInfo[] newArray(int size) {
            return new ProductInfo[size];
        }
    };
}
