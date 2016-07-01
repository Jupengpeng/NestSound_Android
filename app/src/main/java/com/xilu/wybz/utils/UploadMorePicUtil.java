package com.xilu.wybz.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyCommon;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;

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
            String imgPath = pics.get(i);
            if(StringUtil.isNotBlank(imgPath)&&new File(imgPath).exists()) {
                imageUrls.add("");
                if(!new File(FileDir.inspirePicDir).exists()){
                    new File(FileDir.inspirePicDir).mkdirs();
                }
                File compressedImageFile = new Compressor.Builder(context)
//                        .setMaxWidth(640)
//                        .setMaxHeight(480)
//                        .setQuality(80)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(FileDir.inspirePicDir)
                        .build()
                        .compressToFile(new File(imgPath));
                Log.e("imgPath", imgPath);
                Log.e("compressedImageFile", compressedImageFile.getPath());
                upLoadPic(compressedImageFile!=null&&compressedImageFile.exists() ? compressedImageFile.getPath() : imgPath, imageUrls.size() - 1, uploadPicsInterface);
            }
        }
    }

    public void upLoadPic(String imgPath, int pos, UploadPicResult uploadPicsInterface) {
        ImageUploader imageUploader = new ImageUploader();
        imageUploader.setFilePath(imgPath, MyCommon.fixxs[0]);
        imageUploader.UpLoad(context);
        imageUploader.setOnUploadListener(new ImageUploader.OnUploadListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    String imageUrl = new JSONObject(result).getString("key");
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
        uploadPicsInterface.onSuccess(images.substring(0,images.length()-1));
    }

    public interface UploadPicResult {
        void onSuccess(String images);
        void onFail();
    }
}
