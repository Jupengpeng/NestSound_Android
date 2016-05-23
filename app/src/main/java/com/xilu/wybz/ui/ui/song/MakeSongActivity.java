package com.xilu.wybz.ui.ui.song;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.RecordInstance;
import com.xilu.wybz.dao.DBManager;
import com.xilu.wybz.ui.base.ToolbarActivity;

import java.util.Timer;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/5/19.
 */
public class MakeSongActivity extends ToolbarActivity {
    @Bind(R.id.ll_main)
    LinearLayout llMain;
    @Bind(R.id.et_title)
    TextView etTitle;
    @Bind(R.id.et_word)
    TextView etWord;
    @Bind(R.id.make_sv_wave)
    SurfaceView makeSvWave;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_alltime)
    TextView tvAlltime;
    TemplateBean templateBean;
    WorksData worksData;
    long startRecordTime, startPlayTime, allTime;
    Timer recordTimer, playTimer;
    String RECORD_TAG;
    boolean isQc; //是不是清唱
    DBManager dbManager;
    public static void ToMakeSongActivity(Context context, TemplateBean templateBean) {
        Intent intent = new Intent(context, MakeSongActivity.class);
        intent.putExtra("templateBean", templateBean);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makesong;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setTitle("");
        RecordInstance.getInstance().setSurfaceView(makeSvWave);
        llMain.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            templateBean = (TemplateBean) bundle.getSerializable("templateBean");
        }
        if (templateBean == null) isQc = true;
    }
    @OnClick({R.id.rl_import, R.id.rl_play, R.id.rl_record, R.id.rl_restart, R.id.rl_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_import:
                break;
            case R.id.rl_play:
                break;
            case R.id.rl_record:
                break;
            case R.id.rl_restart:
                break;
            case R.id.rl_edit:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
