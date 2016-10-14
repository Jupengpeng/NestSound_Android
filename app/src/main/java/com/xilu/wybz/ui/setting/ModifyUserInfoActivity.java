package com.xilu.wybz.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.presenter.ModifyUserInfoPresenter;
import com.xilu.wybz.ui.IView.IModifyUserInfoView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.AppConstant;
import com.xilu.wybz.utils.GalleryUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.UploadFileUtil;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/5/13.
 */
public class ModifyUserInfoActivity extends ToolbarActivity implements IModifyUserInfoView {
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
    String titles[] = new String[]{"修改昵称", "修改签名", "修改性别", "修改生日"};
    String genders[] = new String[]{"未知", "女", "男"};
    int maxLengths[] = new int[]{16, 30};
    String headPath;
    UploadFileUtil uploadPicUtil;
    ModifyUserInfoPresenter modifyUserInfoPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_modify_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modifyUserInfoPresenter = new ModifyUserInfoPresenter(this, this);
        modifyUserInfoPresenter.init();
    }

    @Override
    public void modifyUserInfoSuccess() {
        cancelPd();
        showMsg("修改成功");
        updateInfo();
    }

    @Override
    public void modifyUserInfoFail() {
        cancelPd();
    }

    @Override
    public void initView() {
        setTitle("编辑资料");
        initUserInfo();
    }

    public void initUserInfo() {
        userBean = PrefsUtil.getUserInfo(context);
        tv_username.setText(userBean.nickname);
        tv_usersign.setText(userBean.signature);
        tv_birthday.setText(userBean.birthday);
        tv_gender.setText(genders[userBean.sex]);
        if (StringUtils.isNotBlank(userBean.headurl)) {
            if (!userBean.headurl.startsWith("http")) {
                userBean.headurl = MyHttpClient.QINIU_URL + userBean.headurl;
            }
            loadImage(userBean.headurl, iv_head);
        }
    }
    public void updateInfo(){
        UserBean bean = PrefsUtil.getUserInfo(context);
        bean.headurl = userBean.headurl;
        bean.nickname = userBean.nickname;
        bean.signature = userBean.signature;
        bean.birthday = userBean.birthday;
        bean.sex = userBean.sex;
        PrefsUtil.saveUserInfo(context, bean);
        EventBus.getDefault().post(new Event.UpdateUserInfo());
        initUserInfo();
    }
    @OnClick({R.id.ll_userhead, R.id.ll_username, R.id.ll_sign, R.id.ll_gender, R.id.ll_birthday, R.id.ll_bq_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_userhead:
                GalleryUtils.getInstance().selectPicture(this);
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
            case R.id.ll_bq_info:

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
        } else {
            year = 1990;
            month = 1;
            day = 1;
            userBean.birthday = "1990-01-01";
        }
        datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    String month = (monthOfYear + 1) + "";
                    month = month.length() == 1 ? "0" + month : month;
                    String day = (dayOfMonth) + "";
                    day = day.length() == 1 ? "0" + day : day;
                    userBean.birthday = year + "-" + month + "-" + day;
                    UpdateUserInfo();
                } catch (Exception e) {
                }
            }
        }, year, month - 1, day);
        datePickerDialog.setYearRange(1900, 2006);
        datePickerDialog.setAccentColor(getResources().getColor(R.color.main_theme_color));
        datePickerDialog.show(getFragmentManager(), "Datepickerdialog");

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
        String nickname = TextUtils.isEmpty(userBean.nickname) ? "昵称" : userBean.nickname;
        String signature = TextUtils.isEmpty(userBean.signature) ? "签名" : userBean.signature;
        new MaterialDialog.Builder(this)
                .title(titles[type])
                .inputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, maxLengths[type])
                .positiveText(R.string.submit)
                .input(type == 0 ? nickname : signature, "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if (type == 0)
                            userBean.nickname = input.toString();
                        else if (type == 1)
                            userBean.signature = input.toString();
                        UpdateUserInfo();
                    }
                }).show();
    }


    //上传头像到服务器
    public void uploadUserHead() {
        showPd("正在上传头像...");
        if(uploadPicUtil==null)
            uploadPicUtil = new UploadFileUtil(context);
        uploadPicUtil.uploadFile(headPath, MyCommon.fixxs[4], new UploadFileUtil.UploadResult() {
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
                cancelPd();
                if (!TextUtils.isEmpty(headPath) && new File(headPath).exists()) {
                    new File(headPath).delete();
                }
                showMsg("上传头像失败！");
            }
        });
    }
    public void UpdateUserInfo() {
        modifyUserInfoPresenter.modifyUserInfo(userBean);
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
            if(!new File(FileDir.headPic).exists())new File(FileDir.headPic).mkdirs();
            headPath = FileDir.headPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(headPath));
            GalleryUtils.getInstance().cropPicture(this, uri, imgUri, 1, 1, 200, 200);
        } else if (requestCode == AppConstant.KITKAT_ABOVE) {
            uri = data.getData();
            Log.d("tag", "uri=" + uri);
            // 先将这个uri转换为path，然后再转换为uri
            String thePath = GalleryUtils.getInstance().getPath(this, uri);
            if(!new File(FileDir.headPic).exists())new File(FileDir.headPic).mkdirs();
            headPath = FileDir.headPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(headPath));
            GalleryUtils.getInstance().cropPicture(this,
                    Uri.fromFile(new File(thePath)),imgUri, 1, 1, 200, 200);
        } else if (requestCode == AppConstant.INTENT_CROP) {
            if(new File(headPath).exists()){
                uploadUserHead();
            }else{
                showMsg("裁切失败");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modifyUserInfoPresenter != null) {
            modifyUserInfoPresenter.cancelRequest();
        }
    }
}
