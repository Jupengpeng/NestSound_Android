package com.xilu.wybz.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.StringAdapter;
import com.xilu.wybz.bean.LyricsListBean;
import com.xilu.wybz.utils.DensityUtil;
import com.xilu.wybz.view.HorizontalListView;


public class LyricsDialog extends Dialog {

    Context mContext;
    EditText focusEt;
    RelativeLayout rl_send;
    HorizontalListView typeHlv;
    ListView lrcLv;
    StringAdapter strAdapter;

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
        lrcLv = (ListView) rootView.findViewById(R.id.lyrics_lv_lrc);
        typeHlv = (HorizontalListView) rootView.findViewById(R.id.lyrics_hlv_type);
        rl_send = (RelativeLayout) rootView.findViewById(R.id.rl_send);

        strAdapter = new StringAdapter(mContext, R.layout.item_lrc);
        lrcLv.setAdapter(strAdapter);

        typeHlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LyricsListBean lyricsListBean = (LyricsListBean) parent.getItemAtPosition(position);
                strAdapter.setData(lyricsListBean.getLyricsList());
            }
        });

        lrcLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = (String) parent.getItemAtPosition(position);
                if (focusEt != null) {
                    focusEt.setText(focusEt.getText() + msg + "\n");
                    focusEt.setSelection(focusEt.getText().length());
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
