package com.xilu.wybz.ui.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.AppInfoUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/7/4.
 */
public class AboutMessageActivity extends ToolbarActivity {

    @Bind(R.id.tv_version)
    TextView tvVersion;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("关于我们");
        String versionName = AppInfoUtil.getVersionName(this);
//        int versionCode = AppInfoUtil.getVersionCode(this);
        tvVersion.setText("版本:"+versionName);
    }

    @OnClick(R.id.ll_feedback)
    public void feedback(){
        startActivity(SettingFeedActivity.class);
    }

    @OnClick(R.id.ll_to_comment)
    public void comment(){
        Uri uri = Uri.parse("market://details?id=com.xilu.wybz");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

}
