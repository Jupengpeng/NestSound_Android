package com.xilu.wybz.ui.mine.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.utils.DateFormatUtils;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/5/22.
 */

public class UserInspirationAdapter extends UserBaseAdapter {


    public UserInspirationAdapter(Context context) {
        super(context);
    }

    public UserInspirationAdapter(Context context, List<WorksData> data) {
        super(context, data);
    }


    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_user_inspiration, parent, false);

        return new UserViewHolder(view);
    }


    public class UserViewHolder extends BaseViewHolder {

        @Bind(R.id.iv_background)
        ImageView ivBackground;
        @Bind(R.id.date_day)
        TextView dateDay;
        @Bind(R.id.date_time)
        TextView dateTime;
        @Bind(R.id.tv_text)
        TextView tvText;
        @Bind(R.id.iv_frequency)
        ImageView ivFrequency;
        @Bind(R.id.iv_frequency_icon)
        ImageView ivFrequencyIcon;

        public UserViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {

            WorksData worksData = datas.get(position);

            int type = getItemType(worksData);

            DateFormatUtils.DateText dt = DateFormatUtils.prase2DateText(worksData.createdate);

            dateDay.setText(dt.getDay());
            dateTime.setText(dt.getYearMonth());

            ivBackground.setVisibility(View.GONE);
            ivFrequency.setVisibility(View.GONE);
            ivFrequencyIcon.setVisibility(View.GONE);
            tvText.setVisibility(View.GONE);

            if ((type & 0x01) == 1 ){
                ivBackground.setVisibility(View.VISIBLE);
            }
            if ((type & 0x02) == 2 ){
                if ((type & 0x04) == 4){
                    ivFrequencyIcon.setVisibility(View.VISIBLE);
                }
                if ((type & 0x05) == 1){
                    ivFrequency.setImageResource(R.drawable.user_frequency_white);
                    ivFrequency.setVisibility(View.VISIBLE);
                }
                if ((type & 0x05) == 0){
                    ivFrequency.setImageResource(R.drawable.user_frequency_yellow);
                    ivFrequency.setVisibility(View.VISIBLE);
                }
            }
            if ((type & 0x04) == 4 ){
                tvText.setVisibility(View.VISIBLE);
                tvText.setText(worksData.spirecontent);
            }

        }

        @Override
        public void onItemClick(View view, int position) {

        }


        public int getItemType(WorksData worksData){
            int type = 0x00;
            if (StringUtil.isNotBlank(worksData.pics)){
                type = 0x01<<0 | type;
            }
            if (StringUtil.isNotBlank(worksData.audio)){
                type = 0x01<<1 | type;
            }
            if (StringUtil.isNotBlank(worksData.spirecontent)){
                type = 0x01<<2 | type;
            }
            Log.d("type","type="+type);
            return type;
        }
    }

}
