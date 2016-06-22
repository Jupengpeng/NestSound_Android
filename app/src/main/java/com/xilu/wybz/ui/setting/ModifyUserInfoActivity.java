package com.xilu.wybz.ui.setting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.ModifyUserInfoPresenter;
import com.xilu.wybz.ui.IView.IModifyUserInfoView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.UploadFileUtil;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import java.io.File;
import java.io.FileOutputStream;
import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by June on 16/5/13.
 */
public class ModifyUserInfoActivity extends ToolbarActivity implements IModifyUserInfoView{
    @Bind(R.id.tv_username)
    TextView tv_username;
    @Bind(R.id.iv_head)
    SimpleDraweeView iv_head;
    @Bind(R.id.tv_usersign)
    TextView tv_usersign;
    @Bind(R.id.tv_gender)
    TextView tv_gender;
    @Bind(R.id.tv_birthday)
    TextView tv_birthday;
    int year, month, day;
    UserBean userBean;
    DatePickerDialog datePickerDialog;
    int type;//0 修改昵称 1 修改签名
    String content;
    String titles[] = new String[]{"修改昵称", "修改签名", "修改性别", "修改生日"};
    String genders[] = new String[]{"男", "女"};
    int maxLengths[] = new int[]{16, 30};
    String headPath;

    ModifyUserInfoPresenter modifyUserInfoPresenter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_modify_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modifyUserInfoPresenter = new ModifyUserInfoPresenter(this,this);
        modifyUserInfoPresenter.init();
    }

    @Override
    public void modifyUserInfoSuccess() {
        showMsg("修改成功");
        initUserInfo();

    }

    @Override
    public void modifyUserInfoFail() {

    }

    @Override
    public void initView() {
        setTitle("完善个人信息");
        userBean = PrefsUtil.getUserInfo(context);
        initUserInfo();
    }
    public void initUserInfo() {
        tv_username.setText(userBean.name);
        tv_usersign.setText(userBean.descr);
        tv_birthday.setText(userBean.birthday);
        tv_gender.setText(genders[userBean.sex]);
        if(StringUtil.isNotBlank(userBean.headurl)){
            if(!userBean.headurl.startsWith("http")){
                userBean.headurl = MyHttpClient.QINIU_URL+userBean.headurl;
            }
            loadImage(userBean.headurl, iv_head);
        }
        //通知我的个人主页更新
        PrefsUtil.saveUserInfo(context,userBean);
        EventBus.getDefault().post(new Event.UpdateUserInfo());
    }

    @OnClick({R.id.ll_userhead, R.id.ll_username, R.id.ll_sign, R.id.ll_gender, R.id.ll_birthday})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_userhead:
                SystemUtils.openGallery(this);
                break;
            case R.id.ll_username:
                type = 0;
                showModifyDialog();
                break;
            case R.id.ll_sign:
                type = 1;
                showModifyDialog();
                break;
            case R.id.ll_gender:
                type = 2;
                showModifyGenderDialog();
                break;
            case R.id.ll_birthday:
                type = 3;
                showModifyBirthdayDialog();
                break;
        }
    }
    //修改生日
    public void showModifyBirthdayDialog() {
        String birthday = userBean.birthday;
        if (!TextUtils.isEmpty(birthday)) {
            year = Integer.valueOf(birthday.substring(0, 4));
            month = Integer.valueOf(birthday.substring(5, 7));
            day = Integer.valueOf(birthday.substring(8, 10));
        }else{
            year = 1990;
            month = 1;
            day = 1;
            userBean.birthday = "1990-01-01";
        }
        datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                try{
                    String month = (monthOfYear + 1) + "";
                    month = month.length() == 1 ? "0" + month : month;
                    String day = (dayOfMonth) + "";
                    day = day.length() == 1 ? "0" + day : day;
                    userBean.birthday = year + "-" + month + "-" + day;
                    UpdateUserInfo();
                }catch (Exception e){
                }
            }
        }, year, month - 1, day);
        datePickerDialog.setYearRange(1900,2006);
        datePickerDialog.setAccentColor(getResources().getColor(R.color.main_theme_color));
        datePickerDialog.show(getFragmentManager(),"Datepickerdialog");

    }

    //修改性别
    public void showModifyGenderDialog() {
        new MaterialDialog.Builder(this)
                .title(titles[2])
                .items(genders)
                .itemsCallbackSingleChoice(userBean.sex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        userBean.sex = which;
                        UpdateUserInfo();
                        return true;
                    }
                })
                .positiveText(R.string.submit)
                .show();
    }

    //修改昵称和签名
    public void showModifyDialog() {
        String name = TextUtils.isEmpty(userBean.name) ? "昵称" : userBean.name;
        String desc = TextUtils.isEmpty(userBean.descr) ? "签名" : userBean.descr;
        new MaterialDialog.Builder(this)
                .title(titles[type])
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, maxLengths[type])
                .positiveText(R.string.submit)
                .input(type == 0 ? name : desc, "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (type == 0)
                            userBean.name = input.toString();
                        else if (type == 1)
                            userBean.descr = input.toString();
                        UpdateUserInfo();
                    }
                }).show();
    }

    public void UpdateUserInfo(){
        modifyUserInfoPresenter.modifyUserInfo(userBean);
    }

    public void selectImage(Intent data, int requestCode) {
        if (requestCode == MyCommon.requestCode_photo) {
            // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
            Uri mImageCaptureUri = data.getData();
            // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
            if (mImageCaptureUri != null) {
                try {
                    // 这个方法是根据Uri获取Bitmap图片的静态方法
                    SystemUtils.startPhotoZoom(this, mImageCaptureUri, 200, 200);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            showMsg("图片选择失败");
        }
    }
    // 保存裁剪后的图片
    public void saveBitmap(Bitmap bitmap) {
        File file = new File(FileDir.picDir);
        if (!file.exists())
            file.mkdirs();
        try {
            headPath = FileDir.picDir + System.currentTimeMillis() + ".jpg";
            FileOutputStream b = new FileOutputStream(headPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
            b.flush();
            b.close();
            bitmap.recycle();
            uploadUserHead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //上传头像到服务器
    public void uploadUserHead() {
        UploadFileUtil uploadPicUtil = new UploadFileUtil(context);
        uploadPicUtil.uploadFile(headPath, MyCommon.fixxs[4],new UploadFileUtil.UploadResult() {
            @Override
            public void onSuccess(String imageUrl) {
                if (!TextUtils.isEmpty(headPath) && new File(headPath).exists()) {
                    new File(headPath).delete();
                }
                userBean.headurl = imageUrl;
                UpdateUserInfo();
            }

            @Override
            public void onFail() {
                if (!TextUtils.isEmpty(headPath) && new File(headPath).exists()) {
                    new File(headPath).delete();
                }
                showMsg("上传头像失败！");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyCommon.requestCode_photo && data != null) {
            selectImage(data, requestCode);
        } else if (requestCode == MyCommon.requestCode_crop && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bitmap = extras.getParcelable("data");
                saveBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modifyUserInfoPresenter != null){
            modifyUserInfoPresenter.cancelUrl();
        }
    }
}
