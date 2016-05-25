package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.DownLoaderDir;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.dao.DBManager;
import com.xilu.wybz.presenter.SaveWordPresenter;
import com.xilu.wybz.ui.IView.ISaveWordView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.ImageUtils;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.UploadPicUtil;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/13.
 */
public class SaveWordActivity extends ToolbarActivity implements ISaveWordView{
    @Bind(R.id.cb_isopen)
    CheckBox cb_isopen;
    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.iv_cover)
    SimpleDraweeView iv_cover;
    WorksData worksData;
    String coverPath;
    int sampleid;
    String lyricsid;
    SaveWordPresenter saveWordPresenter;
    public static void toSaveWordActivity(Context context, WorksData worksData){
        Intent intent = new Intent(context,SaveWordActivity.class);
        intent.putExtra("worksData",worksData);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_saveword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveWordPresenter = new SaveWordPresenter(context,this);
        saveWordPresenter.init();
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            worksData = (WorksData) bundle.getSerializable("worksData");
        }
        EventBus.getDefault().register(this);
        initEvent();
        initData();
    }
    private void initData(){
        //修改歌词的时候 还原数据
        if(worksData!=null){
            cb_isopen.setChecked(worksData.isOpen==1?true:false);
            if(!TextUtils.isEmpty(worksData.detail)){
                et_content.setText(worksData.detail);
            }
            if(!TextUtils.isEmpty(worksData.pic)){
                if(worksData.pic.startsWith("http"))
                    loadImage(worksData.pic,iv_cover);
                else if(new File(worksData.pic).exists())
                    loadImage("file:///"+worksData.pic,iv_cover);
            }
        }
    }
    private void initEvent(){
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 140) {
                    showMsg("最多可输入140个字");
                }
            }
        });
        cb_isopen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                worksData.setIsOpen(isChecked?1:0);
            }
        });
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
                    String imagePath = DownLoaderDir.coverPic + System.currentTimeMillis() + ".jpg";
                    Bitmap cameraBitmap = BitmapFactory.decodeFile(coverPath, bitmapOptions);
                    Bitmap bitmap = ImageUtils.rotaingImageView(degree, cameraBitmap);
                    ImageUtils.saveBitmap(bitmap, imagePath);
                    coverPath = imagePath;
                }

                loadImage("file://" + coverPath, iv_cover);
            } else {
                showMsg("图片读取失败！");
            }
        }
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
        showPd("正在保存中，请稍候...");
        UploadPicUtil uploadPicUtil = new UploadPicUtil();
        uploadPicUtil.uploadFile(context, coverPath, 1, "lyrcover", new UploadPicUtil.UploadPicResult() {
            @Override
            public void onSuccess(String imageUrl) {
                worksData.setPic(imageUrl);
                saveWordPresenter.saveLyrics(worksData,userId);
            }

            @Override
            public void onFail() {
                Log.e("onFail", "onFail");
                showMsg("上传封面失败！");
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
        switch (item.getItemId()){
            case R.id.menu_save:
                if(!TextUtils.isEmpty(coverPath)){
                    uploadCoverPic();
                }else{
                    showMsg("请先选择歌词的封面！");
                }
                break;
        }
        return true;
    }
    @Override
    public void saveWordSuccess(String result) {
        cancelPd();
        if (ParseUtils.checkCode(result)) {
            try {
                lyricsid = new JSONObject(result).getJSONObject("data").getJSONObject("info").getString("lyricsid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(1, worksData));
            EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(2, worksData));
            EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(3, worksData));
            EventBus.getDefault().post(new Event.SaveLyricsSuccessEvent(4, worksData));
        } else {
            ParseUtils.showMsg(context, result);
        }
    }
    @Override
    public void saveWordFail() {
        cancelPd();
        showMsg("保存失败");
    }

    public void onEventMainThread(Event.SaveLyricsSuccessEvent event) {
        if (event.getWhich() == 4) {
            new MaterialDialog.Builder(context)
                    .content("歌词保存成功！")
                    .positiveText("返回首页")
                    .canceledOnTouchOutside(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish();
                        }
                    }).negativeText("查看歌词")
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            Intent intent = new Intent(context, LyricsdisplayActivity.class);
//                            intent.putExtra("id", lyricsid);
//                            intent.putExtra("title", lyricsdisplayBean.getName());
//                            intent.putExtra("from", 1);//1 标识可以修改
//                            startActivity(intent);
//                            overridePendingTransition(R.anim.right_in_anim, R.anim.left_out_anim);
//                            finish();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
