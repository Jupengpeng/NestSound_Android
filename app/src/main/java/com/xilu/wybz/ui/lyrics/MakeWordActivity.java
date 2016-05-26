package com.xilu.wybz.ui.lyrics;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.dao.DBManager;
import com.xilu.wybz.presenter.MakeWordPresenter;
import com.xilu.wybz.ui.IView.IMakeWordView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.view.dialog.LyricsDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
/**
 * Created by June on 16/5/13.
 */
public class MakeWordActivity extends ToolbarActivity implements IMakeWordView {
    MakeWordPresenter makeWordPresenter;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_word)
    EditText etWord;
    WorksData worksData;
    String oldWorksData;
    LyricsDialog lyricsDialog;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makeword;
    }
    public static void toMakeWordActivity(Context context, WorksData worksData){
        Intent intent = new Intent(context,MakeWordActivity.class);
        intent.putExtra(KeySet.WORKS_DATA,worksData);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
        }
        makeWordPresenter = new MakeWordPresenter(context, this);
        makeWordPresenter.init();
    }
    public void initData(){
        etTitle.setText(worksData.getTitle());
        etWord.setText(worksData.getLyrics());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_next:
                String title = etTitle.getText().toString().trim();
                String lyrics = etWord.getText().toString().trim();
                if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(lyrics)) {
                    worksData.setTitle(title);
                    worksData.setLyrics(lyrics);
                    SaveWordActivity.toSaveWordActivity(context, worksData);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initView() {
        EventBus.getDefault().register(this);
        setTitle("");
        if(worksData!=null){
            initData();
        }else{
            worksData = new WorksData();
        }
        oldWorksData = new Gson().toJson(worksData);
    }

    @OnClick({R.id.ll_import, R.id.ll_thesaurus, R.id.ll_course})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_import:
                startActivity(ImportWordActivity.class);
                break;
            case R.id.ll_thesaurus:
                if (lyricsDialog == null) {
                    lyricsDialog = new LyricsDialog(this, etWord);
                }
                if (!lyricsDialog.isShowing()) {
                    lyricsDialog.showDialog();
                }
                break;
            case R.id.ll_course:
                startActivity(MakeCourseActivity.class);
                break;
        }
    }
    public void onEventMainThread(Event.ImportWordEvent event) {
        String title = etTitle.getText().toString().trim();
        String content = etWord.getText().toString().trim();
        if(!TextUtils.isEmpty(title)||!TextUtils.isEmpty(content)){
            new MaterialDialog.Builder(context)
                    .content("导入的歌词会覆盖掉当前内容？")
                    .positiveText("继续导入")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            importData(event.getWorksData());
                        }
                    }).negativeText("返回编辑")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    })
            .show();
        }else{
            importData(event.getWorksData());
        }
    }
    public void onEventMainThread(Event.UpdateLyricsData event) {
        worksData = event.getWorksData();
    }

    public void onEventMainThread(Event.SaveLyricsSuccessEvent event) {
        if (event.getWhich() == 3) {
            finish();
        }
    }
    //提示保存本地数据
    public void TipSaveLocalData() {
        String title = etTitle.getText().toString().trim();
        String content = etWord.getText().toString().trim();
        worksData.setTitle(title);
        worksData.setLyrics(content);
        if (!(title.length()==0&&content.length()==0)||!new Gson().toJson(worksData).equals(oldWorksData)) {
            new MaterialDialog.Builder(context)
                    .content("是否保存到本地？")
                    .positiveText("保存")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            PrefsUtil.putString(KeySet.LOCAL_LYRICS, new Gson().toJson(worksData), context);
                            finish();
                        }
                    }).negativeText("放弃")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            TipSaveLocalData();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void importData(WorksData worksData){
        this.worksData = worksData;
        initData();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
