package com.xilu.wybz.ui.lyrics;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.LyricsPosterAdapter;
import com.xilu.wybz.bean.LyricsPoster;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.FileDir;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.common.MyCommon;
import com.xilu.wybz.presenter.DownPicPresenter;
import com.xilu.wybz.ui.IView.ILoadPicView;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.BitmapUtils;
import com.xilu.wybz.utils.MD5Util;
import com.xilu.wybz.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hujunwei on 16/7/29.
 */
public class LyricsPosterActivity extends ToolbarActivity implements ILoadPicView{
    @Bind(R.id.blurImageView)
    ImageView blurImageView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.app_bar_layout)
    RelativeLayout appBarLayout;
    @Bind(R.id.listview)
    ListView listview;
    private WorksData worksData;
    DownPicPresenter downPicPresenter;
    List<LyricsPoster> lyricsPosters;
    LyricsPosterAdapter adapter;
    public static void toLyricsPosterActivity(Context context, WorksData worksData) {
        Intent intent = new Intent(context, LyricsPosterActivity.class);
        intent.putExtra(KeySet.WORKS_DATA, worksData);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_lyricsposter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        initData();
    }

    public void initData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
        }else{
            finish();
        }
        String pic = worksData.getPic();
        if (StringUtil.isNotBlank(pic)) {
            loadPic(pic);
        }
        if (StringUtil.isNotBlank(worksData.lyrics)) {
            String[] lyricss = worksData.lyrics.split("\\n");
            for(String lyrics : lyricss){
                if(StringUtil.isNotBlank(lyrics)){
                    LyricsPoster lyricsPoster = new LyricsPoster();
                    lyricsPoster.lyrics = lyrics;
                    lyricsPosters.add(lyricsPoster);
                }
            }
            adapter = new LyricsPosterAdapter(context,lyricsPosters);
            listview.setAdapter(adapter);
        }
        downPicPresenter = new DownPicPresenter(this,this);
    }

    public void loadPic(String imageUrl) {
        File file = new File(FileDir.blurPic);
        if (!file.exists()) file.mkdirs();

        String path = FileDir.blurPic + MD5Util.getMD5String(worksData.pic);
        if (new File(path).exists()) {//加载本地
            blurImageView.setImageBitmap(BitmapUtils.getSDCardImg(path));
        } else {//下载并保存到本地
            imageUrl = MyCommon.getImageUrl(imageUrl, 200, 200);
            downPicPresenter.downLoadPic(imageUrl, path);
        }
    }

    @Override
    public void setPic(Bitmap bitmap) {
        if (blurImageView != null)
            blurImageView.setImageBitmap(bitmap);
    }

    @Override
    public void initView() {

    }
}
