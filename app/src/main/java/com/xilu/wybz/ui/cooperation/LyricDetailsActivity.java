package com.xilu.wybz.ui.cooperation;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.LyricsPosterAdapter;
import com.xilu.wybz.bean.CooperaLyricBean;
import com.xilu.wybz.bean.LyricsPoster;
import com.xilu.wybz.ui.base.ToolbarActivity;
import com.xilu.wybz.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class LyricDetailsActivity extends ToolbarActivity {
    CooperaLyricBean cooperaLyricBean;
    @Bind(R.id.title_tv)
    TextView title_tv;
    @Bind(R.id.author_tv)
    TextView author_tv;
    LyricsPosterAdapter adapter;
    @Bind(R.id.listview)
    ListView listView;
    List<LyricsPoster> lyricsPosters;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_lyric_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("选择歌词");
        initDatas();

    }

    private void initDatas() {
        Intent intent = getIntent();
        cooperaLyricBean = (CooperaLyricBean) intent.getSerializableExtra("cooperaLyricBean");

        setTitle(cooperaLyricBean.getTitle());
        title_tv.setText(cooperaLyricBean.getTitle());
        author_tv.setText(cooperaLyricBean.getAuthor());
        lyricsPosters = new ArrayList<>();
        if (StringUtils.isNotBlank(cooperaLyricBean.getLyrics())) {
            String[] lyricss = cooperaLyricBean.getLyrics().split("\\n");
            for (String lyrics : lyricss) {
                if (StringUtils.isNotBlank(lyrics)) {
                    LyricsPoster lyricsPoster = new LyricsPoster();
                    lyricsPoster.lyrics = lyrics;
                    lyricsPosters.add(lyricsPoster);
                }
            }
        }
        adapter = new LyricsPosterAdapter(this, lyricsPosters);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_use, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_use) {
            Intent intent = new Intent(LyricDetailsActivity.this, CooperaPublish.class);
            intent.putExtra("cooperaLyricBean", cooperaLyricBean);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
