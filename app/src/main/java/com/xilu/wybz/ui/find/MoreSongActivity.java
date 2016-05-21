package com.xilu.wybz.ui.find;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.BasePlayMenuActivity;

import butterknife.Bind;

/**
 * Created by June on 16/5/7.
 */
public class MoreSongActivity extends BasePlayMenuActivity {
    @Bind(R.id.recycler_view_moresong)
    RecyclerView recyclerViewMoresong;
    String type;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_moresong;
    }

    public static void toMoreSongActivity(Context context, String type) {
        Intent intent = new Intent(context, MoreSongActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            type = bundle.getString("type");
        }
        if(type.equals("new")){
            setTitle("最新歌曲");
        }else{
            setTitle("最热歌曲");
        }
    }
}
