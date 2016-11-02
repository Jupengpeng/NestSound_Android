package com.xilu.wybz.ui.cooperation;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.CollectBean;
import com.xilu.wybz.utils.DateFormatUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/21.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectViewHolder> {
    private List<CollectBean> collectBeanList;
    private Context context;


    public CollectAdapter(List<CollectBean> collectBeanList, Context context) {
        this.collectBeanList = collectBeanList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int type);
    }

    private CollectAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(CollectAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public CollectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CollectAdapter.CollectViewHolder holder = new CollectAdapter.CollectViewHolder(LayoutInflater.from(context).inflate(R.layout.collect_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(CollectViewHolder holder, int position) {
        CollectBean collectBean = collectBeanList.get(position);

        if (collectBean != null) {
            switch (collectBean.getStatus()) {
                case 1://正在进行
                    holder.collect_tv_status.setText("正在进行");

                    break;

                case 3://对面停止合作
                    holder.collect_tv_status.setText("对方停止该合作");
                    break;

                case 4://已到期
                    holder.collect_tv_status.setText("已到期");
                    holder.collect_tv_status.setTextColor(Color.parseColor("#ff6161"));
                    break;

                case 8://合作成功
                    holder.collect_tv_status.setText("合作成功");
                    break;
            }
            holder.collect_tv_name.setText(collectBean.getNickname());
            holder.collect_tv_title.setText(collectBean.getTitle());
            holder.collect_tv_participatenum.setText(collectBean.getParticipatenum()+"人参与合作");
            holder.collect_tv_time.setText(DateFormatUtils.formatX1(collectBean.getCreatetime()));
        }

    }

    @Override
    public int getItemCount() {
        return collectBeanList.size();
    }

    class CollectViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.collect_headerurl_iv)
        ImageView collect_headerurl_iv;
        @Bind(R.id.collect_tv_status)
        TextView collect_tv_status;
        @Bind(R.id.collect_tv_name)
        TextView collect_tv_name;
        @Bind(R.id.collect_tv_participatenum)
        TextView collect_tv_participatenum;
        @Bind(R.id.collect_tv_time)
        TextView collect_tv_time;
        @Bind(R.id.collect_tv_title)
        TextView collect_tv_title;

        public CollectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
