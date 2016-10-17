package com.xilu.wybz.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ProductInfo implements Parcelable {

    public String id;

    public String title;
    public String lyricAuthor;
    public String songAuthor;
    public String accompaniment;
    public long updatetime;
    public String image;
    public int type;
    public int cType;

    public String preserveDate;
    public String preserveID;

    public ProductInfo() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.lyricAuthor);
        dest.writeString(this.songAuthor);
        dest.writeString(this.accompaniment);
        dest.writeLong(this.updatetime);
        dest.writeString(this.image);
        dest.writeInt(this.type);
        dest.writeInt(this.cType);
        dest.writeString(this.preserveDate);
        dest.writeString(this.preserveID);
    }

    protected ProductInfo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.lyricAuthor = in.readString();
        this.songAuthor = in.readString();
        this.accompaniment = in.readString();
        this.updatetime = in.readLong();
        this.image = in.readString();
        this.type = in.readInt();
        this.cType = in.readInt();
        this.preserveDate = in.readString();
        this.preserveID = in.readString();
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
