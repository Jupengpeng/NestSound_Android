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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MusicTalk;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.presenter.MusicTalkPresenter;
import com.xilu.wybz.ui.IView.IMusicTalkDetailView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.login.LoginActivity;
import com.xilu.wybz.ui.market.MatchActivity;
import com.xilu.wybz.ui.market.StarInfoActivity;
import com.xilu.wybz.ui.market.StarListActivity;
import com.xilu.wybz.ui.song.CommentActivity;
import com.xilu.wybz.utils.PhoneUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.dialog.ShareDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static com.xilu.wybz.common.MyCommon.CommentType_MuiscTalk;

/**
 * Created by June on 2016/3/2.
 */
public class BrowserActivity extends ToolbarActivity implements IMusicTalkDetailView {
    @Bind(R.id.pb)
    ProgressBar mProgressBar;
    @Bind(R.id.webview)
    WebView mWebView;
    private LinearLayout llFootBar;
    private LinearLayout llZan;
    private LinearLayout llComment;
    private LinearLayout llShare;
    private ImageView ivZanIcon;
    private TextView tvZanNum;
    private TextView tvShareNum;
    private TextView tvCommentNum;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private String url = "";
    private List<String> titles;
    private int isZan;
    private ShareDialog shareDialog;
    private MusicTalk musicTalk;
    public static final int FILECHOOSER_RESULTCODE = 200;
    private MusicTalkPresenter musicTalkPresenter;
    public static void toBrowserActivity(Context context, String url) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
    //乐说过来的
    public static void toBrowserActivity(Context context, MusicTalk musicTalk) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra(KeySet.KEY_MUSICTALK, musicTalk);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_webview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initViews();
        initData();
        mProgressBar.setProgress(20);
    }
    public void loadFootBar() {
        ViewStub stub = (ViewStub) findViewById(R.id.view_musictalk_footbar);
        llFootBar = (LinearLayout) stub.inflate();
        llZan = (LinearLayout) llFootBar.findViewById(R.id.ll_zan);
        llShare = (LinearLayout) llFootBar.findViewById(R.id.ll_share);
        llComment = (LinearLayout) llFootBar.findViewById(R.id.ll_comment);
        tvCommentNum = (TextView) llFootBar.findViewById(R.id.tv_comment_num);
        tvShareNum = (TextView) llFootBar.findViewById(R.id.tv_share_num);
        tvZanNum = (TextView) llFootBar.findViewById(R.id.tv_zan_num);
        ivZanIcon = (ImageView) llFootBar.findViewById(R.id.iv_zan_icon);
        llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemUtils.isLogin(context)) {
                    CommentActivity.toCommentActivity(context,musicTalk.itemid,CommentType_MuiscTalk,false);
                }
            }
        });
        llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shareDialog==null){
                    shareDialog = new ShareDialog(BrowserActivity.this,musicTalk);
                }
                shareDialog.showDialog();
            }
        });
        llZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemUtils.isLogin(context)) {
                    musicTalkPresenter.Zan(musicTalk.itemid);
                }
            }
        });
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
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);//设置 缓存模式
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebChromeClient(new MyWebViewChromeClient()); // 处理解析，渲染网页等浏览器做的事情
        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);


            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                Log.e("WebView Url", url);
                String LOGIN = "yinchao://yinchao.cn/login";
                if (url.startsWith(LOGIN)) {
                    startActivity(LoginActivity.class);
                    return true;
                }
                String strs[] = url.split("/");
                if (url.startsWith("yinchao://customization/tel/")) {
                    PhoneUtils.dial(context, strs[strs.length - 1]);
                    return true;
                } else if (url.startsWith("yinchao://customization/musician/uid/")) {
                    if (StringUtils.isInt(strs[strs.length - 1])) {
                        StarInfoActivity.toStarInfoActivity(context, Integer.valueOf(strs[strs.length - 1]));
                    }
                    return true;
                } else if (url.startsWith("yinchao://customization/musician/list")) {
                    startActivity(StarListActivity.class);
                    return true;
                } else if (url.startsWith("yinchao://customization/match/ing/")) {
                    String maps = strs[strs.length - 1];
                    String[] params = maps.split("&");
                    String aid = "";
                    String type = "";
                    if (params.length == 2) {
                        aid = params[0].split("=")[1];
                        type = params[1].split("=")[1];
                        if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(type)) {
                            MatchActivity.toMatchActivity(context, aid, Integer.valueOf(type), "ing");
                        }
                    }
                    return true;
                } else if (url.startsWith("yinchao://customization/match/end/")) {
                    String maps = strs[strs.length - 1];
                    String[] params = maps.split("&");
                    String aid = "";
                    String type = "";
                    if (params.length == 2) {
                        aid = params[0].split("=")[1];
                        type = params[1].split("=")[1];
                        if (StringUtils.isNotBlank(aid) && StringUtils.isNotBlank(type)) {
                            MatchActivity.toMatchActivity(context, aid, Integer.valueOf(type), "end");
                        }
                    }
                    return true;
                }
                int id = PrefsUtil.getUserId(context);
                synCookies(context, url, "imei=" + PhoneUtils.getPhoneImei(context) + ",ycua=APP_ANDROID,userId=" + id);

                Log.e("cookies", getCookies(context, url));
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

    @Override
    public void loadSuccess(MusicTalk musicTalkNum) {
        isZan = musicTalkNum.isZan;
        musicTalk.commentnum = musicTalkNum.commentnum;
        musicTalk.sharenum = musicTalkNum.sharenum;
        musicTalk.zannum = musicTalkNum.zannum;
        ivZanIcon.setImageResource(isZan==0?R.drawable.ic_musictalk_zan:R.drawable.ic_musictalk_zaned);
        tvCommentNum.setText("("+musicTalkNum.commentnum+")");
        tvShareNum.setText("("+musicTalkNum.sharenum+")");
        tvZanNum.setText("("+musicTalkNum.zannum+")");
    }

    @Override
    public void loadError() {

    }

    @Override
    public void zanSuccess() {
        isZan = 1-isZan;
        ivZanIcon.setImageResource(isZan==0?R.drawable.ic_musictalk_zan:R.drawable.ic_musictalk_zaned);
        if(isZan==1){
            musicTalk.zannum += 1;
        }else{
            musicTalk.zannum -= 1;
        }
        tvZanNum.setText("("+musicTalk.zannum+")");
    }

    @Override
    public void zanFail() {

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ShareSuccessEvent event) {
        if(musicTalk!=null){
            musicTalkPresenter.shareCount(musicTalk.itemid);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataCommentNumEvent event) {
        if(event.getType()== CommentType_MuiscTalk){
            int count = event.getNum();
            musicTalk.commentnum+=count;
            tvCommentNum.setText("("+musicTalk.commentnum+")");
        }
    }


    @Override
    public void shareSuccess() {
        musicTalk.sharenum += 1;
        tvShareNum.setText("("+musicTalk.sharenum+")");
    }

    @Override
    public void shareFail() {

    }

    @Override
    public void initView() {

    }


    public class MyWebViewChromeClient extends WebChromeClient {
        // 监听网页加载进度
        public void onProgressChanged(WebView view, int newProgress) {
            // 设置进度条进度
            if (mProgressBar != null) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginSuccessEvent event) {
        int id = PrefsUtil.getUserId(context);
        synCookies(context, url, "imei=" + PhoneUtils.getPhoneImei(context) + ",ycua=APP_ANDROID,userId=" + id);
        Log.e("cookies", "LoginSuccessEvent:" + getCookies(context, url));
    }

    // 内部类
    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            musicTalk = (MusicTalk) bundle.getSerializable(KeySet.KEY_MUSICTALK);
            if(musicTalk==null){
                url = bundle.getString("url");
            }else{
                url = musicTalk.url;
            }
            musicTalk.shareurl = musicTalk.url;
        }
        if(musicTalk!=null){
            loadFootBar();
            musicTalkPresenter = new MusicTalkPresenter(context,this);
            musicTalkPresenter.getDetail(musicTalk.itemid);
        }
        int id = PrefsUtil.getUserId(context);
        synCookies(context, url, "imei=" + PhoneUtils.getPhoneImei(context) + ",ycua=APP_ANDROID,userId=" + id);
        Log.e("cookies", getCookies(context, url));
        mWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断是否可以返回操作
        if (mWebView.canGoBack() && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            mWebView.goBack();
            if (titles.size() > 1) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
                return true;

            case R.id.menu_close:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        EventBus.getDefault().unregister(this);
    }
}
