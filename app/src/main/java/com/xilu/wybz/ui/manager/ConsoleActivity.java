package com.xilu.wybz.ui.manager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.preserve.ApplyPreserveActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/6/29.
 */
public class ConsoleActivity extends ToolbarActivity {

    public static final int RELEASE_SERVICE = 1;
    public static final int TEST_SERVICE = 2;
    public static final int TEMP_SERVICE = 3;

    @Bind(R.id.tv_release)
    TextView tvRelease;
    @Bind(R.id.cb_release)
    CheckBox cbRelease;
    @Bind(R.id.tv_test)
    TextView tvTest;
    @Bind(R.id.cb_test)
    CheckBox cbTest;
    @Bind(R.id.et_temp)
    EditText etTemp;
    @Bind(R.id.cb_temp)
    CheckBox cbTemp;





    @Override
    protected int getLayoutRes() {
        return R.layout.activity_manager_console;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }


    private void initView() {
        setTitle("控制台");

        String temp = PrefsUtil.getString("temp_host",this);

        tvRelease.setText(MyHttpClient.RELEASE_ROOT_URL);
        tvTest.setText(MyHttpClient.TEST_ROOT_URL);
        if (StringUtils.isNotBlank(temp)){
            etTemp.setText(temp);
        } else {
            etTemp.setText(MyHttpClient.TEMP_ROOT_URL);
        }


        String host = PrefsUtil.getString(PrefsUtil.DOMAIN,this);

        if (StringUtils.isBlank(host)){
            selectedService(RELEASE_SERVICE);
        } else {
            if (host.equalsIgnoreCase(MyHttpClient.RELEASE_ROOT_URL)){
                selectedService(RELEASE_SERVICE);
            } else if (host.equalsIgnoreCase(MyHttpClient.TEST_ROOT_URL)){
                selectedService(TEST_SERVICE);
            } else {
                selectedService(TEMP_SERVICE);
            }

        }


        cbRelease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedService(RELEASE_SERVICE);
                }
            }
        });
        cbTest.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedService(TEST_SERVICE);
                }
            }
        });
        cbTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectedService(TEMP_SERVICE);
                }
                PrefsUtil.putString("temp_host",etTemp.getText().toString(),getBaseContext());
            }
        });

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.root);
//        coordinatorLayout.findViewById();



        NestedScrollView scrollingView = (NestedScrollView) findViewById(R.id.nsv);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(scrollingView);
        bottomSheetBehavior.setPeekHeight(0); // 设置当关闭时 底部 的高度 app:behavior_peekHeight="50dp"
        //这里为蓝色的部分
        bottomSheetBehavior.setHideable(true);//设置当拉升到底部是否可以隐藏  app:behavior_hideable="true"
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//设置状态

        //回掉监听
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            public boolean hasRequest = true;
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                /**
                 *public static final int STATE_DRAGGING = 1;  //拖动
                 public static final int STATE_SETTLING = 2;//沉降中
                 public static final int STATE_EXPANDED = 3;//打开了
                 public static final int STATE_COLLAPSED = 4;//关闭了
                 public static final int STATE_HIDDEN = 5;//隐藏了
                 */
                Log.e("static", "---->state:" + newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.e("static", "slideOffset:" + slideOffset);
                if (!hasRequest && bottomSheetBehavior.getPeekHeight() == 0 && slideOffset > 0) {
                    hasRequest = true;
                    updateOffsets(bottomSheet);
                }
            }
        });

        TextView ext = (TextView) findViewById(R.id.tv_ext);
        ext.setText("WeChatPayUtils");
        ext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(content);
//
//                bottomSheetDialog.setContentView(R.layout.activity_manager_console);
//                bottomSheetDialog.show();

//                startActivity(MusicControllerActivity.class);
//                startActivity(PreservPersonInfoActivity.class);
                startActivity(ApplyPreserveActivity.class);


//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                WeChatPayUtils.register(content);
//                WeChatPayUtils.pay();
            }
        });
    }


    private void selectedService(int statu){

        switch (statu){
            case RELEASE_SERVICE:
                cbRelease.setChecked(true);
                cbTest.setChecked(false);
                cbTemp.setChecked(false);
                changeService(MyHttpClient.RELEASE_ROOT_URL);

                break;
            case TEST_SERVICE:
                cbRelease.setChecked(false);
                cbTest.setChecked(true);
                cbTemp.setChecked(false);
                changeService(MyHttpClient.TEST_ROOT_URL);
                break;
            case TEMP_SERVICE:
                cbRelease.setChecked(false);
                cbTest.setChecked(false);
                cbTemp.setChecked(true);

                changeService(etTemp.getText().toString());
                break;
        }

    }

    private void updateOffsets(View view) {

        // Manually invalidate the view and parent to make sure we get drawn pre-M
        if (Build.VERSION.SDK_INT < 23) {
            tickleInvalidationFlag(view);
            final ViewParent vp = view.getParent();
            if (vp instanceof View) {
                tickleInvalidationFlag((View) vp);
            }
        }
    }

    private static void tickleInvalidationFlag(View view) {
        final float y = ViewCompat.getTranslationY(view);
        ViewCompat.setTranslationY(view, y + 1);
        ViewCompat.setTranslationY(view, y);
    }

    private void changeService(String host){

        MyHttpClient.ROOT_URL = host;
        MyHttpClient.PRE_ROOT = host;
        MyHttpClient.BASE_URL = MyHttpClient.ROOT_URL;

        PrefsUtil.putString(PrefsUtil.DOMAIN,host,this);
        Log.d("url","BASE_URL:"+MyHttpClient.BASE_URL);
    }


    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("result", "ok");
        setResult(RESULT_OK, intent);

        super.finish();
    }
}
