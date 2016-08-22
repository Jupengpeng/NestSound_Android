package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.LyricsPresenter;
import com.xilu.wybz.ui.IView.ILyricsView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.ui.mine.UserInfoActivity;
import com.xilu.wybz.ui.setting.SettingFeedActivity;
import com.xilu.wybz.ui.song.CommentActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.DateTimeUtil;
import com.xilu.wybz.utils.NetWorkUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringStyleUtil;
import com.xilu.wybz.utils.SystemUtils;
import com.xilu.wybz.view.CircleImageView;
import com.xilu.wybz.view.dialog.ActionMoreDialog;
import com.xilu.wybz.view.dialog.ShareDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/3/10 0010.
 */
public class LyricsdisplayActivity extends ToolbarActivity implements ILyricsView, AdapterView.OnItemClickListener {
    @Bind(R.id.ll_loading)
    LinearLayout ll_loading;
    @Bind(R.id.ll_nonet)
    LinearLayout ll_nonet;
    @Bind(R.id.ly_content)
    TextView ly_content;
    @Bind(R.id.iv_head)
    CircleImageView ivHead;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_comment_num)
    TextView tvCommentNum;
    @Bind(R.id.iv_zan)
    ImageView ivZan;
    @Bind(R.id.iv_fav)
    ImageView ivFav;
    @Bind(R.id.tv_favnum)
    TextView tvFavNum;
    @Bind(R.id.tv_zannum)
    TextView tvZanNum;
    String title;
    int id;
    int pos;
    WorksData worksData;
    ShareDialog shareDialog;
    List<ActionBean> actionBeanList;
    ActionMoreDialog actionMoreDialog;
    LyricsPresenter lyricsPresenter;
    String[] actionTitles = new String[]{"分享", "举报", "编辑"};
    String[] actionTypes = new String[]{"share", "jubao", "edit"};
    public static void toLyricsdisplayActivity(Context context, int id, String title) {
        Intent intent = new Intent(context, LyricsdisplayActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_lyricsdisplay;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        lyricsPresenter = new LyricsPresenter(this, this);
        lyricsPresenter.init();
        initData();
    }

    public void initView() {
        title = getResources().getString(R.string.app_name);
        worksData = new WorksData();
    }

    public void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            if (!TextUtils.isEmpty(title))
                setTitle(title);
            id = bundle.getInt("id");
            pos = bundle.getInt("pos");
            loadData();
        }
    }

    @OnClick({R.id.rl_zan, R.id.rl_fav, R.id.iv_nonet, R.id.rl_head, R.id.rl_comment, R.id.tv_author})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_fav:
                if (SystemUtils.isLogin(context)) {
                    lyricsPresenter.setCollectionState(worksData.itemid, worksData.uid);
                }
                break;
            case R.id.rl_zan:
                if (SystemUtils.isLogin(context)) {
                    lyricsPresenter.zan(id, worksData.uid);
                }
                break;
            case R.id.iv_nonet:
                loadData();
                break;
            case R.id.rl_comment:
                toCommentActivity();
                break;
            case R.id.tv_author:
            case R.id.rl_head:
                if (worksData.uid > 0 && worksData.uid != PrefsUtil.getUserId(context)) {
                    UserInfoActivity.toUserInfoActivity(context, worksData.uid, worksData.author);
                }
                break;
        }
    }

    public void loadData() {
        lyricsPresenter.getLyric(id);
    }

    //修改歌词以后 更新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.SaveLyricsSuccessEvent event) {
        if (event.getWhich() == 2) {
            worksData = event.getLyricsdisplayBean();
            loadTitleContent();
        }
    }

    //更新评论数量
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.UpdataCommentNumEvent event) {
        if (event.getType() == 2) {
            worksData.commentnum = worksData.getCommentnum() + event.getNum();
            updateCommentNum();
        }
    }

    //显示歌词名称和内容
    public void loadTitleContent() {
        if (!TextUtils.isEmpty(worksData.getTitle()))
            setTitle(worksData.getTitle());
        String str = worksData.getLyrics();
        //根据不同的模板id来显示歌词
        if (!TextUtils.isEmpty(str))
            ly_content.setText(StringStyleUtil.getLyrics(worksData));
    }

    public void updateZanFavNum() {
        if (worksData.zannum > 0) {
            tvZanNum.setVisibility(View.VISIBLE);
            tvZanNum.setText(NumberUtil.format(worksData.zannum));
        } else {
            tvZanNum.setVisibility(View.INVISIBLE);
        }
        if (worksData.fovnum > 0) {
            tvFavNum.setVisibility(View.VISIBLE);
            tvFavNum.setText(NumberUtil.format(worksData.fovnum));
        } else {
            tvFavNum.setVisibility(View.INVISIBLE);
        }
    }

    public void updateCommentNum() {
        if (worksData.commentnum > 0) {
            if (worksData.commentnum < 1000) {
                tvCommentNum.setText(NumberUtil.format(worksData.commentnum));
            } else {
                tvCommentNum.setText("999+");
            }
        } else {
            tvCommentNum.setText("");
        }
    }

    @Override
    public void showProgressBar() {
        showLoading(ll_loading);
    }

    @Override
    public void hideProgressBar() {
        disMissLoading(ll_loading);
    }

    @Override
    public void showErrorView() {
        if (isDestroy) {
            return;
        }
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            ll_nonet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void loadLyrics(WorksData worksData) {
        if (isDestroy) {
            return;
        }
        boolean isMe = worksData.uid==PrefsUtil.getUserId(context);
        actionBeanList = new ArrayList<>();
        for (int i = 0; i < actionTitles.length; i++) {
            ActionBean actionBean = new ActionBean();
            actionBean.setTitle(actionTitles[i]);
            actionBean.setType(actionTypes[i]);
            actionBeanList.add(actionBean);
        }
        if(!isMe)actionBeanList.remove(actionBeanList.size()-1);//不是我的时候 不能编辑
        this.worksData = worksData;
        worksData.type = worksData.status;//type表示是否公开
        worksData.status = 2;//status=2歌词
        ivZan.setImageResource(worksData.getIsZan() == 0 ? R.drawable.ic_lyrics_zan1 : R.drawable.ic_lyrics_zan2);
        ivFav.setImageResource(worksData.getIscollect() == 0 ? R.drawable.ic_lyrics_fav1 : R.drawable.ic_lyrics_fav2);
        loadTitleContent();
        tvAuthor.setText(worksData.getAuthor());
        tvTime.setText(DateTimeUtil.timestamp2Date(worksData.getCreatedate()));
        String headUrl = worksData.headurl.replace(MyCommon.defult_head, "");
        loadHeadImage(headUrl.replace(MyCommon.defult_head, ""), ivHead);
        updateCommentNum();
        updateZanFavNum();
    }

    @Override
    public void zanStart() {
        if (isDestroy) {
            return;
        }
        ivZan.setEnabled(false);
    }

    @Override
    public void zanSuccess() {
        if (isDestroy) {
            return;
        }
        worksData.isZan = 1 - worksData.isZan;
        if (worksData.isZan == 1) showMsg("点赞成功");
        worksData.zannum = worksData.zannum + (worksData.isZan == 1 ? 1 : -1);
        updateZanFavNum();
        EventBus.getDefault().post(new Event.UpdateWorkNum(worksData, 2));
        ivZan.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dianzan_anim));
        ivZan.setImageResource(worksData.isZan == 0 ? R.drawable.ic_lyrics_zan1 : R.drawable.ic_lyrics_zan2);

    }

    @Override
    public void zanFail() {

    }

    @Override
    public void zanFinish() {
        if (isDestroy) {
            return;
        }
        ivZan.setClickable(true);
    }

    @Override
    public void favStart() {

    }

    @Override
    public void favSuccess() {
        if (isDestroy) {
            return;
        }
        worksData.iscollect = 1 - worksData.iscollect;
        if (worksData.iscollect == 1) showMsg("收藏成功！");
        worksData.fovnum = worksData.fovnum + (worksData.iscollect == 1 ? 1 : -1);
        updateZanFavNum();
        EventBus.getDefault().post(new Event.UpdateWorkNum(worksData, 1));
        EventBus.getDefault().post(new Event.UpdataWorksList(worksData, 3, 1 - worksData.iscollect));
        ivFav.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dianzan_anim));
        ivFav.setImageResource(worksData.iscollect == 0 ? R.drawable.ic_lyrics_fav1 : R.drawable.ic_lyrics_fav2);
    }

    @Override
    public void favFail() {
    }

    @Override
    public void favFinish() {

    }

    @Override
    public void loadPicSuccess(String pic) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(BitmapUtils.getSDCardImg(pic));
        mToolbar.setLogo(bitmapDrawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_more:
                if(actionBeanList==null||actionBeanList.size()==0) break;
                if (actionMoreDialog == null) {
                    actionMoreDialog = new ActionMoreDialog(this, this, actionBeanList);
                }
                if (!actionMoreDialog.isShowing()) {
                    actionMoreDialog.showDialog();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (lyricsPresenter != null)
            lyricsPresenter.cancelRequest();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ActionBean actionBean = actionBeanList.get(position);
        Intent intent;
        if (actionBean.getType().equals("share")) {
            if (worksData != null && worksData.getItemid() > 0) {
                if (shareDialog == null) {
                    shareDialog = new ShareDialog(LyricsdisplayActivity.this, worksData, 0);
                }
                if (!shareDialog.isShowing()) {
                    shareDialog.showDialog();
                }
            }
        } else if (actionBean.getType().equals("edit")) {
            MakeWordActivity.toMakeWordActivity(context, worksData);
        } else if (actionBean.getType().equals("jubao")) {
            intent = new Intent(context, SettingFeedActivity.class);
            intent.putExtra("type", 1);
            startActivity(intent);
        }
        if (actionMoreDialog != null)
            actionMoreDialog.dismiss();
    }

    public void toCommentActivity() {
        CommentActivity.toCommentActivity(context, worksData.itemid,2,false);
    }

}