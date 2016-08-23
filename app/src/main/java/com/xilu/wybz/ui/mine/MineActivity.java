package com.xilu.wybz.ui.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commit451.nativestackblur.NativeStackBlur;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.MineAdapter;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.UserInfoBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.common.interfaces.OnTabActivityResultListener;
import com.xilu.wybz.presenter.DownPicPresenter;
import com.xilu.wybz.presenter.ModifyCoverPresenter;
import com.xilu.wybz.ui.IView.ILoadPicView;
import com.xilu.wybz.ui.IView.IModifyCoverView;
import com.xilu.wybz.ui.base.BaseActivity;
import com.xilu.wybz.ui.fragment.WorksDataFragment;
import com.xilu.wybz.ui.setting.SettingActivity;
import com.xilu.wybz.utils.AppConstant;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.FileUtils;
import com.xilu.wybz.utils.GalleryUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PermissionUtils;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.utils.UploadFileUtil;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.IndexViewPager;
import com.xilu.wybz.view.SystemBarHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hujunwei on 16/6/2.
 */
public class MineActivity extends BaseActivity implements IModifyCoverView, ILoadPicView, OnTabActivityResultListener {
    @Bind(R.id.iv_blur_view)
    ImageView ivBlurView;
    @Bind((R.id.ll_mine_info))
    LinearLayout llMineInfo;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.viewpager)
    IndexViewPager mViewpager;
    @Bind(R.id.appbar)
    AppBarLayout mAppbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tv_nickname)
    TextView mNickname;
    @Bind(R.id.tv_desc)
    TextView mDesc;
    @Bind(R.id.tv_fans_num)
    TextView mFansNum;
    @Bind(R.id.tv_follow_num)
    TextView mFollowNum;
    @Bind(R.id.ll_myrecord)
    LinearLayout llMyrecord;
    @Bind(R.id.ll_mysong)
    LinearLayout llMysong;
    @Bind(R.id.ll_mylyrics)
    LinearLayout llMylyrics;
    @Bind(R.id.ll_myfav)
    LinearLayout llMyfav;
    @Bind(R.id.tv_song_num)
    TextView tvSongNum;
    @Bind(R.id.tv_lyrics_num)
    TextView tvLyricsNum;
    @Bind(R.id.tv_fov_num)
    TextView tvFovNum;
    @Bind(R.id.tv_record_num)
    TextView tvRecordNum;
    @Bind(R.id.iv_headpic)
    ImageView ivHeadPic;
    private boolean firstLoadUserInfo;
    private int currentIndex;
    private MineAdapter pagerAdapter;
    private List<LinearLayout> tabs;
    public boolean isFirst;
    public UserInfoBean userInfoBean;
    public UserBean userBean;
    ModifyCoverPresenter modifyCoverPresenter;
    String bgPic;
    DownPicPresenter downPicPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_new_mine;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarHelper.setHeightAndPadding(this, mToolbar);
    }

    public void initData() {
        if (isFirst) return;
        else isFirst = true;
        EventBus.getDefault().register(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        SystemBarHelper.immersiveStatusBar(this, 0);
        userBean = PrefsUtil.getUserInfo(context);
        setLocalUserInfo();
        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                boolean showTitle = mCollapsingToolbar.getHeight() + verticalOffset <= mToolbar.getHeight() * 2;
                llMineInfo.setVisibility(showTitle ? View.GONE : View.VISIBLE);
                int height = mCollapsingToolbar.getHeight() - mToolbar.getHeight();
                float alpha = Math.abs(verticalOffset) / (height + 0.0f);
                Log.e("alpha", alpha + "");
                ivBlurView.setAlpha(alpha);
            }
        });

        tabs = new ArrayList<>();
        llMysong.setSelected(true);
        tabs.add(llMysong);
        tabs.add(llMylyrics);
        tabs.add(llMyfav);
        tabs.add(llMyrecord);
        pagerAdapter = new MineAdapter(context, getSupportFragmentManager(), PrefsUtil.getUserId(context), PrefsUtil.getUserInfo(context).name);
        mViewpager.setAdapter(pagerAdapter);
        mViewpager.setOffscreenPageLimit(tabs.size());

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                changeTabColor();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        WorksDataFragment worksDataFragment = pagerAdapter.getFragment(position);
                        worksDataFragment.loadData();
                    }
                }, 120);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginOutEvent event) {
        firstLoadUserInfo = false;
        mNickname.setText("");
        mDesc.setText("");
        ivHead.setImageResource(R.drawable.ic_default_head_252);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.LoginSuccessEvent event) {
        firstLoadUserInfo = false;
        mViewpager.setCurrentItem(0);
        userBean = PrefsUtil.getUserInfo(context);
        WorksDataFragment worksDataFragment;
        worksDataFragment = pagerAdapter.getFragment(0);
        worksDataFragment.reSet();
        worksDataFragment.loadData();
        worksDataFragment = pagerAdapter.getFragment(1);
        worksDataFragment.reSet();
        worksDataFragment = pagerAdapter.getFragment(2);
        worksDataFragment.reSet();
        worksDataFragment = pagerAdapter.getFragment(3);
        worksDataFragment.reSet();
    }

    private void changeTabColor() {
        for (int i = 0; i < tabs.size(); i++) {
            LinearLayout tab = tabs.get(i);
            tab.setSelected(currentIndex == i);
        }
    }

    public void setUserInfo(UserBean updateUserBean) {
        if (!firstLoadUserInfo) {
            //更新本地数据
            if (updateUserBean.userid > 0) userBean.userid = updateUserBean.userid;
            if (StringUtils.isNotBlank(updateUserBean.nickname))
                userBean.name = updateUserBean.nickname;
            if (StringUtils.isNotBlank(updateUserBean.signature))
                userBean.descr = updateUserBean.signature;
            if (StringUtils.isNotBlank(updateUserBean.headurl))
                userBean.headurl = updateUserBean.headurl;
            if (StringUtils.isNotBlank(updateUserBean.bgpic)) {
                userBean.bgpic = updateUserBean.bgpic;
            }
            PrefsUtil.saveUserInfo(context, userBean);
            //更新本地我的信息
            setLocalUserInfo();
        }
    }

    public void setLocalUserInfo() {
        if (StringUtils.isNotBlank(userBean.headurl) && !userBean.headurl.equals(MyCommon.defult_head)) {
            int headWidth = DensityUtil.dip2px(context, 92);
            loadImage(MyCommon.getImageUrl(userBean.headurl, headWidth, headWidth), ivHead);
        }
        if (StringUtils.isNotBlank(userBean.nickname)) mNickname.setText(userBean.nickname);
        if (StringUtils.isNotBlank(userBean.signature)) mDesc.setText(userBean.signature);
        if (StringUtils.isNotBlank(userBean.nickname)) setTitle(userBean.nickname);
        if (StringUtils.isNotBlank(userBean.bgpic)) {
            String path = FileDir.mineBgPic + MD5Util.getMD5String(userBean.bgpic) + ".jpg";
            if (new File(path).exists()) {
                loadHeadPic(path);
            } else {//需要先下载图片
                if (downPicPresenter == null) {
                    downPicPresenter = new DownPicPresenter(context, MineActivity.this);
                    downPicPresenter.downLoadPic(userBean.bgpic, path);
                }
            }
        } else {
            ivHeadPic.setImageResource(R.mipmap.bg_top_mine);
            Bitmap bmp = NativeStackBlur.process(BitmapUtils.ReadBitmapById(this, R.mipmap.bg_top_mine), 200);
            ivBlurView.setImageBitmap(bmp);
        }
    }

    public void updateUserFansNum(UserInfoBean userBean) {
        userInfoBean = userBean;
        mFansNum.setText(NumberUtil.format(userBean.fansnum));
        mFollowNum.setText(NumberUtil.format(userBean.gznum));
        tvSongNum.setText(NumberUtil.format(userBean.worknum));
        tvLyricsNum.setText(NumberUtil.format(userBean.lyricsnum));
        tvFovNum.setText(NumberUtil.format(userBean.fovnum));
        tvRecordNum.setText(NumberUtil.format(userBean.inspirenum));
    }

    //在修改个人资料页面发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateUserInfo event) {
        userBean = PrefsUtil.getUserInfo(context);
        setLocalUserInfo();
    }

    //在歌曲的列表 发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataUserBean event) {
        if (event.getType() == 1) {
            if (event.getUserBean() != null)
                setUserInfo(event.getUserBean());
        }
    }

    //在歌曲的列表 发送过来的
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataUserInfoBean event) {
        if (event.getType() == 1) {
            updateUserFansNum(event.getUserBean());
        }
    }

    //修改粉丝数量
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateFollowNumEvent event) {
        int type = event.getType();
        if (type == 0) {
            userInfoBean.gznum -= 1;
            if (userInfoBean.gznum < 0) userInfoBean.gznum = 0;
        } else {
            userInfoBean.gznum += 1;
        }
        mFollowNum.setText(NumberUtil.format(userInfoBean.gznum));
    }

    //灵感记录 歌曲  歌词 发布成功 更新列表数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataWorksList event) {
        int type = event.getType();
        if (event.getChange() == 0) {
            (pagerAdapter.getFragment(type)).updateList();
        } else if (event.getChange() == 1) {
            (pagerAdapter.getFragment(type)).removeData(event.getWorksData());
            updateNums(type, -1);
        } else if (event.getChange() == 2) {
            (pagerAdapter.getFragment(type)).updateData(event.getWorksData());
        }
    }

    //灵感记录 歌曲  歌词 发布成功 更新列表数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdateWorksNum event) {
        if (userInfoBean == null) return;
        int type = event.getType();
        int count = event.getCount();
        updateNums(type, count);
    }

    public void updateNums(int type, int count) {
        switch (type) {
            case 1:
                userInfoBean.worknum = userInfoBean.worknum + count;
                tvSongNum.setText(NumberUtil.format(userInfoBean.worknum));
                break;
            case 2:
                userInfoBean.lyricsnum = userInfoBean.lyricsnum + count;
                tvLyricsNum.setText(NumberUtil.format(userInfoBean.lyricsnum));
                break;
            case 3:
                userInfoBean.fovnum = userInfoBean.fovnum + count;
                tvFovNum.setText(NumberUtil.format(userInfoBean.fovnum));
                break;
            case 4:
                userInfoBean.inspirenum = userInfoBean.inspirenum + count;
                tvRecordNum.setText(NumberUtil.format(userInfoBean.inspirenum));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.RemoveMySongEvent event) {
        int itemid = event.getItemid();
        (pagerAdapter.getFragment(0)).removeByItemid(itemid);
    }

    @OnClick({R.id.iv_blur_view, R.id.user_fansnum, R.id.user_follownum, R.id.ll_myrecord, R.id.ll_mysong, R.id.ll_mylyrics, R.id.ll_myfav})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_blur_view:
                GalleryUtils.getInstance().selectPicture(getParent());
                break;
            case R.id.user_follownum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FOLLOW_ACT, PrefsUtil.getUserId(context));
                break;
            case R.id.user_fansnum:
                FollowAndFansActivity.toFollowAndFansActivity(context, KeySet.TYPE_FANS_ACT, PrefsUtil.getUserId(context));
                break;
            case R.id.ll_myrecord:
                if (currentIndex == 3) {
                    return;
                } else {
                    currentIndex = 3;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_mysong:
                if (currentIndex == 0) {
                    return;
                } else {
                    currentIndex = 0;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_mylyrics:
                if (currentIndex == 1) {
                    return;
                } else {
                    currentIndex = 1;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
                break;
            case R.id.ll_myfav:
                if (currentIndex == 2) {
                    return;
                } else {
                    currentIndex = 2;
                }
                mViewpager.setCurrentItem(currentIndex);
                changeTabColor();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_setting) {
            startActivity(SettingActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    //上传头像到服务器
    public void uploadCoverBg() {
        showPd("图片上传中...");
        UploadFileUtil uploadPicUtil = new UploadFileUtil(context);
        uploadPicUtil.uploadFile(bgPic, MyCommon.fixxs[4], new UploadFileUtil.UploadResult() {
            @Override
            public void onSuccess(String imageUrl) {
                if (modifyCoverPresenter == null) {
                    modifyCoverPresenter = new ModifyCoverPresenter(context, MineActivity.this);
                }
                modifyCoverPresenter.modifyCover(imageUrl);
            }

            @Override
            public void onFail() {
                if (!TextUtils.isEmpty(bgPic) && new File(bgPic).exists()) {
                    new File(bgPic).delete();
                }
                cancelPd();
                showMsg("上传失败！");
            }
        });
    }

    @Override
    public void onSuccess(String pic) {
        cancelPd();
        showMsg("上传成功！");
        String bgpic = MyHttpClient.QINIU_URL + pic;
        userBean.bgpic = bgpic;
        PrefsUtil.saveUserInfo(context, userBean);
        String newPath = FileDir.mineBgPic + MD5Util.getMD5String(bgpic) + ".jpg";
        FileUtils.renameFile(bgPic, newPath);
        loadHeadPic(newPath);
    }

    public void loadHeadPic(String bgPicPath) {
        Bitmap bitmap = BitmapUtils.getSDCardImg(bgPicPath);
        ivHeadPic.setImageBitmap(bitmap);
        Bitmap bmp = NativeStackBlur.process(bitmap, 200);
        ivBlurView.setImageBitmap(bmp);
    }

    @Override
    public void onFail() {
        cancelPd();
    }

    @Override
    public void initView() {

    }

    @Override
    public void setPic(String path) {
        loadHeadPic(path);
    }
    // 保存裁剪后的图片
    public void saveBitmap(Bitmap bitmap) {
        File file = new File(FileDir.mineBgPic);
        if (!file.exists())
            file.mkdirs();
        try {
            bgPic = FileDir.mineBgPic + System.currentTimeMillis() + ".jpg";
            FileOutputStream b = new FileOutputStream(bgPic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);
            b.flush();
            b.close();
            bitmap.recycle();
            uploadCoverBg();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (null == data) {
            return;
        }
        Uri uri = null;
        File file = new File(FileDir.mineBgPic);
        if (!file.exists())
            file.mkdirs();
        System.gc();
        if (requestCode == AppConstant.KITKAT_LESS) {
            uri = data.getData();
            // 调用裁剪方法
            if(!new File(FileDir.mineBgPic).exists())new File(FileDir.mineBgPic).mkdirs();
            bgPic = FileDir.mineBgPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(bgPic));
            GalleryUtils.getInstance().cropPicture(getParent(), uri, imgUri, 10, 7, 1080, 756);
        } else if (requestCode == AppConstant.KITKAT_ABOVE) {
            uri = data.getData();
            if(!new File(FileDir.mineBgPic).exists())new File(FileDir.mineBgPic).mkdirs();
            bgPic = FileDir.mineBgPic + System.currentTimeMillis() + ".jpg";
            Uri imgUri = Uri.fromFile(new File(bgPic));
            // 先将这个uri转换为path，然后再转换为uri
            String thePath = GalleryUtils.getInstance().getPath(this, uri);
            GalleryUtils.getInstance().cropPicture(getParent(),
                    Uri.fromFile(new File(thePath)), imgUri, 10, 7, 1080, 756);
        } else if (requestCode == AppConstant.INTENT_CROP) {
            if(new File(bgPic).exists()){
                uploadCoverBg();
            }else{
                showMsg("裁切失败");
            }
        }
    }
}
