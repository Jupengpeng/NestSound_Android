package com.xilu.wybz.ui.mine;

import android.os.Bundle;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;

/**
 * Created by Administrator on 2016/5/24.
 */
public class DraftActivity extends ToolbarActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_draft;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }



    private void initView() {

        setTitle("草稿箱");
    }







}
