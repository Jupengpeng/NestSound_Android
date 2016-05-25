package com.xilu.wybz.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/25.
 */
public class Response<T> implements Serializable{

    protected int code;
    protected String message;
    protected String error;
    protected T data;
    protected String tag;

    public Response(){

    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public <D extends Object> D getData() {
        return (D)data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                ", data=" + data +
                ", tag='" + tag + '\'' +
                '}';
    }
}
