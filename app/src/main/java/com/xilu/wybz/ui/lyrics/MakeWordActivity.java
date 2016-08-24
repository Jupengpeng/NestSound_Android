package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.LyricsDraftBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.DraftPresenter;
import com.xilu.wybz.presenter.MakeWordPresenter;
import com.xilu.wybz.ui.IView.IMakeWordView;
import com.xilu.wybz.ui.IView.ISimpleView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.LyricsDraftUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.ToastUtils;
import com.xilu.wybz.view.dialog.LyricsDialog;
import com.xilu.wybz.view.dialog.MakeWordTipDialog;
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
public class MakeWordActivity extends ToolbarActivity implements IMakeWordView,ISimpleView {
    MakeWordPresenter makeWordPresenter;
    DraftPresenter draftPresenter;
    @Bind(R.id.et_title)
    EditText etTitle;
    @Bind(R.id.et_word)
    EditText etWord;
    WorksData worksData;
    String oldWorksData;
    LyricsDialog lyricsDialog;
    String aid;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_makeword;
    }

    public static void toMakeWordActivity(Context context) {
        Intent intent = new Intent(context, MakeWordActivity.class);
        context.startActivity(intent);
    }
    public static void toMakeWordActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, MakeWordActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        context.startActivity(intent);
    }
    public static void toMakeWordActivity(Context context, WorksData worksData,String aid) {
        Intent intent = new Intent(context, MakeWordActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        intent.putExtra(KeySet.KEY_ID, aid);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("flag","onCreate");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
            aid =  bundle.getString(KeySet.KEY_ID,"");
        }
        draftPresenter  = new DraftPresenter(context,this);
        makeWordPresenter = new MakeWordPresenter(context, this);
        makeWordPresenter.init();

        if(!PrefsUtil.getBoolean("isMakeWordTip",context)){
            MakeWordTipDialog wordTipDialog = new MakeWordTipDialog(context);
            wordTipDialog.showDialog();
            PrefsUtil.putBoolean("isMakeWordTip",true,context);
        }
    }

    public void initData() {
        etTitle.setText(worksData.getTitle());
        if (StringUtils.isNotBlank(worksData.getLyrics())) {
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
            String text;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                text = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                worksData.lyrics = s.toString().trim();
                if (!text.equals(s.toString())){
                    etWord.setText(s.toString());
                    etWord.setSelection(etWord.getText().length());
                }
                PrefsUtil.putString(KeySet.LOCAL_LYRICS, new Gson().toJson(worksData), context);
            }
        });
        etWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN){
                    String text = etWord.getText().toString()+"\n";
                    etWord.setText(text);
                    etWord.setSelection(text.length());
                    return true;
                }
                return false;
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
        switch (item.getItemId()) {
            case R.id.menu_next:
                String title = etTitle.getText().toString().trim();
                String lyrics = etWord.getText().toString().trim();
                if (TextUtils.isEmpty(title)){
                    ToastUtils.toast(this,"请填写歌词名称");
                    return true;
                }
                if (TextUtils.isEmpty(lyrics)) {
                    ToastUtils.toast(this,"请填写歌词");
                    return true;
                }
                worksData.setTitle(title);
                worksData.setLyrics(lyrics);
                if(StringUtils.isNotBlank(aid)){
                    SaveWordActivity.toSaveWordActivity(context, worksData, aid);
                }else{
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
        if (worksData != null) {
            initData();
        } else {
            worksData = new WorksData();
        }
        oldWorksData = new Gson().toJson(worksData);
    }

    public void showLyrics() {
        if (lyricsDialog == null) {
            lyricsDialog = new LyricsDialog(this, etWord);
        }
        if (!lyricsDialog.isShowing()) {
            lyricsDialog.showDialog();
        }
    }

    @Override
    public void onSuccess(int id, String message) {
        cancelPd();
        ToastUtils.toast(this,message);
        PrefsUtil.putString(KeySet.LOCAL_LYRICS, "", context);
        finish();
    }

    @Override
    public void onError(int id, String error) {
        cancelPd();
        ToastUtils.toast(this,error);
    }

    LyricsMenuPopWindow window;

    @OnClick({R.id.ll_import, R.id.ll_thesaurus, R.id.ll_course})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_import:
                startActivity(DraftLyricsActivity.class);
//                startActivity(MakeWordByTempleateActivity.class);
                break;
            case R.id.ll_thesaurus:
                startActivity(LyricsTemplateListActivity.class);
                break;
            case R.id.ll_course:
                if (window == null) window = new LyricsMenuPopWindow(this);
                int y = (int) (49.4 * DensityUtil.getScreenDensity(this));

                LinearLayout layout= (LinearLayout)findViewById(R.id.ll_course);
                int h = DensityUtil.getScreenH(this);
                int n = DensityUtil.getNavigationBarHeight(this);
                int[] local = new int[2];
                layout.getLocationOnScreen(local);
                window.showAtLocation(etTitle, Gravity.BOTTOM | Gravity.RIGHT, 0, h+n-local[1]);

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ImportWordEvent event) {
        String title = etTitle.getText().toString().trim();
        String content = etWord.getText().toString().trim();
        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(content)) {
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
        } else {
            importData(event.getWorksData());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateLyricsData event) {
        worksData = event.getWorksData();
//        oldWorksData = new Gson().toJson(worksData);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.SaveLyricsSuccessEvent event) {
        if (event.getWhich() == 1) {
            finish();
        }
    }

    //提示保存本地数据
    public void tipSaveLocalData() {
        if (StringUtils.isBlank(worksData.title) && StringUtils.isBlank(worksData.lyrics)) {
            finish();
            return;
        }
        String newWorksData = new Gson().toJson(worksData);

        if (!newWorksData.equals(oldWorksData)) {
            new MaterialDialog.Builder(context)
                    .content("是否保存到草稿箱？")
                    .positiveText("保存")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            LyricsDraftBean bean = new LyricsDraftBean();
                            bean.id = LyricsDraftUtils.getId();
                            bean.title = worksData.title;
                            bean.draftdesc = worksData.detail;
                            bean.content = worksData.lyrics.replaceAll("[\\t\\n\\r]","\n");
                            bean.createtime = String.valueOf(System.currentTimeMillis());

//                            LyricsDraftUtils.save(bean);
                            draftPresenter.saveDraft(bean);
                            showPd("正在保存中");

//                            PrefsUtil.putString(KeySet.LOCAL_LYRICS, "", context);
//                            finish();
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
            PrefsUtil.putString(KeySet.LOCAL_LYRICS, "", context);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            tipSaveLocalData();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void importData(WorksData worksData) {
        this.worksData = worksData;
        oldWorksData = new Gson().toJson(worksData);
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("flag","onDestroy");
        EventBus.getDefault().unregister(this);
        if (makeWordPresenter != null)
            makeWordPresenter.cancelRequest();
    }
}
