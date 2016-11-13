package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CooperaDetailsBean;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.utils.DateFormatUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */

public class CooperaDetatilesAdapter extends BaseAdapter {
    private List<CooperaDetailsBean.CompleteListBean> CompleteListBean;
    private Context context;
    private int flag;
    private boolean b = false;

    public CooperaDetatilesAdapter(List<CooperaDetailsBean.CompleteListBean> completeListBean, Context context, int flag) {
        CompleteListBean = completeListBean;
        this.context = context;
        this.flag = flag;
    }

    @Override
    public int getCount() {
        return CompleteListBean.size();
    }

    @Override
    public Object getItem(int position) {
        return CompleteListBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    private CompleteListAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CompleteListAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.completework_item, null);
            holder.complete_createtime = (TextView) convertView.findViewById(R.id.complete_createtime);
            holder.complete_lUsername = (TextView) convertView.findViewById(R.id.complete_lUsername);
            holder.complete_wUsername = (TextView) convertView.findViewById(R.id.complete_wUsername);
            holder.complete_title = (TextView) convertView.findViewById(R.id.complete_title);
            holder.complete_iv = (ImageView) convertView.findViewById(R.id.complete_iv);
            holder.complete_isaccess_bt = (Button) convertView.findViewById(R.id.complete_isaccess_bt);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CooperaDetailsBean.CompleteListBean completeBean = CompleteListBean.get(position);
        if (completeBean != null) {
            holder.complete_title.setText("歌曲名: " + completeBean.getTitle());
            holder.complete_lUsername.setText("作词    : " + completeBean.getLUsername());
            holder.complete_wUsername.setText("作曲    : " + completeBean.getWUsername());
            holder.complete_createtime.setText(DateFormatUtils.formatX1(completeBean.getCreatetime()));
            ZnImageLoader.getInstance().displayImage(completeBean.getPic(), ZnImageLoader.getInstance().headOptions, holder.complete_iv);
            if (flag == 1) {
                holder.complete_isaccess_bt.setVisibility(View.GONE);
            }
            for (int i = 0; i < CompleteListBean.size(); i++) {
                if (CompleteListBean.get(i).getAccess() == 1) {
                    b = true;
                }
            }

            if (b == true && completeBean.getAccess() == 1) {
                holder.complete_isaccess_bt.setBackgroundResource(R.drawable.finishbt_bg);
                holder.complete_isaccess_bt.setText("已采纳");
            } else {
                holder.complete_isaccess_bt.setVisibility(View.GONE);
            }

            if (completeBean.getAccess() != 1 && b == false) {
                ViewHolder finalHolder = holder;
                holder.complete_isaccess_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mOnItemClickListener.onItemClick(finalHolder.complete_isaccess_bt, position, 1);
                    }
                });
            }

        }

        return convertView;
    }


    class ViewHolder {
        ImageView complete_iv;
        TextView complete_title;
        TextView complete_wUsername;
        TextView complete_lUsername;
        TextView complete_createtime;
        Button complete_isaccess_bt;

    }
}
