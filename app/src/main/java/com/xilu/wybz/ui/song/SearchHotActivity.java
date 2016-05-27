package com.xilu.wybz.ui.song;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.common.PlayBanZouInstance;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.fragment.BanzouFragment;
import com.xilu.wybz.ui.fragment.SearchSongFragment;
import com.xilu.wybz.utils.KeyBoardUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/5/19.
 */
public class SearchHotActivity extends BaseActivity {
    @Bind(R.id.et_keyword)
    EditText etKeyword;
    @Bind(R.id.iv_cancle)
    ImageView ivCancle;
    @Bind(R.id.frame_content)
    FrameLayout frameContent;
    private BanzouFragment banzouFragment;

    public SearchHotActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        initData();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search_hot;
    }


    private void initView() {
    }

    private void initEvent() {
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().equals("")) {
                    ivCancle.setVisibility(View.GONE);
                } else {
                    ivCancle.setVisibility(View.VISIBLE);
                }
            }
        });
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(etKeyword.getText().toString());
                }
                return false;
            }
        });
    }

    private void search(String keyword) {
        if (keyword.trim().equals("")) {
            showMsg("关键字不能为空");
            return;
        }
        banzouFragment.loadHotData(keyword);
    }

    private void initData() {
        banzouFragment = new BanzouFragment();
        banzouFragment.setType(3);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content, banzouFragment).commit();
    }

    @OnClick({R.id.iv_cancle, R.id.rl_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cancle:
                KeyBoardUtil.openKeybord(etKeyword, context);
                etKeyword.setText("");
                break;
            case R.id.rl_right:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (banzouFragment != null && banzouFragment.getCount() > 0) {
            PlayBanZouInstance.getInstance().stopMediaPlay();
        }
    }
}
