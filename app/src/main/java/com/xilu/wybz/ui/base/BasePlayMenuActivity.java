package com.xilu.wybz.ui.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.xilu.wybz.R;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.AnimImageView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 16/5/4.
 */
public abstract class BasePlayMenuActivity extends ToolbarActivity {
    protected RelativeLayout rl_right;
    private ImageView ivPlay;
    private AnimImageView animImageView;
    private List<Integer> resourceIdList;
    protected LinearLayout ll_search;
    protected boolean visible =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        rl_right = (RelativeLayout) findViewById(R.id.rl_right);
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        ivPlay = (ImageView) findViewById(R.id.iv_right);
        visible = true;
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
        if(animImageView!=null)
        animImageView.stop();
        ivPlay.setImageResource(R.drawable.ic_menu_play_4);
    }

    public void onPlayMenuClick() {
        int playId = PrefsUtil.getInt("playId", context);
        String playFrom = PrefsUtil.getString("playFrom", context);
        String idData = PrefsUtil.getString(playFrom,context);
        if(StringUtil.isNotBlank(idData)){
            String ids[] = idData.split(",");
            for(int i=0;i<ids.length;i++){
                MyApplication.ids.add(Integer.valueOf(ids[i]));
            }
        }else{
            MyApplication.ids.add(playId);
        }
        if (playId>0) {
            PlayAudioActivity.toPlayAudioActivity(context, PrefsUtil.getInt("playId", context),
                    PrefsUtil.getString("playGedanId", context),
                    playFrom);
        }
    }
    protected void hideRight(){
        visible = false;
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
    public void onResume() {
        super.onResume();
        if(visible) {
            if (PlayMediaInstance.getInstance().status == 3) {
                startAnimal();
            } else {
                stopAnimal();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Event.PPStatusEvent event) {
        if(visible) {
            switch (event.getStatus()) {
                case MyCommon.PP_START://开始
                case MyCommon.PP_PLAY://播放
                    startAnimal();
                    break;
                case MyCommon.PP_STOP://停止
                case MyCommon.PP_PAUSE://暂停
                    stopAnimal();
                    break;
            }
        }
    }
}
