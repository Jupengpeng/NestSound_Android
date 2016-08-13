package com.xilu.wybz.ui.lyrics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.xilu.wybz.R;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.dialog.LyricsDialog;

/**
 * Created by Administrator on 2016/8/9.
 */
public class LyricsMenuPopWindow extends PopupWindow {

    public Activity act;

    public LyricsDialog lyricsDialog;

    public LyricsMenuPopWindow(Activity context) {
        super(context);
        act = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mMenuView = inflater.inflate(R.layout.popup_lyrics_menu, null);

        LinearLayout ll_main = (LinearLayout) mMenuView.findViewById(R.id.ll_main);
        LinearLayout ll_yunjiao = (LinearLayout) mMenuView.findViewById(R.id.lyrics_yunjiao);
        LinearLayout ll_repertory = (LinearLayout) mMenuView.findViewById(R.id.lyrics_repertory);


        ll_yunjiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act,LyricsRhymeActivity.class);
                act.startActivity(intent);
                dismiss();
            }
        });

        ll_repertory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MakeWordActivity)act).showLyrics();
                dismiss();
            }
        });

        if (Build.VERSION.SDK_INT > 20) {
            ll_main.setElevation(1.0f);
            ll_main.setTranslationZ(30f);
            ll_main.setTranslationY(-1f);
            ll_main.setTranslationX(1f);
        }

        int w = DensityUtil.getScreenW(act);
        float d = DensityUtil.getScreenDensity(act);

        setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
//        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth((int)((w)/3+0.5));
        //设置SelectPicPopupWindow弹出窗体的高
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        setAnimationStyle(android.R.style.Widget_Material_Light_PopupMenu);
        //实例化一个ColorDrawable颜色为半透明
        setBackgroundDrawable(new BitmapDrawable());
    }

}
