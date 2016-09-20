package com.xilu.yinchao.music;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.util.Log;

import com.xilu.yinchao.music.utils.LogHelper;

import java.util.List;

/**
 * Created by Administrator on 2016/9/11.
 */
public class YinchaoMusicService extends MediaBrowserServiceCompat{

    private static final String TAG = LogHelper.makeLogTag(YinchaoMusicService.class);

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {

        LogHelper.d(TAG,"onGetRoot",clientPackageName,"-",clientUid);
        Log.e("ycms","onGetRoot");
        return new BrowserRoot("root",null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        LogHelper.d(TAG,"onGetRoot",parentId,"-",result);


    }
}
