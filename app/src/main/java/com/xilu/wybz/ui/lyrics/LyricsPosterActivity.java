package com.xilu.wybz.ui.lyrics;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.xilu.wybz.R;
import com.xilu.wybz.adapter.LyricsPosterAdapter;
import com.xilu.wybz.bean.LyricsPoster;
import com.xilu.wybz.bean.UserBean;
import com.xilu.wybz.bean.WorksData;
import com.xilu.wybz.common.Event;
import com.xilu.wybz.common.KeySet;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.PrefsUtil;
import com.xilu.wybz.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;

/**
 * Created by hujunwei on 16/7/29.
 */
public class LyricsPosterActivity extends ToolbarActivity{
    @Bind(R.id.listview)
    ListView listView;
    private WorksData worksData;
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
        EventBus.getDefault().register(this);
        initData();
    }

    public void initData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            worksData = (WorksData) bundle.getSerializable(KeySet.WORKS_DATA);
        }else{
            finish();
        }
        mToolbar.setTitleTextColor(Color.WHITE);
        setTitle(worksData.title);
        lyricsPosters = new ArrayList<>();
        if (StringUtil.isNotBlank(worksData.lyrics)) {
            String[] lyricss = worksData.lyrics.split("\\n");
            for(String lyrics : lyricss){
                if(StringUtil.isNotBlank(lyrics)){
                    LyricsPoster lyricsPoster = new LyricsPoster();
                    lyricsPoster.lyrics = lyrics;
                    lyricsPosters.add(lyricsPoster);
                }
            }
        }
        adapter = new LyricsPosterAdapter(this,lyricsPosters);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lyricsPosters.get(position).isChecked = !lyricsPosters.get(position).isChecked;
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_next, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_next:
                String content = "";
                for(LyricsPoster lyricsPoster:lyricsPosters){
                    if(lyricsPoster.isChecked){
                        content+=lyricsPoster.lyrics+"\n";
                    }
                }
                SharePosterActivity.toSharePosterActivity(context,worksData,content);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Event.SavePosterSuccessEvent event){
        finish();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
