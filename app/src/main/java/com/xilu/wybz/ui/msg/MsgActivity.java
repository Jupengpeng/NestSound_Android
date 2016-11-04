package com.xilu.wybz.ui.msg;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MsgBean;
import com.xilu.wybz.bean.MsgNumBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.MsgNumPresenter;
import com.xilu.wybz.ui.IView.IMsgNumView;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.SystemBarHelper;
import com.xilu.wybz.view.pull.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by June on 16/4/29.
 */
public class MsgActivity extends BaseListActivity<MsgBean> implements IMsgNumView {
    public int[] icons = new int[]{R.drawable.ic_msg_comment, R.drawable.ic_msg_zan,
            R.drawable.ic_msg_fav, R.drawable.ic_msg_bq, R.drawable.ic_msg_notice, R.drawable.ic_hezuoxiaoxi};
    public String[] titles = new String[]{"评论", "点赞", "收藏", "保全消息", "系统消息", "合作消息"};
    public MsgNumPresenter msgNumPresenter;

    public boolean canBack() {
        return false;
    }

    public int msgCount;

    @Override
    protected void initPresenter() {
        msgNumPresenter = new MsgNumPresenter(context, this);
        msgNumPresenter.init();

    }

    @Override
    protected void setUpData() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < icons.length; i++) {
            MsgBean msgBean = new MsgBean();
            msgBean.icon = icons[i];
            msgBean.title = titles[i];
            mDataList.add(msgBean);
        }
        super.setUpData();
    }


    @Override
    public boolean hasPadding() {
        return false;
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_msg, parent, false);
        return new MsgViewHolder(view);
    }

    /**
     * showMsgNum.
     *
     * @param msgBean
     */
    @Override
    public void showMsgNum(MsgNumBean msgBean) {
        if (isDestroy || msgBean == null) return;
        msgCount = msgBean.commentnum + msgBean.fovnum + msgBean.sysmsg + msgBean.zannum + msgBean.copyrightnum;
        changeMsgStatus();
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.get(0).count = msgBean.commentnum;
            mDataList.get(1).count = msgBean.zannum;
            mDataList.get(2).count = msgBean.fovnum;
            mDataList.get(3).count = msgBean.copyrightnum;
            mDataList.get(4).count = msgBean.sysmsg;
            adapter.notifyDataSetChanged();
        }
    }

    //通知首页更新消息提示红点显示的状态
    public void changeMsgStatus() {
        EventBus.getDefault().post(new Event.MsgTipEvent(msgCount > 0));
    }

    @Override
    public void initView() {
        setTitle("消息");

        SystemBarHelper.tintStatusBar(this, Color.argb(255, 0xFF, 0xD7, 0x05));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.main_theme_color));
        mAppBar.setBackgroundColor(getResources().getColor(R.color.main_theme_color));
        SystemBarHelper.setHeightAndPadding(this, mAppBar);

        recycler.enablePullToRefresh(false);

        if(!StringUtils.isBlank(PrefsUtil.getUserInfo(context).loginToken)){
            msgNumPresenter.loadData();
        }
    }

    public class MsgViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_msg_title)
        TextView tvMsgTitle;
        @Bind(R.id.tv_msg_count)
        TextView tvMsgCount;
        @Bind(R.id.iv_msg_icon)
        ImageView ivMsgIcon;

        public MsgViewHolder(View view) {
            super(view);
        }

        @Override
        public void onBindViewHolder(int position) {
            MsgBean msgBean = mDataList.get(position);
            tvMsgTitle.setText(msgBean.title);
            ivMsgIcon.setImageResource(msgBean.icon);
            tvMsgCount.setVisibility(msgBean.count == 0 ? View.GONE : View.VISIBLE);
            tvMsgCount.setText(msgBean.count + "");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, position);
                }
            });
        }

        @Override
        public void onItemClick(View view, int position) {
            switch (position) {
                case 0:
                    startActivity(MsgCommentActivity.class);
                    break;
                case 1:
                    startActivity(MsgZambiaActivity.class);
                    break;
                case 2:
                    startActivity(MsgFavActivity.class);
                    break;
                case 3:
                    startActivity(MsgPreserveActivity.class);
                    break;
                case 4:
                    startActivity(MsgSystemActivity.class);
                    break;
                case 5:

                    break;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.NoticeMsgEvent event) {
        int pos = 0;
        switch (event.getType()) {
            case MyCommon.PUSH_TYPE_COMMENT:
                pos = 0;
                break;
            case MyCommon.PUSH_TYPE_ZAN:
                pos = 1;
                break;
            case MyCommon.PUSH_TYPE_FOV:
                pos = 2;
                break;
            case MyCommon.PUSH_TYPE_COPYRIGHSUCCESS:
            case MyCommon.PUSH_TYPE_COPYRIGHFAIL:
                pos = 3;
                break;
            case MyCommon.PUSH_TYPE_SYSTEMMSG:
                pos = 4;
                break;
        }
        mDataList.get(pos).count = mDataList.get(pos).count + 1;
        msgCount += 1;
        changeMsgStatus();
        adapter.notifyItemChanged(pos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.ClearMsgEvent event) {
        int pos = 0;
        switch (event.getType()) {
            case MyCommon.PUSH_TYPE_COMMENT:
                pos = 0;
                break;
            case MyCommon.PUSH_TYPE_ZAN:
                pos = 1;
                break;
            case MyCommon.PUSH_TYPE_FOV:
                pos = 2;
                break;
            case MyCommon.PUSH_TYPE_COPYRIGHSUCCESS:
            case MyCommon.PUSH_TYPE_COPYRIGHFAIL:
                pos = 3;
                break;
            case MyCommon.PUSH_TYPE_SYSTEMMSG:
                pos = 4;
                break;
        }
        //清空消息之前 先把消息总个数减掉当前分类的消息个数
        msgCount -= mDataList.get(pos).count;
        changeMsgStatus();
        //清空单个分类消息个数
        mDataList.get(pos).count = 0;
        adapter.notifyDataSetChanged();
    }
}
