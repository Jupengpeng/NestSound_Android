package com.xilu.wybz.ui.view.menuitem;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.xilu.wybz.R;
import com.xilu.wybz.view.AnimImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by June on 16/5/4.
 */
public class PlayActionProvider extends ActionProvider {
    ImageView ivPlay;
    private Context context;
    private View view;
    private AnimImageView animImageView;
    private List<Integer> resourceIdList;
    private View.OnClickListener mOnClickListener;

    public PlayActionProvider(Context context) {
        super(context);
        this.context = context;
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
    }

    @Override
    public View onCreateActionView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.menu_play, null);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        initAnimal();
        view.setOnClickListener(mOnClickListener);
        return view;
    }
    private void initAnimal(){
        if (animImageView == null)
            animImageView = new AnimImageView();
        animImageView.setAnimation(ivPlay, resourceIdList);
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
    public void startAnimal(){
        animImageView.start(true, 70);
    }
    public void stopAnimal(){
        animImageView.stop();
        ivPlay.setImageResource(R.drawable.ic_menu_play_4);
    }
}
