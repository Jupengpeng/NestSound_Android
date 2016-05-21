package com.xilu.wybz.ui.base;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.AnimImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by June on 16/5/4.
 */
public abstract class BasePlayMenuActivity extends ToolbarActivity {
    private RelativeLayout rl_right;
    protected RelativeLayout app_bar_layout;
    private ImageView ivPlay;
    private AnimImageView animImageView;
    private List<Integer> resourceIdList;
    protected LinearLayout ll_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        rl_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayMenuClick();
            }
        });
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
        String playId = PrefsUtil.getString("playId", context);
        if (!TextUtils.isEmpty(playId)) {
            PlayAudioActivity.toPlayAudioActivity(context, PrefsUtil.getString("playId", context),
                    PrefsUtil.getString("playGedanId", context),
                    PrefsUtil.getString("playFrom", context), PrefsUtil.getInt("playdPos", context));
        }
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
}
