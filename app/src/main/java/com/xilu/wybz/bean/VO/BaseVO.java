package com.xilu.wybz.bean.VO;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/24.
 */
public class BaseVO implements Serializable{

    public int requestCode;
    public int resultCode;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
