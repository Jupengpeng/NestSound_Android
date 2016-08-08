package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.MakeWordPresenter;
import com.xilu.wybz.ui.IView.IMakeWordView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.dialog.LyricsDialog;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.OnClick;
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
        if(StringUtil.isNotBlank(worksData.getLyrics())) {
            etWord.setText(worksData.getLyrics());
            etWord.requestFocus();
            etWord.setSelection(worksData.getLyrics().length());
        }
        etTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                worksData.title = s.toString().trim();
                PrefsUtil.putString(KeySet.LOCAL_LYRICS, new Gson().toJson(worksData), context);
            }
        });
        etWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                worksData.lyrics = s.toString().trim();
                PrefsUtil.putString(KeySet.LOCAL_LYRICS, new Gson().toJson(worksData), context);
            }
        });
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
                return true;
            case android.R.id.home:
                tipSaveLocalData();
                return true;
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
//                startActivity(MakeWordByTempleateActivity.class);
                break;
            case R.id.ll_thesaurus:
                startActivity(LyricsTemplateListActivity.class);
//                if (lyricsDialog == null) {
//                    lyricsDialog = new LyricsDialog(this, etWord);
//                }
//                if (!lyricsDialog.isShowing()) {
//                    lyricsDialog.showDialog();
//                }
                break;
            case R.id.ll_course:
                startActivity(MakeCourseActivity.class);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventMainThread(Event.ImportWordEvent event) {
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
    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventMainThread(Event.UpdateLyricsData event) {
        worksData = event.getWorksData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) public void onEventMainThread(Event.SaveLyricsSuccessEvent event) {
        if (event.getWhich() == 1) {
            finish();
        }
    }
    //提示保存本地数据
    public void tipSaveLocalData() {
        if(StringUtil.isBlank(worksData.title)&&StringUtil.isBlank(worksData.lyrics)) {
            finish();
            return;
        }
        if (!new Gson().toJson(worksData).equals(oldWorksData)) {
            new MaterialDialog.Builder(context)
                    .content("是否保存到本地？")
                    .positiveText("保存")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).negativeText("放弃")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            PrefsUtil.putString(KeySet.LOCAL_LYRICS, "", context);
                            finish();
                        }
                    })
                    .show();
        } else {
            finish();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tipSaveLocalData();
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
        if(makeWordPresenter!=null)
            makeWordPresenter.cancelRequest();
    }
}
