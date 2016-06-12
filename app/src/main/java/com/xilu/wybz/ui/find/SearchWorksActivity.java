package com.xilu.wybz.ui.find;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.SearchAdapter;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.fragment.SearchLyricsFragment;
import com.xilu.wybz.ui.fragment.SearchSongFragment;
import com.xilu.wybz.ui.fragment.SearchUserFragment;
import com.xilu.wybz.utils.KeyBoardUtil;
import com.xilu.wybz.view.KeyboardListenLayout;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by hujunwei on 16/5/22.
 */
public class SearchWorksActivity extends ToolbarActivity {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.iv_cancle)
    ImageView ivCancle;
    @Bind(R.id.container)
    ViewPager container;
    @Bind(R.id.rl_tab)
    RelativeLayout rlTab;
    @Bind(R.id.et_keyword)
    EditText etKeyword;
    boolean isShowTab;
    String keyWord;
    SearchAdapter pagerAdapter;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }
    public void initView() {
        EventBus.getDefault().register(this);
        pagerAdapter = new SearchAdapter(getSupportFragmentManager());
        container.setAdapter(pagerAdapter);
        container.setOffscreenPageLimit(3);
        rlTab.setVisibility(View.GONE);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(container);
        container.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(TextUtils.isEmpty(keyWord))return;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (position){
                            case 0:
                                SearchSongFragment searchSongFragment = (SearchSongFragment) pagerAdapter.getFragment(0);
                                if(searchSongFragment!=null){
                                    searchSongFragment.loadData(keyWord);
                                }
                                break;
                            case 1:
                                SearchLyricsFragment searchLyricsFragment = (SearchLyricsFragment) pagerAdapter.getFragment(1);
                                if(searchLyricsFragment!=null){
                                    searchLyricsFragment.loadData(keyWord);
                                }
                                break;
                            case 2:
                                SearchUserFragment searchUserFragment = (SearchUserFragment) pagerAdapter.getFragment(2);
                                if(searchUserFragment!=null){
                                    searchUserFragment.loadData(keyWord);
                                }
                                break;
                        }
                    }
                },100);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWord = s.toString().trim();
                if(keyWord.equals("")){
                    clearContent();
                    etKeyword.requestFocus();
                    KeyBoardUtil.openKeybord(etKeyword,context);
                    ivCancle.setVisibility(View.GONE);
                }else{
                    ivCancle.setVisibility(View.VISIBLE);
                }
            }
        });
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    if(!TextUtils.isEmpty(keyWord)){
                        clearContent();
                        if(container.getCurrentItem()==0) {
                            SearchSongFragment searchSongFragment = (SearchSongFragment) pagerAdapter.getFragment(0);
                            if (searchSongFragment != null) {
                                searchSongFragment.loadData(keyWord);
                            }
                        }else if(container.getCurrentItem()==1){
                            SearchLyricsFragment searchLyricsFragment = (SearchLyricsFragment) pagerAdapter.getFragment(1);
                            if (searchLyricsFragment != null) {
                                searchLyricsFragment.loadData(keyWord);
                            }
                        }else if(container.getCurrentItem()==2) {
                            SearchUserFragment searchUserFragment = (SearchUserFragment) pagerAdapter.getFragment(2);
                            if (searchUserFragment != null) {
                                searchUserFragment.loadData(keyWord);
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    public void onEventMainThread(Event.ShowSearchTabEvent event) {
        if(!isShowTab) {
            rlTab.setVisibility(View.VISIBLE);
            isShowTab = true;
        }
        KeyBoardUtil.closeKeybord(etKeyword,context);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.rl_right, R.id.iv_cancle})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_right:
                KeyBoardUtil.closeKeybord(etKeyword,context);
                finish();
                break;
            case R.id.iv_cancle:
                etKeyword.setText("");
                break;
        }
    }
    public void clearContent(){
        SearchSongFragment searchSongFragment = (SearchSongFragment) pagerAdapter.getFragment(0);
        if(searchSongFragment!=null){
            searchSongFragment.clearData();
        }
        SearchLyricsFragment searchLyricsFragment = (SearchLyricsFragment) pagerAdapter.getFragment(1);
        if(searchLyricsFragment!=null){
            searchLyricsFragment.clearData();
        }
        SearchUserFragment searchUserFragment = (SearchUserFragment) pagerAdapter.getFragment(2);
        if(searchUserFragment!=null){
            searchUserFragment.clearData();
        }
    }
}
