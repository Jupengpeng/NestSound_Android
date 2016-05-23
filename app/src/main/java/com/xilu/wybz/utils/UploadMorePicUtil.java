package com.xilu.wybz.utils;

import android.content.Context;
import android.text.TextUtils;

import com.xilu.wybz.common.DownLoaderDir;
import com.xilu.wybz.common.MyHttpClient;

import net.bither.util.NativeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by hujunwei on 16/4/7.
 */
public class UploadMorePicUtil {
    public  List<String> imageUrls;
    public  int num;//标记上传了几个了
    public  String images = "";
    public  Context context;
    public UploadMorePicUtil(Context context){
        this.context = context;
    }
    /*
    * fixx 文件类型空间前缀标示。灵感记录=inspire，歌词封面=lyrcover,歌曲封面=muscover,头像=head
     */
    public void uploadPics(List<String> pics, UploadPicResult uploadPicsInterface) {
        imageUrls = new ArrayList<>();
        num = 0;
        images = "";
        for (int i = 0; i < pics.size(); i++) {
            final int c = i;
            imageUrls.add("");
            final String imgPath = pics.get(i);
            double size = FileUtils.getFileOrFilesSize(imgPath, 2);
            final String newPath = DownLoaderDir.inspirePicDir + UUID.randomUUID() + ".jpg";
            if (size > 300) {
                NativeUtil.CompressionPic(imgPath, newPath, new NativeUtil.CompressPicInterface() {
                    @Override
                    public void onCompressResult(int errorCode, String path) {
                        if (errorCode == 0) { //压缩成功 上传新图
                            upLoadPic(path, c, uploadPicsInterface);
                        } else { //压缩失败 上传原
                            upLoadPic(imgPath, c, uploadPicsInterface);
                        }
                    }
                });
            } else { //图片本身就小 无需压缩
                //直接上传图片到七牛
                upLoadPic(imgPath, imageUrls.size() - 1, uploadPicsInterface);
            }
        }
    }

    public void upLoadPic(String imgPath, int pos, UploadPicResult uploadPicsInterface) {
        ImageUploader imageUploader = new ImageUploader();
        imageUploader.setFilePath(imgPath, 1, "inspire");
        imageUploader.UpLoad(context);
        imageUploader.setOnUploadListener(new ImageUploader.OnUploadListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    String imageUrl = MyHttpClient.QINIU_URL + new JSONObject(result).getString("key");
                    imageUrls.set(pos, imageUrl);
                    num++;
                } catch (Exception e) {
                } finally {
                }
                if (num == imageUrls.size()) {
                    loadOver(uploadPicsInterface);
                }
            }

            @Override
            public void onFailure() {
                uploadPicsInterface.onFail();
            }
            @Override
            public void UpLoadProgress(double percent) {

            }
        });
    }

    public void loadOver(UploadPicResult uploadPicsInterface) {
        for (int i = 0; i < imageUrls.size(); i++) {
            String imageUrl = imageUrls.get(i);
            if (!TextUtils.isEmpty(imageUrl)) {
                images += imageUrl + ",";
            }
        }
        uploadPicsInterface.onSuccess(images);
    }

    public interface UploadPicResult {
        void onSuccess(String images);
        void onFail();
    }
}
