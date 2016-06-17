package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.xilu.wybz.R;

/**
 * Created by Administrator on 2016/5/26.
 */
public class TestActivity extends AppCompatActivity {

    protected int getLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

    }
}
