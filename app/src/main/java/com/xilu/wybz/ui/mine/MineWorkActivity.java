package com.xilu.wybz.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.xilu.wybz.R;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.fragment.WorksFragment;

/**
 * Created by hujunwei on 16/9/14.
 */
public class MineWorkActivity extends BasePlayMenuActivity {
    int type;
    private String[] MyTitles = new String[]{"我的歌曲", "我的歌词", "我的收藏", "灵感记录"};
    public static void toNewMyWorkActivity(Context context, int type) {
        Intent intent = new Intent(context, MineWorkActivity.class);
        intent.putExtra(KeySet.KEY_TYPE, type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_work;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideRight();
        initView();
    }

    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(KeySet.KEY_TYPE);
            if(type>4)type=1;
            setTitle(MyTitles[type-1]);
        }
        WorksFragment worksFragment = WorksFragment.newInstance(type);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, worksFragment).commit();
    }
}