package com.xilu.wybz.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/18.
 */
public class PersonInfo implements Parcelable {

    public String cUserName;
    public String cCardId;
    public String cPhone;
    public String cEmail;
    public int cType = 1;
    public int from = 1;






    public String getTypeString(){
        String type = "/";
        if (cType == 1){
            type = "曲作者";
        } else if (cType == 2){
            type = "词作者";
        } else if (cType == 3){
            type = "曲作者/词作者";
        }
        return type;
    }

    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cUserName);
        dest.writeString(this.cCardId);
        dest.writeString(this.cPhone);
        dest.writeString(this.cEmail);
        dest.writeInt(this.cType);
        dest.writeInt(this.from);
    }

    public PersonInfo() {
    }

    protected PersonInfo(Parcel in) {
        this.cUserName = in.readString();
        this.cCardId = in.readString();
        this.cPhone = in.readString();
        this.cEmail = in.readString();
        this.cType = in.readInt();
        this.from = in.readInt();
    }

    public static final Creator<PersonInfo> CREATOR = new Creator<PersonInfo>() {
        @Override
        public PersonInfo createFromParcel(Parcel source) {
            return new PersonInfo(source);
        }

        @Override
        public PersonInfo[] newArray(int size) {
            return new PersonInfo[size];
        }
    };
}
