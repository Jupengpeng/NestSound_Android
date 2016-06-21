package com.xilu.wybz.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.ui.MyApplication;
import com.xilu.wybz.ui.fragment.WorksDataFragment;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;
import com.xilu.wybz.view.pull.*;

import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/6/3.
 */
public class WorksViewHolder extends com.xilu.wybz.view.pull.BaseViewHolder {
    int itemWidth;
    List<WorksData> mDataList;
    Context mContext;
    String COME;
    @Bind(R.id.iv_cover)
    SimpleDraweeView ivCover;
    @Bind(R.id.tv_look_num)
    TextView tvLookNum;
    @Bind(R.id.tv_fov_num)
    TextView tvFovNum;
    @Bind(R.id.tv_zan_num)
    TextView tvZanNum;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_author)
    TextView tvAuthor;
    OnDeleteListener onDeleteListener;
    public WorksViewHolder(View view, Context context, List<WorksData> worksDataList, String from, OnDeleteListener onDeleteListener) {
        super(view);
        mDataList = worksDataList;
        COME = from;
        mContext = context;
        this.onDeleteListener = onDeleteListener;
        itemWidth = DensityUtil.dip2px(context, 66);
        ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
        //只有我的作品才可以长按删除
        if(onDeleteListener==null)return;
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(v, getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(int position) {
        WorksData worksData = mDataList.get(position);
        String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
        ImageLoadUtil.loadImage(url, ivCover);
        tvName.setText(worksData.title);
        tvAuthor.setText(worksData.author);
        tvLookNum.setText(NumberUtil.format(worksData.looknum));
        tvZanNum.setText(NumberUtil.format(worksData.zannum));
        tvFovNum.setText(NumberUtil.format(worksData.fovnum));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v,position);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(v,position);
                return false;
            }
        });
    }

    public void onItemLongClick(View view, int position) {
        if(onDeleteListener==null)return;
        new MaterialDialog.Builder(mContext)
                .title(R.string.dialog_title)
                .items(R.array.states)
                .itemsColor(Color.RED)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if(which==0){
                            onDeleteListener.deletePos(position);
                        }
                    }
                }).show();
    }
    public interface OnDeleteListener{
        void deletePos(int pos);
    }
    @Override
    public void onItemClick(View view, int position) {
        if (mDataList.get(position).status == 1||mDataList.get(position).status == 0) {
            toPlayPos(position);
        } else if(mDataList.get(position).status == 2){
            int from = 0;
            if (COME.equals("mylyrics")) from = 1;
            else if (COME.equals("myfav")) from = 2;
            LyricsdisplayActivity.toLyricsdisplayActivity(mContext, mDataList.get(position).getItemid(), from, mDataList.get(position).name);
        }
    }

    public void toPlayPos(int position) {
        if (mDataList.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom", mContext);
            if (!playFrom.equals(COME) || MyApplication.ids.size() == 0) {
                if (MyApplication.ids.size() > 0)
                    MyApplication.ids.clear();
                for (WorksData worksData : mDataList) {
                    if (worksData.status == 1) {
                        MyApplication.posMap.put(worksData.getItemid(), position);
                        MyApplication.ids.add(worksData.getItemid());
                    }
                }
            }
            WorksData worksData = mDataList.get(position);
            PlayAudioActivity.toPlayAudioActivity(mContext, worksData.getItemid(), "", COME);
        }
    }
}
