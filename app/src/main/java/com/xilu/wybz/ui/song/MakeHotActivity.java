package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.HotFragment;
import com.xilu.wybz.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MakeHotActivity extends ToolbarActivity {
    public static final String FLASH_TAG = "FLASH_TAG";
    private HotCatalog hotCatalog;
    private HotFragment hotFragment;
    String type = "";
    public boolean flash = false;
    public static void toMakeHotActivity(Context context, HotCatalog hotCatalog,boolean flash){
        Intent intent = new Intent(context,MakeHotActivity.class);
        intent.putExtra("hotCatalog",hotCatalog);
        intent.putExtra(FLASH_TAG,flash);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makehot;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }
    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            hotCatalog = (HotCatalog) bundle.getSerializable("hotCatalog");
            flash = bundle.getBoolean(FLASH_TAG,false);
        }
        if(hotCatalog==null)finish();
        EventBus.getDefault().register(this);
        MyApplication.mMainService.doRelease();
        if(StringUtil.isNotBlank(hotCatalog.categoryname))
            setTitle(hotCatalog.categoryname);
        else
            setTitle("原创伴奏");

        if(hotCatalog.categoryname.contains("最新")){
            type = "new";

        }else if(hotCatalog.categoryname.contains("最热")){
            type = "hot";
        }
        hotFragment = HotFragment.newInstance(type,hotCatalog.id,flash);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, hotFragment).commit();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event.PPStatusEvent event) {
        String from = event.getFrom();
        if(StringUtil.isBlank(from))return;
        if(from.equals(type+"_hot")) {
            switch (event.getStatus()) {
                case MyCommon.STARTED://开始
                    break;
                case MyCommon.PLAYED://播放
                    break;
                case MyCommon.PAUSED://暂停
                    break;
                case MyCommon.STOPPED://停止
                case MyCommon.COMPLETED://完成
                case MyCommon.END://释放
                case MyCommon.ERROR://出错
                case MyCommon.FAILED://获取数据失败
                    if(hotFragment!=null){
                        hotFragment.doStop();
                    }
                    break;
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭播放
        EventBus.getDefault().unregister(this);
        MyApplication.mMainService.doRelease();
    }

}
