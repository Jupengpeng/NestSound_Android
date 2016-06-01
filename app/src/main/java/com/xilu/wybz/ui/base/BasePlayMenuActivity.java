package com.xilu.wybz.ui.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.ui.song.SearchHotActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.AnimImageView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by June on 16/5/4.
 */
public abstract class BasePlayMenuActivity extends ToolbarActivity {
    protected RelativeLayout rl_right;
    protected RelativeLayout app_bar_layout;
    private ImageView ivPlay;
    private AnimImageView animImageView;
    private List<Integer> resourceIdList;
    protected LinearLayout ll_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        rl_right = (RelativeLayout) findViewById(R.id.rl_right);
        app_bar_layout = (RelativeLayout) findViewById(R.id.app_bar_layout);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ivPlay = (ImageView) findViewById(R.id.iv_right);
        resourceIdList = new ArrayList<>();
        resourceIdList.add(R.drawable.ic_menu_play_3);
        resourceIdList.add(R.drawable.ic_menu_play_4);
        resourceIdList.add(R.drawable.ic_menu_play_5);
        resourceIdList.add(R.drawable.ic_menu_play_6);
        resourceIdList.add(R.drawable.ic_menu_play_7);
        resourceIdList.add(R.drawable.ic_menu_play_8);
        resourceIdList.add(R.drawable.ic_menu_play_13);
        resourceIdList.add(R.drawable.ic_menu_play_14);
        resourceIdList.add(R.drawable.ic_menu_play_15);
        resourceIdList.add(R.drawable.ic_menu_play_16);
        resourceIdList.add(R.drawable.ic_menu_play_17);
        resourceIdList.add(R.drawable.ic_menu_play_18);
        initAnimal();
        if(!(this instanceof SearchHotActivity)) {
            rl_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlayMenuClick();
                }
            });
        }
    }

    private void initAnimal() {
        if (animImageView == null)
            animImageView = new AnimImageView();
        animImageView.setAnimation(ivPlay, resourceIdList);
    }

    public void startAnimal() {
        animImageView.start(true, 70);
    }

    public void stopAnimal() {
        animImageView.stop();
        ivPlay.setImageResource(R.drawable.ic_menu_play_4);
    }

    public void onPlayMenuClick() {
        int playId = PrefsUtil.getInt("playId", context);
        if (playId>0) {
            PlayAudioActivity.toPlayAudioActivity(context, PrefsUtil.getInt("playId", context),
                    PrefsUtil.getString("playGedanId", context),
                    PrefsUtil.getString("playFrom", context), PrefsUtil.getInt("playdPos", context));
        }
    }
    protected void hideRight(){
        rl_right.setVisibility(View.GONE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (PlayMediaInstance.getInstance().status == 3) {
            stopAnimal();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PlayMediaInstance.getInstance().status == 3) {
            startAnimal();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Event.PPStatusEvent event) {
        switch (event.getStatus()) {
            case 1://开始
                startAnimal();
                break;
            case 2://停止
                stopAnimal();
                break;
            case 3://播放
                startAnimal();
                break;
            case 4://暂停
                stopAnimal();
                break;
        }
    }
}
