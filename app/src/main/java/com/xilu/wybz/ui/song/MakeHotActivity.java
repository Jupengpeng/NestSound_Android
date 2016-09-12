package com.xilu.wybz.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.HotBean;
import com.xilu.wybz.bean.HotCatalog;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.HotCatalogPresenter;
import com.xilu.wybz.ui.IView.IHotCatalogView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.HotFragment;
import com.xilu.wybz.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MakeHotActivity extends ToolbarActivity implements IHotCatalogView {
    public static final String FLASH_TAG = "FLASH_TAG";
    private HotCatalog hotCatalog;
    private HotFragment hotFragment;
    String type = "";
    String aid;
    public boolean flash = false;
    HotCatalogPresenter hotCatalogPresenter;
    public static void toMakeHotActivity(Context context, HotCatalog hotCatalog, boolean flash, String aid, String type) {
        Intent intent = new Intent(context, MakeHotActivity.class);
        intent.putExtra("hotCatalog", hotCatalog);
        intent.putExtra(KeySet.KEY_ID, aid);
        intent.putExtra(KeySet.KEY_TYPE, type);
        intent.putExtra(FLASH_TAG, flash);
        context.startActivity(intent);
    }

    public static void toMakeHotActivity(Context context, boolean flash) {
        Intent intent = new Intent(context, MakeHotActivity.class);
        intent.putExtra(FLASH_TAG, flash);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makehot;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyApplication.getInstance().mMainService == null) {
            MyApplication.getInstance().bindMainService();
        }
        initViews();
    }

    private void initViews() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            hotCatalog = (HotCatalog) bundle.getSerializable("hotCatalog");
            flash = bundle.getBoolean(FLASH_TAG, false);
            aid = bundle.getString(KeySet.KEY_ID);
            type = bundle.getString(KeySet.KEY_TYPE);
        }
//        if (hotCatalog == null) finish();
        EventBus.getDefault().register(this);
        if (MyApplication.getInstance().getMainService() != null)
            MyApplication.getInstance().mMainService.doRelease();
        setTitle("原创伴奏");

        hotCatalogPresenter = new HotCatalogPresenter(context,this);
        hotCatalogPresenter.loadData(1);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.PPStatusEvent event) {
        String from = event.getFrom();
        if (StringUtils.isBlank(from)) return;
        if (from.equals(type + "_hot")) {
            switch (event.getStatus()) {
                case MyCommon.END:
                case MyCommon.STOPPED://停止
                    if (hotFragment != null) {
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
        if (MyApplication.getInstance().getMainService() != null)
            MyApplication.getInstance().mMainService.doRelease();
    }

    @Override
    public void showHotCatalog(HotBean hotBean) {
        List<HotCatalog> list = hotBean.list;
        if(list!=null&&list.size()>0) {
            HotCatalog hotCatalog = list.get(0);
            type="new";
            hotFragment = HotFragment.newInstance(type, hotCatalog.id, flash, aid);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, hotFragment).commit();
        }
    }

    @Override
    public void loadFail() {

    }

    @Override
    public void loadNoMore() {

    }

    @Override
    public void loadNoData() {

    }

    @Override
    public void initView() {

    }
}
