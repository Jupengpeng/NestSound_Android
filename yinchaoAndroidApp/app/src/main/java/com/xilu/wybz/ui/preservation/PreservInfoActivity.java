package com.xilu.wybz.ui.preservation;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.ToolbarActivity;

/**
 * Created by Administrator on 2016/9/14.
 */
public class PreservInfoActivity extends ToolbarActivity{

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_preserv_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("保全单信息");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
