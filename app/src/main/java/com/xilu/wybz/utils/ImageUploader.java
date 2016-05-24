package com.xilu.wybz.utils;

import android.content.Context;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.xilu.wybz.ui.MyApplication;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/3/7.
 */
public class ImageUploader {
    String filePath;
    int fileType;//1 图片 2 音乐
    String fixx;
    String endStrs[] = new String[]{".jpg", ".mp3"};

    public void setFilePath(String filePath, int fileType, String fixx) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.fixx = fixx;
    }

    public interface OnUploadListener {
        void onSuccess(String imageURL);

        void onFailure();

        void UpLoadProgress(double percent);
    }

    public void setOnUploadListener(OnUploadListener onUploadListener) {
        this.onUploadListener = onUploadListener;
    }

    OnUploadListener onUploadListener;

    public void UpLoad(final Context context) {
        GetToken getToken = new GetToken(context);
        getToken.setTokenResult(new GetToken.TokenResult() {
            @Override
            public void OnResult(String token, String filename) {
                uploadPic(token, filename);
            }
        });
        getToken.getToken(fileType, fixx);
    }

    public void uploadPic(String token, String filename) {
        MyApplication.uploadManager.put(filePath, filename + endStrs[fileType], token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        Log.e("response", response.toString());
                        if (response == null) {
                            onUploadListener.onFailure();
                        } else
                            try {
                                onUploadListener.onSuccess(response.toString());
                            } catch (Exception ex) {

                                onUploadListener.onFailure();
                            }
                    }
                }, null);
    }
}
