package com.xilu.wybz.ui.lyrics;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import com.xilu.wybz.utils.AppConstant;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.GalleryUtils;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
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
    EditText etContent;
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    WorksData worksData;
    String coverPath;
    boolean isAbleOnClick = true;
    String aid;
    int width;
    SaveWordPresenter saveWordPresenter;

    public static void toSaveWordActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, SaveWordActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        context.startActivity(intent);
    }

    public static void toSaveWordActivity(Context context, WorksData worksData, String aid) {
        Intent intent = new Intent(context, SaveWordActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        intent.putExtra(KeySet.KEY_ID, aid);
        context.startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KeySet.WORKS_DATA, worksData);
        outState.putString(KeySet.KEY_ID, aid);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        worksData = (WorksData) savedInstanceState.getSerializable(KeySet.WORKS_DATA);
        aid = savedInstanceState.getString(KeySet.KEY_ID, "");
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
            aid = bundle.getString(KeySet.KEY_ID, "");
        }
        width = DensityUtil.dip2px(context, 60);
        if (StringUtils.isBlank(worksData.itemid)) worksData.type = 1;
        initEvent();
        initData();
    }

    private void initData() {
        //修改歌词的时候 还原数据
        if (worksData != null) {
            cb_isopen.setChecked(worksData.type == 1 ? true : false);
            if (StringUtils.isNotBlank(worksData.detail)) {
                etContent.setText(worksData.detail);
            }
            if (!TextUtils.isEmpty(worksData.pic)) {
                if (worksData.pic.startsWith("http"))
                    ImageLoadUtil.loadImage(worksData.pic, ivCover, width, width);
                else if (new File(worksData.pic).exists()) {
                    ImageLoadUtil.loadImage("file:///" + worksData.pic, ivCover, width, width);
                }
            }
        }
    }

    private void initEvent() {
        etContent.addTextChangedListener(new TextWatcher() {
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
                GalleryUtils.getInstance().selectPicture(this);
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
                if (materialDialog != null && materialDialog.isShowing()) {
                    saveWordPresenter.saveLyrics(worksData, aid);
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
//                if (StringUtils.isBlank(worksData.detail)) {
//                    showMsg("请先添加歌词描述！");
//                    return true;
//                }
                if (StringUtils.isBlank(worksData.pic)) {
                    showMsg("请先选择歌词的封面！");
                    return true;
                }
                if (new File(worksData.pic).exists()) {
                    showPd("正在发布中，请稍候...");
                    if (isAbleOnClick)
                        uploadCoverPic();
                } else {
                    showPd("正在修改中，请稍候...");
                    saveWordPresenter.saveLyrics(worksData, aid);
                }
                if (materialDialog != null) {
                    materialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            saveWordPresenter.cancelRequest();
                        }
                    });
                }
                return true;
            case android.R.id.home:
                closeActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveWordSuccess(String result) {
        if (isDestroy) return;
        cancelPd();
        EventBus.getDefault().post(new Event.UpdataWorksList(worksData, 1, StringUtils.isBlank(worksData.itemid) ? 0 : 2));
        if (StringUtils.isBlank(worksData.itemid) && StringUtils.isBlank(aid)) {//aid 存在 服务端不返回这两个值
            try {
                String shareurl = new JSONObject(result).getString("shareurl");
                String itemid = new JSONObject(result).getString("itemid");
                worksData.setShareurl(shareurl + "?id=" + itemid);
                worksData.setItemid(itemid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        worksData.createdate = System.currentTimeMillis();
        //新增或者更新
        if (StringUtils.isNotBlank(aid)) {
            EventBus.getDefault().post(new Event.AttendMatchSuccessEvent());
        }
        PrefsUtil.putString(KeySet.LOCAL_LYRICS, "", context);
        EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(1, worksData));//新建歌词页面
        EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(2, worksData));//歌词展示页面
        if (StringUtils.isBlank(aid))//参加活动的作品 由于服务端没有返回分享地址 故不能跳转
            worksData.type = 2;
            ShareActivity.toShareActivity(context, worksData);
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
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (null == data) {
            return;
        }
        Uri uri = null;
        if (requestCode == AppConstant.KITKAT_LESS) {
            uri = data.getData();
            Log.d("tag", "uri=" + uri);
            // 调用裁剪方法
            if (!new File(FileDir.coverPic).exists()) new File(FileDir.coverPic).mkdirs();
            coverPath = FileDir.coverPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(coverPath));
            GalleryUtils.getInstance().cropPicture(this, uri, imgUri, 1, 1, 750, 750);
        } else if (requestCode == AppConstant.KITKAT_ABOVE) {
            uri = data.getData();
            Log.d("tag", "uri=" + uri);
            // 先将这个uri转换为path，然后再转换为uri
            String thePath = GalleryUtils.getInstance().getPath(this, uri);
            if (!new File(FileDir.coverPic).exists()) new File(FileDir.coverPic).mkdirs();
            coverPath = FileDir.coverPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(coverPath));
            GalleryUtils.getInstance().cropPicture(this,
                    Uri.fromFile(new File(thePath)), imgUri, 1, 1, 750, 750);
        } else if (requestCode == AppConstant.INTENT_CROP) {
            if (new File(coverPath).exists()) {
                ImageLoadUtil.loadImage(context, new File(coverPath), ivCover, width, width);
                worksData.setPic(coverPath);
            } else {
                showMsg("裁切失败");
            }
        }
    }

}
