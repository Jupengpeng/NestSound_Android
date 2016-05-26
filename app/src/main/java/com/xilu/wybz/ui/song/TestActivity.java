package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.view.SurfaceView;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.ToolbarActivity;

/**
 * Created by Administrator on 2016/5/26.
 */
public class TestActivity extends ToolbarActivity {

    SurfaceView view;
    WaveSurfaceHelper helper;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = (SurfaceView)findViewById(R.id.surface);

        helper = new WaveSurfaceHelper(view);



    }
}
