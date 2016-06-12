package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.TemplateBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.PlayBanZouInstance;
import com.xilu.wybz.common.PlayMediaInstance;
import com.xilu.wybz.common.interfaces.IMediaPlayerListener;
import com.xilu.wybz.common.interfaces.ITemplateMusicListener;
import com.xilu.wybz.presenter.HotPresenter;
import com.xilu.wybz.ui.IView.IHotView;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.HotFragment;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.KeyboardListenLayout;
import com.xilu.wybz.view.pull.BaseViewHolder;
import com.xilu.wybz.view.pull.PullRecycler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/19.
 */
public class SearchHotActivity extends ToolbarActivity {
    @Bind(R.id.et_keyword)
    EditText etkeyWord;
    @Bind(R.id.iv_cancle)
    ImageView ivCancle;
    HotFragment hotFragment;
    String keyWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
        initEvent();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search_hot;
    }


    public void initView() {
        hotFragment = HotFragment.newInstance(-1);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, hotFragment).commit();

    }

    private void initEvent() {
        etkeyWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                keyWord = s.toString().trim();
                if (keyWord.equals("")) {
                    if(hotFragment!=null){
                        hotFragment.clearData();
                    }
                    etkeyWord.requestFocus();
                    KeyBoardUtil.openKeybord(etkeyWord,context);
                    ivCancle.setVisibility(View.GONE);
                } else {
                    ivCancle.setVisibility(View.VISIBLE);
                }
            }
        });
        etkeyWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (keyWord.equals("")) {
                        showMsg("关键字不能为空");
                        return false;
                    } else {
                        hotFragment.loadData(keyWord);
                    }
                }
                return false;
            }
        });
    }

    @OnClick({R.id.iv_cancle, R.id.rl_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancle:
                etkeyWord.setText("");
                break;
            case R.id.rl_right:
                KeyBoardUtil.closeKeybord(etkeyWord,context);
                finish();
                break;
        }
    }
    public void onEventMainThread(Event.HideKeyboardEvent event) {
        KeyBoardUtil.openAndCloseKeybord(context);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(PlayBanZouInstance.getInstance().status>1){
            PlayBanZouInstance.getInstance().stopMediaPlay();
        }
        EventBus.getDefault().unregister(this);
    }
}
