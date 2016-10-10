package com.xilu.wybz.bean.VO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/29.
 */

public class NodeViewData implements Parcelable {


    public int id;
    public int type;
    public int cType;
    public int status;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeInt(this.cType);
        dest.writeInt(this.status);
    }

    public NodeViewData() {
    }

    protected NodeViewData(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.cType = in.readInt();
        this.status = in.readInt();
    }

    public static final Creator<NodeViewData> CREATOR = new Creator<NodeViewData>() {
        @Override
        public NodeViewData createFromParcel(Parcel source) {
            return new NodeViewData(source);
        }

        @Override
        public NodeViewData[] newArray(int size) {
            return new NodeViewData[size];
        }
    };
}
