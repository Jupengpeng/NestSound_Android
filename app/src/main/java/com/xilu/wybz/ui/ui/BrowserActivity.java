package com.xilu.wybz.ui.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.ToolbarActivity;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by June on 2016/3/2.
 */
public class BrowserActivity extends ToolbarActivity {
    @Bind(R.id.pb)
    ProgressBar mProgressBar;
    @Bind(R.id.webview)
    WebView mWebView;
    String url = "";
    List<String> loadHistoryUrls;
    public static void toBrowserActivity(Context context,String url){
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
    }

    public void initViews() {
        // TODO Auto-generated method stub
        setTitle("网页加载中...");
        initWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public void initWebView() {
        // TODO Auto-generated method stub
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
        mWebView.setWebChromeClient(new mWebViewChromeClient()); // 处理解析，渲染网页等浏览器做的事情
        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                loadHistoryUrls.add(url);
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
                // TODO Auto-generated method stub

            }
        });
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

    }

    public class mWebViewChromeClient extends WebChromeClient {
        // 监听网页加载进度
        public void onProgressChanged(WebView view, int newProgress) {
            // 设置进度条进度
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
            if (!TextUtils.isEmpty(title)) {
                setTitle(title);
                if(title.equals("正在跳转")){
                    loadHistoryUrls.remove(loadHistoryUrls.size()-1);
                }
            }
            super.onReceivedTitle(view, title);
        }
    }

    public void initData() {
        // TODO Auto-generated method stub
        loadHistoryUrls = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            loadHistoryUrls.add(url);
        }
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断是否可以返回操作
        if (mWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //过滤是否为重定向后的链接
            if (loadHistoryUrls.size() > 1 ) {
                //移除加载栈中的最后一个链接
                loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
                //加载重定向之前的页
                mWebView.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
                return false;
            }
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
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;
        }
    }
}
