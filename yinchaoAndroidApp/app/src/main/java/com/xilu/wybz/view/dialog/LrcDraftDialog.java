package com.xilu.wybz.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.xilu.wybz.R;


public class LrcDraftDialog extends Dialog {

    Context context;

    LinearLayout llDelete;
    LinearLayout llancel;

    OnDeleteListener listener;
    int position;

    public LrcDraftDialog(Context context,int position) {
        super(context, R.style.CommentDialog);
        this.context = context;
        this.position =position;
        setCanceledOnTouchOutside(true);
        getWindow().setGravity(Gravity.CENTER);

        setContentView(getDialogView());
    }

    View getDialogView() {

        View rootView = LayoutInflater.from(context).inflate(
                R.layout.dialog_lrc_draft, null);
        llDelete = (LinearLayout) rootView.findViewById(R.id.ll_delete);
        llancel = (LinearLayout) rootView.findViewById(R.id.ll_cancel);

        llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.delete(position);
                    dismiss();
                }
            }
        });
        llancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }


    public void setListener(OnDeleteListener listener) {
        this.listener = listener;
    }

    @Override
    public void show() {
        super.show();
    }

    public interface OnDeleteListener{
        void delete(int position);
    }

}
