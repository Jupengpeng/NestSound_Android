package com.xilu.wybz.ui.msg;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MsgBean;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;
import com.xilu.wybz.ui.find.MoreWorkActivity;
import com.xilu.wybz.view.SystemBarHelper;
import com.xilu.wybz.view.pull.BaseViewHolder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by June on 16/4/29.
 */
public class MsgActivity extends BaseListActivity<MsgBean> {
    public int[] icons = new int[]{R.drawable.ic_msg_comment, R.drawable.ic_msg_zan,
            R.drawable.ic_msg_fav, R.drawable.ic_msg_notice, R.drawable.ic_msg_notice};
    public String[] titles = new String[]{"评论", "点赞", "收藏", "系统消息", "保全消息"};

    public boolean canBack() {
        return false;
    }

    @Override
    protected void initPresenter() {
        setTitle("消息");
//        hideRight();
        SystemBarHelper.tintStatusBar(this, Color.argb(255, 0xFF, 0xD7, 0x05));
        recycler.enablePullToRefresh(false);
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
                    startActivity(MsgSystemActivity.class);
                    break;
                case 4:
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
            case MyCommon.PUSH_TYPE_RECOMENDTOINDEX:
            case MyCommon.PUSH_TYPE_ADDTOSONGLIST:
                pos = 3;
                break;
            case MyCommon.PUSH_TYPE_COPYRIGHSUCCESS:
            case MyCommon.PUSH_TYPE_COPYRIGHFAIL:
                pos = 4;
                break;
        }
        mDataList.get(pos).count = mDataList.get(pos).count+1;
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
            case MyCommon.PUSH_TYPE_RECOMENDTOINDEX:
            case MyCommon.PUSH_TYPE_ADDTOSONGLIST:
                pos = 3;
                break;
            case MyCommon.PUSH_TYPE_SYSTEMMSG:
                pos = 4;
                break;
        }
        mDataList.get(pos).count = 0;
        adapter.notifyItemChanged(pos);
    }
}
