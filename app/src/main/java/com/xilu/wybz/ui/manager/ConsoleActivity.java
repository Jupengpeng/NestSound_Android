package com.xilu.wybz.ui.manager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.WeChatPayUtils;

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



        TextView ext = (TextView) findViewById(R.id.tv_ext);
        ext.setText("WeChatPayUtils");
        ext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WeChatPayUtils.register(context);
                WeChatPayUtils.pay();
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

    private void changeService(String host){

        MyHttpClient.ROOT_URL = host;
        MyHttpClient.PRE_ROOT = host.substring(0,host.length()-1);
        MyHttpClient.BASE_URL = MyHttpClient.ROOT_URL+MyHttpClient.BASE_PATH;

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
