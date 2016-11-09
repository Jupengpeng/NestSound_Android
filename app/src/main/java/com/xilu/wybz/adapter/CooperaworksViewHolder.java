package com.xilu.wybz.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.ZnImageLoader;
import com.xilu.wybz.ui.song.PlayAudioActivity;
import com.xilu.wybz.utils.NumberUtil;
import com.xilu.wybz.view.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/6/3.
 */
public class CooperaworksViewHolder extends com.xilu.wybz.view.pull.BaseViewHolder {

    @Bind(R.id.iv_cover)
    ImageView iv_cover;
    @Bind(R.id.tv_name)
    TextView tv_name;
    @Bind(R.id.tv_makeword)
    TextView tv_makeword;
    @Bind(R.id.tv_look_num)
    TextView tv_look_num;
    @Bind(R.id.tv_fov_num)
    TextView tv_fov_num;
    @Bind(R.id.tv_zan_num)
    TextView tv_zan_num;
    List<WorksData> mDataList;
    Context mContext;
    String COME;
    OnItemClickListener OnItemClickListener;

    public CooperaworksViewHolder(View view, Context context, List<WorksData> worksDataList, String from, OnItemClickListener OnItemClickListener) {
        super(view);
        mDataList = worksDataList;
        COME = from;
        mContext = context;
        this.OnItemClickListener = OnItemClickListener;


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
        ZnImageLoader.getInstance().displayImage(worksData.pic, ZnImageLoader.getInstance().options, iv_cover);
        tv_name.setText(worksData.title);
        tv_makeword.setText(worksData.getComAuthor());
        tv_look_num.setText(NumberUtil.format(worksData.looknum));
        tv_fov_num.setText(NumberUtil.format(worksData.fovnum));
        tv_zan_num.setText(NumberUtil.format(worksData.zannum));
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onItemClick(v, position);
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

    @Override
    public void onItemClick(View view, int position) {
        WorksData worksData = mDataList.get(position);
        PlayAudioActivity.toPlayAudioActivity(mContext, worksData.getItemid(), "", "hezuo");

    }

    public interface OnItemClickListener {
        void onClick(int pos, int which);
    }
}
