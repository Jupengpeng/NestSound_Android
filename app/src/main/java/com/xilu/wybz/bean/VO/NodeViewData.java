package com.xilu.wybz.bean.VO;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/29.
 */

public class NodeViewData implements Parcelable {


    public int id;



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
    }

    public NodeViewData() {
    }

    protected NodeViewData(Parcel in) {
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<NodeViewData> CREATOR = new Parcelable.Creator<NodeViewData>() {
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
