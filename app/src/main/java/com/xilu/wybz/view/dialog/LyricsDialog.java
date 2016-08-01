package com.xilu.wybz.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.LyricListAdapter;
import com.xilu.wybz.adapter.StringAdapter;
import com.xilu.wybz.bean.LibArr;
import com.xilu.wybz.bean.Lyricat;
import com.xilu.wybz.common.MyHttpClient;
import com.xilu.wybz.http.callback.MyStringCallback;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.utils.ParseUtils;
import com.xilu.wybz.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;


public class LyricsDialog extends Dialog {

    Context mContext;
    EditText focusEt;
    RelativeLayout rl_send;
    HorizontalListView typeHlv;
    ListView lrcLv;
    StringAdapter strAdapter;
    List<String> titles;
    List<String> lyricsList;
    List<Lyricat> lyricats;

    LyricListAdapter lyricsListAdapter;
    public LyricsDialog(Activity context, EditText focusEt) {
        super(context, R.style.CommentDialog);
        this.mContext = context;
        this.focusEt = focusEt;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }

    public View getDialogView() {
        View rootView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_lyrics, null);
        lyricsList = new ArrayList<>();
        lyricats = new ArrayList<>();
        titles = new ArrayList<>();

        lrcLv = (ListView) rootView.findViewById(R.id.lyrics_lv_lrc);
        typeHlv = (HorizontalListView) rootView.findViewById(R.id.lyrics_hlv_type);
        rl_send = (RelativeLayout) rootView.findViewById(R.id.rl_send);

        strAdapter = new StringAdapter(mContext, lyricsList);
        lrcLv.setAdapter(strAdapter);
        lyricsListAdapter = new LyricListAdapter(mContext, titles);
        typeHlv.setAdapter(lyricsListAdapter);

        typeHlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<LibArr> libArrs= lyricats.get(position).libArr;
                for(LibArr libArr : libArrs){
                    if(lyricsList.size()>0)lyricsList.clear();
                    lyricsList.add(libArr.lyrics);
                }
                strAdapter.notifyDataSetChanged();
                lyricsListAdapter.setCurrTitle(titles.get(position));
                lyricsListAdapter.notifyDataSetChanged();
            }
        });

        lrcLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = ((LibArr) parent.getItemAtPosition(position)).lyrics;
                if (focusEt != null) {
                    focusEt.setText(focusEt.getText() + msg + "\n");
                    focusEt.setSelection(focusEt.getText().length());
                }
            }
        });
        com.xilu.wybz.http.HttpUtils httpUtils = new com.xilu.wybz.http.HttpUtils(mContext);
        httpUtils.get(MyHttpClient.getLyricatsUrl(),null,new MyStringCallback(){
            @Override
            public void onResponse(String response) {
                lyricats = ParseUtils.getLyricatsData(mContext,response);
                if(lyricats.size()>0){
                    for(Lyricat lyricat:lyricats){
                        titles.add(lyricat.title);
                    }
                    lyricsListAdapter.setCurrTitle(titles.get(0));
                    lyricsListAdapter.notifyDataSetChanged();
                    List<LibArr> libArrs= lyricats.get(0).libArr;
                    for(LibArr libArr : libArrs){
                        lyricsList.add(libArr.lyrics);
                    }
                    strAdapter.notifyDataSetChanged();
                }
            }
        });
        return rootView;
    }

    public void showDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = DensityUtil.getScreenW(mContext);
        getWindow().setAttributes(params);
        show();
    }
}
