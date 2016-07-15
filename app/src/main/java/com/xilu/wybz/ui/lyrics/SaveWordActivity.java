package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.SaveWordPresenter;
import com.xilu.wybz.ui.IView.ISaveWordView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.ImageUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.UploadFileUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/5/13.
 */
public class SaveWordActivity extends ToolbarActivity implements ISaveWordView {
    @Bind(R.id.cb_isopen)
    CheckBox cb_isopen;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.iv_cover)
    SimpleDraweeView iv_cover;
    WorksData worksData;
    String coverPath;
    boolean isAbleOnClick = true;
    SaveWordPresenter saveWordPresenter;

    public static void toSaveWordActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, SaveWordActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_saveword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveWordPresenter = new SaveWordPresenter(context, this);
        saveWordPresenter.init();
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
        }
        if(worksData.itemid==0)worksData.type=1;
        initEvent();
        initData();
    }

    private void initData() {
        //修改歌词的时候 还原数据
        if (worksData != null) {
            cb_isopen.setChecked(worksData.type == 1 ? true : false);
            if (StringUtil.isNotBlank(worksData.detail)) {
                et_content.setText(worksData.detail);
            }
            if (!TextUtils.isEmpty(worksData.pic)) {
                if (worksData.pic.startsWith("http"))
                    loadImage(worksData.pic, iv_cover);
                else if (new File(worksData.pic).exists()) {
                    loadImage("file:///" + worksData.pic, iv_cover);
                }
            }
        }
    }

    private void initEvent() {
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                worksData.setDetail(s.toString().trim());
                if (s.length() == 140) {
                    showMsg("最多可输入140个字");
                }
            }
        });
        cb_isopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                worksData.setType(cb_isopen.isChecked() ? 1 : 0);
            }
        });
    }

    @OnClick({R.id.iv_cover})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_cover:
                SystemUtils.openGallery(this);
                break;
        }
    }

    //上传封面图片
    public void uploadCoverPic() {
        isAbleOnClick = false;
        UploadFileUtil uploadPicUtil = new UploadFileUtil(context);
        uploadPicUtil.uploadFile(worksData.pic, MyCommon.fixxs[1], new UploadFileUtil.UploadResult() {
            @Override
            public void onSuccess(String imageUrl) {
                isAbleOnClick = true;
                worksData.setPic(imageUrl);
                if(materialDialog!=null&&materialDialog.isShowing()) {
                    saveWordPresenter.saveLyrics(worksData);
                }
            }
            @Override
            public void onFail() {
                isAbleOnClick = true;
                cancelPd();
                showMsg("封面上传失败！");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_makeword, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                //先检查歌词的描述
                if (StringUtil.isBlank(worksData.detail)) {
                    showMsg("请先添加歌词描述！");
                    return true;
                }
                if (StringUtil.isBlank(worksData.pic)) {
                    showMsg("请先选择歌词的封面！");
                    return true;
                }
                if (new File(worksData.pic).exists()) {
                    showPd("正在发布中，请稍候...");
                    if(isAbleOnClick)
                    uploadCoverPic();
                } else {
                    showPd("正在修改中，请稍候...");
                    saveWordPresenter.saveLyrics(worksData);
                }
                if(materialDialog!=null){
                    materialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            saveWordPresenter.cancelRequest();
                        }
                    });
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveWordSuccess(String result) {
        cancelPd();
        worksData.createdate = System.currentTimeMillis();
        //新增或者更新
        EventBus.getDefault().post(new Event.UpdataWorksList(worksData, 2, worksData.itemid==0?0:2));
        if(worksData.itemid==0) {
            try {
                String shareurl = new JSONObject(result).getString("shareurl");
                int itemid = new JSONObject(result).getInt("itemid");
                worksData.setShareurl(shareurl + "?id=" + itemid);
                worksData.setItemid(itemid);
                ShareActivity.toShareActivity(context, worksData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        PrefsUtil.putString(KeySet.LOCAL_LYRICS, "", context);
        EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(1, worksData));//新建歌词页面
        EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(2, worksData));//歌词展示页面
        finish();
    }

    @Override
    public void saveWordFail() {
        cancelPd();
        showMsg("发布失败");
    }

    @Override
    public void onFinish() {
        cancelPd();
    }

    public void closeActivity() {
        EventBus.getDefault().post(new Event.UpdateLyricsData(worksData));
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyCommon.requestCode_photo && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);
                if (TextUtils.isEmpty(thePath)) {
                    coverPath = ImageUtils.getPath(this, uri);
                } else {
                    coverPath = thePath;
                }
                if (TextUtils.isEmpty(coverPath)) {
                    showMsg("图片读取失败~");
                    return;
                }
                File picture = new File(coverPath);
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                //获取图片的旋转角度
                int degree = ImageUtils.readPictureDegree(picture.getAbsolutePath());
                if (degree > 0) {//大于0的时候需要调整角度
                    String imagePath = FileDir.coverPic + System.currentTimeMillis() + ".jpg";
                    Bitmap cameraBitmap = BitmapFactory.decodeFile(coverPath, bitmapOptions);
                    Bitmap bitmap = ImageUtils.rotaingImageView(degree, cameraBitmap);
                    ImageUtils.saveBitmap(bitmap, imagePath);
                    coverPath = imagePath;

                }
                loadImage("file://" + coverPath, iv_cover);
                worksData.setPic(coverPath);
            } else {
                showMsg("图片读取失败！");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(saveWordPresenter!=null)
            saveWordPresenter.cancelRequest();
    }
}
