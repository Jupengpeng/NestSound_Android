package com.xilu.wybz.ui.preserve;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xilu.wybz.R;
import com.xilu.wybz.ui.base.ToolbarActivity;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/14.
 */
public class PreservePersonInfoActivity extends ToolbarActivity {


    @Bind(R.id.preservation_name)
    TextView preservationName;
    @Bind(R.id.preservation_card_id)
    TextView preservationCardId;
    @Bind(R.id.preservation_phone)
    TextView preservationPhone;
    @Bind(R.id.user_info_container)
    LinearLayout userInfoContainer;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_preserv_person_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("个人信息" +
                "");

        userInfoContainer.setVisibility(View.VISIBLE);
    }
}
