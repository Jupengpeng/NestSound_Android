package com.xilu.wybz.ui.setting;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.MsgBean;
import com.xilu.wybz.ui.base.BaseListActivity;
import com.xilu.wybz.ui.msg.MsgCommentActivity;
import com.xilu.wybz.ui.msg.MsgFavActivity;
import com.xilu.wybz.ui.msg.MsgSystemActivity;
import com.xilu.wybz.ui.msg.MsgZambiaActivity;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/9/20.
 */
public class OtherAccountActivity extends BaseListActivity<MsgBean>{
    public int[] icons = new int[]{R.drawable.ic_share_wx, R.drawable.ic_share_wb, R.drawable.ic_share_qq};
    public String[] titles = new String[]{"微信", "微博", "QQ"};
    @Override
    protected void initPresenter() {
        setTitle("账号绑定");
        hideRight();
    }
    @Override
    public boolean hasPadding() {
        return false;
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
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bd, parent, false);
        return new MsgViewHolder(view);
    }
    public class MsgViewHolder extends BaseViewHolder {
        @Bind(R.id.tv_msg_title)
        TextView tvMsgTitle;
        @Bind(R.id.tv_bd)
        TextView tvBd;
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
            tvBd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
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
            }
        }
    }
}
