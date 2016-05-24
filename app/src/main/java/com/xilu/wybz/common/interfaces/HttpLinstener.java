package com.xilu.wybz.common.interfaces;

/**
 * Created by June on 16/4/5.
 */
public interface HttpLinstener {
    void OnSuccess(String result);
    void OnStart();
    void OnFinish();
    void OnFail(String msg);
}
