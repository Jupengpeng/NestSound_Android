package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.adapter.ActionAdapter;
import com.xilu.wybz.bean.ActionBean;
import com.xilu.wybz.utils.DensityUtil;

import java.util.List;


public class ActionMoreDialog extends Dialog{
    Context context;
    ListView listView;
    AdapterView.OnItemClickListener ipl;
    ActionAdapter adapter;
    List<ActionBean> mList;
    public ActionMoreDialog(Context context, AdapterView.OnItemClickListener ipl,List<ActionBean> actionBeans) {
        super(context, R.style.ToastDialog);
        this.context = context;
        this.mList = actionBeans;
        this.ipl = ipl;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialogAnim); //设置窗口弹出动画
        setContentView(getDialogView());
    }

    public View getDialogView() {
        View rootView = LayoutInflater.from(context).inflate(
                R.layout.ll_action_more, null);
        listView = (ListView) rootView.findViewById(R.id.listView);
        TextView tv_cancle = (TextView) rootView.findViewById(R.id.tv_cancle);
        adapter = new ActionAdapter(context,mList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(ipl);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }


    public void showDialog() {
        show();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.dimAmount=0.4f;
        params.width = DensityUtil.getScreenW(context);
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
