package com.xilu.wybz.view.menuitem;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.xilu.wybz.R;
/**
 * Created by hujunwei on 16/5/21.
 */
public class SelectPicProvider extends ActionProvider {
    private View view;
    private TextView tv_count;
    private Context context;
    private View.OnClickListener mOnClickListener;
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public SelectPicProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.menu_selectpic_send, null);
        tv_count = (TextView) view.findViewById(R.id.tv_count);
        view.setOnClickListener(mOnClickListener);
        return view;
    }
    public void setOnClickListener(View.OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }
    public void setCount(int count){
        tv_count.setVisibility(count==0?View.GONE:View.VISIBLE);
        tv_count.setText(""+count);
    }
}
