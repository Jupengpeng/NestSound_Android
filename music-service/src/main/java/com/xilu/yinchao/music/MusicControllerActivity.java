package com.xilu.yinchao.music;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;

import com.xilu.yinchao.music.utils.LogHelper;

/**
 * Created by Administrator on 2016/9/11.
 */
public class MusicControllerActivity extends AppCompatActivity {

    private static final String TAG = LogHelper.makeLogTag(MusicControllerActivity.class);
    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;


    /**
     *
     */
    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
            LogHelper.d(TAG, "onPlaybackStateChanged changed", state);
//            updatePlaybackState(state);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            LogHelper.d(TAG, " onMetadataChanged");
            if (metadata != null) {
//                updateMediaDescription(metadata.getDescription());
//                updateDuration(metadata);
            }
        }
    };


    /**
     *
     */
    private final MediaBrowserCompat.ConnectionCallback mConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    LogHelper.d(TAG, "onConnected");
                    try {
                        connectToSession(mMediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                        LogHelper.e(TAG, e, "could not connect media controller");
                    }
                }
            };


    MediaBrowserCompat mMediaBrowser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_controller);

        mMediaBrowser = new MediaBrowserCompat(this,
                new ComponentName(this, YinchaoMusicService.class), mConnectionCallback, null);

        context = this;
    }

    Context context;

    /**
     *
     * @param token
     * @throws RemoteException
     */
    private void connectToSession(MediaSessionCompat.Token token) throws RemoteException {
        LogHelper.d(TAG,"connectToSession:",token.toString());

        MediaControllerCompat mediaController = new MediaControllerCompat(
                context, token);
        if (mediaController.getMetadata() == null) {
            finish();
            return;
        }
        setSupportMediaController(mediaController);
        mediaController.registerCallback(mCallback);





//        PlaybackStateCompat state = mediaController.getPlaybackState();
//        updatePlaybackState(state);
//        MediaMetadataCompat metadata = mediaController.getMetadata();
//        if (metadata != null) {
//            updateMediaDescription(metadata.getDescription());
//            updateDuration(metadata);
//        }
//        updateProgress();
//        if (state != null && (state.getState() == PlaybackStateCompat.STATE_PLAYING ||
//                state.getState() == PlaybackStateCompat.STATE_BUFFERING)) {
//            scheduleSeekbarUpdate();
//        }
    }



    @Override
    public void onStart() {
        super.onStart();
        if (mMediaBrowser != null) {
            mMediaBrowser.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMediaBrowser != null) {
            mMediaBrowser.disconnect();
        }
        if (getSupportMediaController() != null) {
            getSupportMediaController().unregisterCallback(mCallback);
        }
    }

}
