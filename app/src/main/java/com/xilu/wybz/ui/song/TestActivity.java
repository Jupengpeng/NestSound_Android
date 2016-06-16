package com.xilu.wybz.ui.song;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.view.MoreWindow;
import com.xilu.wybz.view.SystemBarHelper;

/**
 * Created by Administrator on 2016/5/26.
 */
public class TestActivity extends AppCompatActivity {
    MoreWindow mMoreWindow;
    LinearLayout ll_main;
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        Button btn = (Button)findViewById(R.id.btn);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mMoreWindow) {
                    mMoreWindow = new MoreWindow(TestActivity.this);
                    mMoreWindow.init();
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mMoreWindow.setOverlapAnchor(false);
                    }
                }
                mMoreWindow.showMoreWindow(TestActivity.this, ll_main, null);
            }
        });

    }
}
