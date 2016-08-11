package com.xilu.wybz.ui.msg;

import android.os.Bundle;
import android.view.View;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.SystemBarHelper;

import butterknife.OnClick;

/**
 * Created by June on 16/4/29.
 */
public class MsgActivity extends BasePlayMenuActivity {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home_msg;
    }
    public boolean canBack() {
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("消息");
    }
    @OnClick({R.id.ll_comment, R.id.ll_zambia, R.id.ll_collection, R.id.ll_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_comment:
                startActivity(MsgCommentActivity.class);
                break;
            case R.id.ll_zambia:
                startActivity(MsgZambiaActivity.class);
                break;
            case R.id.ll_collection:
                startActivity(MsgFavActivity.class);
                break;
            case R.id.ll_message:
                startActivity(MsgSystemActivity.class);
                break;
        }
    }
}
