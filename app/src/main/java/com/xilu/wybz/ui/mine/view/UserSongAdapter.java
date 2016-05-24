package com.xilu.wybz.ui.mine.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.view.pull.BaseViewHolder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/22.
 */

public class UserSongAdapter extends UserBaseAdapter {

    public UserSongAdapter(Context context) {
        super(context);
    }

    public UserSongAdapter(Context context, List<WorksData> data) {
        super(context, data);
    }

    @Override
    protected int getDataCount() {
        return datas.size();
    }

    @Override
    public int getDataViewType(int position) {
        return 0;
    }

    protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(mContext).inflate(R.layout.item_user_song, parent, false);

        return new UserSongViewHolder(view);
    }


    public class UserSongViewHolder extends BaseViewHolder {

        @Bind(R.id.iv_image)
        ImageView ivImage;
        @Bind(R.id.iv_play)
        ImageView ivPlay;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_text)
        TextView tvText;
        @Bind(R.id.iv_show)
        ImageView ivShow;
        @Bind(R.id.tv_show_num)
        TextView tvShowNum;
        @Bind(R.id.iv_like)
        ImageView ivLike;
        @Bind(R.id.tv_like_num)
        TextView tvLikeNum;
        @Bind(R.id.iv_zan)
        ImageView ivZan;
        @Bind(R.id.tv_zan_num)
        TextView tvZanNum;

//        "createDate":"2015-10-10",
//                "fovnum":10,
//                "itemid":2,
//                "looknum":10,
//                "name":"歌曲标题",
//                "pic":"http://a/a.jpg",
//                "zannum":2

        public UserSongViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            WorksData worksData = datas.get(position);

            tvTitle.setText(worksData.name);
            tvText.setText(worksData.name);

            tvShowNum.setText(NumberUtil.format(worksData.looknum));
            tvLikeNum.setText(NumberUtil.format(worksData.fovnum));
            tvZanNum.setText(NumberUtil.format(worksData.zannum));

        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

}
