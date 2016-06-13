package com.xilu.wybz.utils;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import com.sina.weibo.sdk.utils.LogUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;
import com.xilu.wybz.R;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.view.materialdialogs.DialogAction;
import com.xilu.wybz.view.materialdialogs.GravityEnum;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class VersionUtil {
    final int DOWN_APK = 1;
    final int INSTALL_APK = 2;
    final int DOWN_ERROR = 3;
    final int UPDATE_INFO = 4;
    int mSize;
    UpdateResponse mUpdateResponse;
    final String TAG = "VersionUtil";
    String apkFilePath;
    Activity mContext;
    MaterialDialog materialDialog1;
    MaterialDialog materialDialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_APK:
                    break;
                case INSTALL_APK:
                    installApk();
                    break;
                case DOWN_ERROR:
                    if (materialDialog != null)
                        materialDialog.dismiss();
                    ToastUtils.toast(mContext, "下载出错");
                    break;
                case UPDATE_INFO:
                    showUpdataInfo();
                    break;
                default:
                    break;
            }
        }
    };
    public void checkUpdateInfo(Activity activity){
        mContext = activity;
        UmengUpdateAgent.setUpdateAutoPopup(false);
        UmengUpdateAgent.setUpdateCheckConfig(false);
        UmengUpdateAgent.setUpdateOnlyWifi(true);
        UmengUpdateAgent.setDeltaUpdate(false);
        UmengUpdateAgent.update(activity);
        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int i, final UpdateResponse updateResponse) {
                if (i == UpdateStatus.Yes) {
                    //如果版本已经被忽略，不弹框
                    if (UmengUpdateAgent.isIgnore(mContext, updateResponse)) {
                        return;
                    }
                    new Thread() {
                        public void run() {
                            try {
                                int apkSize = (int) (FileUtils.getFileSize(updateResponse.path) / 1024.0);
                                Message msg = new Message();
                                mUpdateResponse = updateResponse;
                                mSize = apkSize;
                                if (mSize > 0) {
                                    handler.sendEmptyMessage(UPDATE_INFO);
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        });
    }
    public void showUpdataInfo() {
        if(!new File(FileDir.apkDir).exists())new File(FileDir.apkDir).mkdirs();
        apkFilePath = FileDir.apkDir + mUpdateResponse.version + ".apk";
        String content = mUpdateResponse.updateLog;
        materialDialog1 = new MaterialDialog.Builder(mContext)
                .title("发现新版本" + mUpdateResponse.version)
                .content(content)
                .positiveText("安装")
                .negativeText("取消")
                .neutralText("忽略")
                .canceledOnTouchOutside(true)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UmengUpdateAgent.ignoreUpdate(mContext,mUpdateResponse);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        materialDialog1.dismiss();
                        File file = new File(apkFilePath);
                        String md5 = mUpdateResponse.new_md5;
                        if (file.exists() && MD5Util.getFileMD5String(file).equals(md5)) {
                            handler.sendEmptyMessage(INSTALL_APK);
                        } else {
                            updateNewDialog();
                            downApk(mUpdateResponse.path);
                        }
                    }
                }).onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                }).build();
        materialDialog1.show();
    }
    public void updateNewDialog() {
        try {
            materialDialog = new MaterialDialog.Builder(mContext)
                    .title(R.string.progress_dialog)
                    .content(R.string.please_wait_down)
                    .contentGravity(GravityEnum.CENTER)
                    .progress(false, mSize, true)
                    .canceledOnTouchOutside(false).build();
            materialDialog.show();
        } catch (Exception e) {
            LogUtil.d("error", e.toString());
        }
    }
    public void downApk(final String apkUrl) {
        new Thread() {
            public void run() {
                try {
                    // 发送开始下载apk通知
                    handler.sendEmptyMessage(DOWN_APK);
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setConnectTimeout(5000);
                    InputStream is = conn.getInputStream();
                    if (!new File(FileDir.apkDir).exists()) {
                        new File(FileDir.apkDir).mkdirs();
                    }
                    File file = new File(apkFilePath);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[2048];
                    int len;
                    int total = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        total += len;
                        // 获取当前下载量
                        if (total / 1024 <= mSize) {
                            if (materialDialog != null)
                                materialDialog.setProgress(total / 1024);
                        }else{
                            if (materialDialog != null)
                                materialDialog.dismiss();
                        }
                    }
                    fos.close();
                    bis.close();
                    is.close();
                    // 发送下载完成apk通知
                    handler.sendEmptyMessage(INSTALL_APK);
                } catch (Exception e) {
                    // 发送下载apk错误通知
                    handler.sendEmptyMessage(DOWN_ERROR);
                    e.printStackTrace();
                }
            }
        }.start();
        ;
    }
    // 安装apk
    public void installApk() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkFilePath)),
                "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }
}