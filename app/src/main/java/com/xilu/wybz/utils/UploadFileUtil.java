package com.xilu.wybz.utils;

import android.content.Context;

import com.xilu.wybz.common.MyHttpClient;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by hujunwei on 16/4/7.
 */
public class UploadFileUtil {
    public String mFilePath;
    public Context context;
    public UploadResult mUploadResult;
    public UploadFileUtil(Context context){
        this.context = context;
    }
    /*
    * fixx 文件类型空间前缀标示。灵感记录=inspire，歌词封面=lyrcover,歌曲封面=muscover,头像=head
     */
    public void uploadFile(String filePath, String fixx, UploadResult uploadResult) {
        mFilePath = filePath;
        mUploadResult = uploadResult;
        ImageUploader imageUploader = new ImageUploader();
        imageUploader.setFilePath(filePath, fixx);
        imageUploader.setOnUploadListener(new ImageUploader.OnUploadListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    String filrUrl = new JSONObject(result).getString("key");
                    mUploadResult.onSuccess(filrUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                mUploadResult.onFail();
            }

            @Override
            public void UpLoadProgress(double percent) {

            }
        });
        imageUploader.UpLoad(context);
    }
    public interface UploadResult {
        void onSuccess(String filrUrl);
        void onFail();
    }

}
