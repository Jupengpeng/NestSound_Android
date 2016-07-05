package com.xilu.wybz.ui;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.xilu.wybz.R;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.utils.PhoneDeviceUtil;
import com.xilu.wybz.utils.PrefsUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by June on 2016/3/2.
 */
public class BrowserActivity extends ToolbarActivity {

    public static final int FILECHOOSER_RESULTCODE = 200;

    @Bind(R.id.pb)
    ProgressBar mProgressBar;
    @Bind(R.id.webview)
    WebView mWebView;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private String url = "";
    private List<String> titles;
    public static void toBrowserActivity(Context context, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initData();

        mProgressBar.setProgress(20);
    }

    public void initViews() {
        titles = new ArrayList<>();
        setTitle("网页加载中...");
        initWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public void initWebView() {
        WebSettings webSettings = mWebView.getSettings(); // webView使用设置
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);//扩大比例的缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLoadWithOverviewMode(true);////自适应屏幕
        webSettings.setJavaScriptEnabled(true);// 设置支持JS脚本
        webSettings.setDisplayZoomControls(false);// 隐藏缩放按钮
        webSettings.setAllowFileAccess(true); // 设置允许访问文件数据
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 不带缓存

        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebChromeClient(new MyWebViewChromeClient()); // 处理解析，渲染网页等浏览器做的事情
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                String LOGIN = "yinchao://yinchao.cn/login";
                if (url.startsWith(LOGIN)){
                    startActivity(LoginActivity.class);
                    return true;
                }

                int id = PrefsUtil.getUserId(context);
                synCookies(context,url,"imei="+ PhoneDeviceUtil.getPhoneImei(context)+",ycua=APP_ANDROID,userId="+id);

                Log.e("cookies",getCookies(context,url));
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {

            }
        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

    }


    public void synCookies(Context context, String url, String cookies) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        cookieManager.setCookie(url, cookies);
        CookieSyncManager.getInstance().sync();
    }
    public String getCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        return cookieManager.getCookie(url);
    }


    public class MyWebViewChromeClient extends WebChromeClient {
        // 监听网页加载进度
        public void onProgressChanged(WebView view, int newProgress) {
            // 设置进度条进度
            if(mProgressBar!=null) {
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            titles.add(title);
            if (!TextUtils.isEmpty(title)) {
                setTitle(title);
            }
            super.onReceivedTitle(view, title);
        }

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(Intent.createChooser(intent, "选择照片"),
                    FILECHOOSER_RESULTCODE);
        }

        // For Android  > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(Intent.createChooser(i, "选择照片"), FILECHOOSER_RESULTCODE);
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(
                    Intent.createChooser(i, "选择照片"),
                    FILECHOOSER_RESULTCODE);
            return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (mUploadMessage != null) {
                Uri result = data == null || resultCode != RESULT_OK ? null
                        : data.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            } else if (mUploadCallbackAboveL != null) {
                Uri[] results = null;
                if (data == null) {
                } else {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null)
                        results = new Uri[]{Uri.parse(dataString)};
                }
                mUploadCallbackAboveL.onReceiveValue(results);
                mUploadCallbackAboveL = null;
            }
        }
    }

    public void onEventMainThread(Event.LoginSuccessEvent event){
        int id = PrefsUtil.getUserId(context);
        synCookies(context,url,"imei="+ PhoneDeviceUtil.getPhoneImei(context)+",ycua=APP_ANDROID,userId="+id);
        Log.e("cookies","LoginSuccessEvent:"+getCookies(context,url));
    }


    // 内部类
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
        }
        int id = PrefsUtil.getUserId(context);
        synCookies(context,url,"imei="+ PhoneDeviceUtil.getPhoneImei(context)+",ycua=APP_ANDROID,userId="+id);
        Log.e("cookies",getCookies(context,url));
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断是否可以返回操作
        if (mWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack();
            if(titles.size()>1) {
                titles.remove(titles.size() - 1);
                setTitle(titles.get(titles.size() - 1));
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 内部类
    class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String downUrl, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
            Uri uri = Uri.parse(downUrl);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        if (mWebView != null) mWebView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
