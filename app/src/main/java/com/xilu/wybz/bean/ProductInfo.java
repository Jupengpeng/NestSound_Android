package com.xilu.wybz.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/20.
 */

public class ProductInfo implements Parcelable {

    public int id;

    public String title;
    public String lyricAuthor;
    public String songAuthor;
    public String accompaniment;
    public long createTime;
    public String image;
    public int type;
    public int cType;

    public long preserveDate;
    public String preserveID;

    public ProductInfo() {
    }


    /*
     productInfo:{
            "id":1,
            "title":"name",
            "lyricAuthor":"sd"
            "songAuthor":"sd",
            "accompaniment":"yinchao",//伴奏
            "createTime"："14265756856"
            "image":"http://pic.yinchao.cn/214124.png"
            "type":1

            "preserveDate":
            "preserveID"://保全号
        }
    */

    /**
     *
     * @param name
     * @param time
     */
    public ProductInfo(String name, long time) {
        this.title = name;
        this.createTime = time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.lyricAuthor);
        dest.writeString(this.songAuthor);
        dest.writeString(this.accompaniment);
        dest.writeLong(this.createTime);
        dest.writeString(this.image);
        dest.writeInt(this.type);
        dest.writeLong(this.preserveDate);
        dest.writeString(this.preserveID);
    }

    protected ProductInfo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.lyricAuthor = in.readString();
        this.songAuthor = in.readString();
        this.accompaniment = in.readString();
        this.createTime = in.readLong();
        this.image = in.readString();
        this.type = in.readInt();
        this.preserveDate = in.readLong();
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
