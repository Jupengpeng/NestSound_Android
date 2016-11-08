package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.service.MainService;
import com.xilu.wybz.ui.lyrics.LyricsdisplayActivity;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ImageLoadUtil;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtils;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.util.ArrayList;
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
    @Bind((R.id.iv_lock))
    ImageView ivLock;
    OnItemClickListener OnItemClickListener;

    public WorksViewHolder(View view, Context context, List<WorksData> worksDataList, String from, OnItemClickListener OnItemClickListener) {
        super(view);
        mDataList = worksDataList;
        COME = from;
        mContext = context;
        this.OnItemClickListener = OnItemClickListener;
        itemWidth = DensityUtil.dip2px(context, 66);
        ivCover.setLayoutParams(new LinearLayout.LayoutParams(itemWidth, itemWidth));
        //只有我的作品才可以长按删除
        if (OnItemClickListener == null) return;
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
        if (StringUtils.isBlank(worksData.pic) && worksData.status == 2) {
            worksData.pic = MyHttpClient.QINIU_URL + MyCommon.getLyricsPic().get((int) (Math.random() * 10));
        }
        String url = MyCommon.getImageUrl(worksData.getPic(), itemWidth, itemWidth);
        ImageLoadUtil.loadImage(url, ivCover);
        if (COME.equals(MyCommon.MYSONG)) {
            ivLock.setVisibility(worksData.status == 1 ? View.GONE : View.VISIBLE);
        } else if (COME.equals(MyCommon.MYLYRICS)) {
            ivLock.setVisibility(worksData.status == 1 ? View.GONE : View.VISIBLE);
        } else {
            ivLock.setVisibility(View.GONE);
        }
        tvName.setText(worksData.title);
        tvAuthor.setText(worksData.author);
        tvLookNum.setText(NumberUtil.format(worksData.looknum));
        tvZanNum.setText(NumberUtil.format(worksData.zannum));
        tvFovNum.setText(NumberUtil.format(worksData.fovnum));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v, position);
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(v, position);
                return false;
            }
        });
    }

    public void onItemLongClick(View view, int position) {
        if (OnItemClickListener == null) return;
        List<String> itemNames = new ArrayList<>();
        if (COME.equals("mysong")||COME.equals("mylyrics")) {
            itemNames.add(mDataList.get(position).status == 0 ? "设为公开" : "设为私密");
            itemNames.add("删除");
        } else if (COME.equals("myfav")) {
            itemNames.add("取消收藏");
        } else if (COME.equals("hezuo")){
            itemNames.add("删除");
        }
        new MaterialDialog.Builder(mContext)
                .title(R.string.dialog_title)
                .items(itemNames)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        OnItemClickListener.onClick(position, which);
                    }
                }).show();

    }

    public interface OnItemClickListener {
        void onClick(int pos, int which);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position >= 0 && position < mDataList.size()) {
            //ygs 他人页面收藏打不开  缺少type判断
            WorksData worksData = mDataList.get(position);
            if (worksData.type == 1 || mDataList.get(position).type == 0 || mDataList.get(position).type == 3) {
                toPlayPos(position);
                return;
            }
            if (worksData.type == 2) {
                LyricsdisplayActivity.toLyricsdisplayActivity(mContext, mDataList.get(position).getItemid(), mDataList.get(position).name);
                return;
            }
            if (worksData.type == 5){
                PlayAudioActivity.toPlayAudioActivity(mContext, worksData.getItemid(), "", COME);
            }

        }
    }

    public void toPlayPos(int position) {
        if (mDataList.size() > 0) {
            String playFrom = PrefsUtil.getString("playFrom", mContext);
            if (!playFrom.equals(COME) || MainService.ids.size() == 0) {
                if (MainService.ids.size() > 0)
                    MainService.ids.clear();
                for (WorksData worksData : mDataList) {
                    if (worksData.status == 1) {
                        MainService.ids.add(worksData.getItemid());
                    }
                }
            }
            WorksData worksData = mDataList.get(position);
            PlayAudioActivity.toPlayAudioActivity(mContext, worksData.getItemid(), "", COME);
        }
    }
}
